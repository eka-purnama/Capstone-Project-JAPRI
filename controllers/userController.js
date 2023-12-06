const db = require('../config/db');
const storage = require('../config/bucket');
const multer = require('multer');
const bcrypt = require('bcrypt');
const { json } = require('express');

const bucket = storage.bucket('japri-dev-bucket');

const getAllUsers = async (req, res) => {
  try {
    const usersSnapshot = await db.collection('users').get();
    const users = [];

    usersSnapshot.forEach((doc) => {
      users.push({
        id: doc.id,
        ...doc.data(),
      });
    });

    res.json(users);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

const getUserById = async (req, res) => {
  try {
    const userId = req.params.id;
    const userDoc = await db.collection('users').doc(userId).get();

    if (!userDoc.exists) {
      res.status(404).json({ message: 'User not found' });
      return;
    }

    const userData = {
      id: userDoc.id,
      ...userDoc.data(),
    };

    res.json(userData);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

const editUser = async (req, res) => {
  try {
    const userId = req.params.id;
    const { email, name, password, phone_number, gender, address, personal_data } = req.body;

    // Ambil data user dari Firestore
    const userSnapshot = await db.collection('users').doc(userId).get();
    const userData = userSnapshot.data();

    // Validasi untuk mengubah field yang diisi oleh user saja
    const updatedData = {
      email: email ? email : userData.email, // Gunakan email baru jika diisi, jika tidak, gunakan email lama
      name: name ? name : userData.name,
      password: password ? await bcrypt.hash(password, 10) : userData.password, // Hash password baru jika diisi, jika tidak, gunakan password lama
      phone_number: phone_number ? phone_number : userData.phone_number,
      gender: gender ? gender : userData.gender,
      address: address ? address : userData.address,
      personal_data: personal_data ? personal_data : userData.personal_data,
    };

    // Update data user di Firestore
    await db.collection('users').doc(userId).update(updatedData);

    res.json({ message: 'User updated successfully' });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

const getStatusData = async (req, res) => {
  try {
    const { status, username } = req.body;

    // Mendapatkan data form jasa dengan status 'proses' dan username cocok dengan 'pemberi_jasa'
    const querySnapshotPemberi = await db.collection('formJasa').where('status', '==', status).where('pemberi_jasa', '==', username).get();

    // Mendapatkan data form jasa dengan status 'proses' dan username cocok dengan 'pengguna_jasa'
    const querySnapshotPengguna = await db.collection('formJasa').where('status', '==', status).where('pengguna_jasa', '==', username).get();

    // Menggabungkan hasil kedua query
    const combinedResults = [...querySnapshotPemberi.docs, ...querySnapshotPengguna.docs];

    const formData = combinedResults.map((doc) => {
      const data = doc.data();
      return {
        id: doc.id,
        ...data,
      };
    });

    res.json(formData);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

const upload = multer({
  storage: multer.memoryStorage(),
  limits: {
    fileSize: 5 * 1024 * 1024,
  },
});

const uploadProfilePhoto = async (req, res) => {
  try {
    const userId = req.params.id; // Mengambil ID pengguna dari token

    upload.single('photo')(req, res, (err) => {
      if (err instanceof multer.MulterError) {
        return res.status(400).json({ message: 'File size exceeds the limit' });
      } else if (err) {
        return res.status(500).json({ message: 'Error uploading photo' });
      }

      if (!req.file) {
        return res.status(400).json({ message: 'No file uploaded' });
      }

      const file = req.file;
      const fileName = `user_photos/${userId}_${Date.now()}_${file.originalname.replace(/\s+/g, '_')}`;
      const fileBuffer = file.buffer;

      const stream = bucket.file(fileName).createWriteStream({
        metadata: {
          contentType: file.mimetype,
        },
      });

      stream.on('error', (err) => {
        console.error(err);
        res.status(500).json({ message: 'Error uploading photo' });
      });

      stream.on('finish', () => {
        const photoUrl = `https://storage.googleapis.com/${bucket.name}/${fileName}`;
        // Menyimpan URL foto di field photo_url pada koleksi users
        savePhotoUrl(userId, photoUrl);
        res.json({ message: 'Profile photo uploaded successfully', photoUrl });
      });

      stream.end(fileBuffer);
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

const savePhotoUrl = async (userId, photoUrl) => {
  const userRef = db.collection('users').doc(userId);
  await userRef.update({ photo_url: photoUrl });
};

module.exports = {
  getAllUsers,
  getUserById,
  editUser,
  getStatusData,
  uploadProfilePhoto,
};
