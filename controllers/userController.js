const db = require('../config/db');
const storage = require('../config/bucket');
const multer = require('multer');
const bcrypt = require('bcrypt');
const { json } = require('express');
const sharp = require('sharp');

const bucket = storage.bucket('japri-dev-bucket');


const path = require('path');
const url = require('url');

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

    // cek user ada atau tidak
    if (!userSnapshot.exists) {
      return res.status(404).json({ error: 'User not found' });
    }
    
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
    const userId = req.params.id; // ambil id user

    const userDoc = await db.collection('users').doc(userId).get();
    // cek user ada atau tidak
    if (!userDoc.exists) {
      return res.status(404).json({ error: 'User not found' });
    }
    const username = userDoc.data()?.username;
    const oldPhotoUrl = userDoc.data()?.photo_url;

    // Upload new profile photo
    upload.single('photo')(req, res, async (err) => {
      if (err instanceof multer.MulterError) {
        return res.status(400).json({ message: 'File size exceeds the limit' });
      } else if (err) {
        return res.status(500).json({ message: 'Error uploading photo' });
      }

      // cek apakah ada file yg terupload 
      if (!req.file) {
        return res.status(400).json({ message: 'No file uploaded' });
      }

      const file = req.file;
      const fileName = `user_photos/${username}_${Date.now()}_${file.originalname.replace(/\s+/g, '_')}`;

      try {
        // Resize and upload image
        const resizedBuffer = await resizeImage(file.buffer);
        const stream = await uploadImage(fileName, resizedBuffer, file.mimetype);

        // Handle upload errors
        stream.on('error', (err) => {
          console.error(err);
          res.status(500).json({ message: 'Error uploading photo' });
        });

        // Handle upload completion
        stream.on('finish', async () => {
          const photoUrl = `https://storage.googleapis.com/${bucket.name}/${fileName}`;

          // Delete old photo if it exists
          if (oldPhotoUrl) {
            await deleteOldPhoto(oldPhotoUrl);
          }

          // Update user document with new photo URL
          await savePhotoUrl(userId, photoUrl);
          res.json({ message: 'Profile photo uploaded successfully', photoUrl });
        });
        
        stream.end(resizedBuffer);

      } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Internal Server Error' });
      }
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

// mengubah ukuran gambar
const resizeImage = async (buffer) => {
  try {
    return await sharp(buffer).resize(500, 500).toBuffer();
  } catch (error) {
    console.error('Error resizing image:', error);
    throw error;
  }
}

// mengupload gambar ke storage
const uploadImage = async (fileName, buffer, mimeType) => {
  const file = bucket.file(fileName);
  const stream = file.createWriteStream({ metadata: { contentType: mimeType } });
  stream.write(buffer);
  return stream;
}

// menghapus gambar lama
const deleteOldPhoto = async (oldPhotoUrl) => {
  try {
    const fileName = path.basename(url.parse(oldPhotoUrl).pathname);
    await bucket.file("user_photos/"+fileName).delete();
  } catch (error) {
    console.error('Error deleting old photo:', error);
  }
}

module.exports = {
  getAllUsers,
  getUserById,
  editUser,
  getStatusData,
  uploadProfilePhoto,
};
