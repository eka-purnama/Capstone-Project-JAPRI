const db = require('../config/db');
const storage = require('../config/bucket');
const multer = require('multer');
const crypto = require('crypto');
const axios = require('axios');

const bucket = storage.bucket('japri-dev-bucket');

const path = require('path');
const url = require('url');

// fungsi dapatkan semua user sekaligus mendapatkan ratingnya
const getAllUsers = async (req, res) => {
  try {
    const usersSnapshot = await db.collection('users').where('role', '==', 'penyedia_jasa').get();
    const users = [];

    for (const doc of usersSnapshot.docs) {
      const userData = doc.data();
      // panggil fungsi dapatkan rating
      const rating = await getRatingUser(userData.username);

      users.push({
        id: doc.id,
        ...userData,
        rating: rating,
      });
    }

    // urutkan data berdasarkan averagee rating tertinggi ke terendah
    users.sort((a, b) => (b.rating.averageRating || 0) - (a.rating.averageRating || 0));

    res.json(users);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Internal Server Error' });
  }
};

// fungsi dapatkan rating
const getRatingUser = async (username) => {
  try {
    const userRef = await db.collection('formJasa').where('penyedia_jasa', '==', username).get();

    let totalRating = 0;
    let totalFeedback = 0;

    // ambil semua data yang sesuai, olah ratingnya
    userRef.forEach((doc) => {
      const feedbackData = doc.data();

      if (feedbackData.feedback) {
        const rating = feedbackData.feedback.rating;

        if (rating) {
          totalRating += rating;
          totalFeedback++;
        }
      }
    });

    const averageRating = totalFeedback > 0 ? parseFloat((totalRating / totalFeedback).toFixed(1)) : false;

    return { totalFeedback: totalFeedback, averageRating: averageRating };
  } catch (error) {
    console.error(error);
    return { totalFeedback: 0, averageRating: false };
  }
};

// fungsi dapatkan user berdasarkan id
const getUserById = async (req, res) => {
  try {
    const userId = req.params.id;
    const userDoc = await db.collection('users').doc(userId).get();

    if (!userDoc.exists) {
      res.status(404).json({ error: true, message: 'Pengguna tidak ditemukan!' });
      return;
    }

    // panggil fungsi dapatkan rating
    const rating = await getRatingUser(userDoc.data().username);

    const userData = {
      id: userDoc.id,
      ...userDoc.data(),
      rating: rating,
    };

    res.json(userData);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Internal Server Error' });
  }
};

// fungsi edit profil user
const editUser = async (req, res) => {
  try {
    const userId = req.params.id;
    const { email, name, phone_number, gender, address, personal_data } = req.body;

    // Ambil data user dari Firestore
    const userSnapshot = await db.collection('users').doc(userId).get();
    const userData = userSnapshot.data();

    // cek user ada atau tidak
    if (!userSnapshot.exists) {
      return res.status(404).json({ error: true, message: 'Pengguna tidak ditemukan!' });
    }

    // Validasi email
    if (email) {
      if (email !== userData.email) {
        // Jika email diisi dan berbeda dengan email lama, lakukan validasi
        const existingUser = await db.collection('users').where('email', '==', email).get();
        if (!existingUser.empty) {
          // Jika email sudah ada di database, kirim response error
          return res.status(400).json({ error: true, message: 'Email sudah terdaftar!' });
        }
      }
    }

    // Validasi untuk mengubah field yang diisi oleh user saja
    const updatedData = {
      email: email ? email : userData.email, // Gunakan email baru jika diisi, jika tidak, gunakan email lama
      name: name ? name : userData.name,
      phone_number: phone_number ? phone_number : userData.phone_number,
      gender: gender ? gender : userData.gender,
      address: address ? address : userData.address,
      personal_data: personal_data ? personal_data : userData.personal_data,
    };

    // Update data user di Firestore
    await db.collection('users').doc(userId).update(updatedData);

    res.json({ error: false, message: 'Pengguna berhasil dirubah!' });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Internal Server Error' });
  }
};

// fungsi edit password user
const editPassword = async (req, res) => {
  try {
    const userId = req.params.id;
    const { password, confirm_password } = req.body;

    // Ambil data user dari Firestore
    const userSnapshot = await db.collection('users').doc(userId).get();
    const userData = userSnapshot.data();

    // cek user ada atau tidak
    if (!userSnapshot.exists) {
      return res.status(404).json({ error: true, message: 'Pengguna tidak ditemukan!' });
    }

    // Validasi validasi password
    const passwordValid = validatePassword(password, confirm_password);
    if (passwordValid.error) {
      switch (passwordValid.type) {
        case 'length':
          return res.status(400).json({ error: true, message: 'Panjang Password minimal 8 karakter!' });
        case 'combination':
          return res.status(400).json({ error: true, message: 'Password harus menggunakan kombinasi huruf besar, huruf kecil, angka, dan simbol!' });
        case 'general':
          return res.status(400).json({ error: true, message: 'Password terlalu umum!' });
        case 'confirm':
          return res.status(400).json({ error: true, message: 'Password dan Konfirmasi Password harus sama!' });

        default:
          return res.status(400).json({ error: true, message: 'Password dan Konfirmasi Password harus sama!' });
      }
    }

    const updatedData = {
      password: password ? await hashPassword(password) : userData.password, // Hash password baru jika diisi, jika tidak, gunakan password lama
    };
    // Update data user di Firestore
    await db.collection('users').doc(userId).update(updatedData);
    res.json({ error: false, message: 'Password pengguna berhasil dirubah!' });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Internal Server Error' });
  }
};

// fungsi validasi password
const validatePassword = (password, confirm_password) => {
  // Validasi panjang password
  if (password.length < 8) {
    return { error: true, type: 'length' };
  }

  // Validasi kombinasi karakter
  const hasUpperCase = /[A-Z]/.test(password);
  const hasLowerCase = /[a-z]/.test(password);
  const hasNumber = /[0-9]/.test(password);
  const hasSymbol = /[!@#$%^&*()_+-={}[\]|\;:'",.<>?/]/.test(password);
  if (!hasUpperCase || !hasLowerCase || !hasNumber || !hasSymbol) {
    return { error: true, type: 'combination' };
  }

  // Validasi pola umum
  const commonPasswords = ['Ab12345678', 'Password1', 'Qwerty123', '1234567890', 'Abcd1234'];
  if (commonPasswords.includes(password)) {
    return { error: true, type: 'general' };
  }

  // Validasi bahwa password dan confirm_password sama
  if (password !== confirm_password) {
    return { error: true, type: 'confirm' };
  }

  return true;
};

// fungsi dapatkan status pekerjaan
const getStatusData = async (req, res) => {
  try {
    const { status, username } = req.body;

    // Mendapatkan data form jasa dengan status 'proses' dan username cocok dengan 'pemberi_jasa'
    const querySnapshotPemberi = await db.collection('formJasa').where('status', '==', status).where('penyedia_jasa', '==', username).get();

    // Mendapatkan data form jasa dengan status 'proses' dan username cocok dengan 'pengguna_jasa'
    const querySnapshotPengguna = await db.collection('formJasa').where('status', '==', status).where('pengguna_jasa', '==', username).get();

    // Menggabungkan hasil kedua query
    const combinedResults = [...querySnapshotPemberi.docs, ...querySnapshotPengguna.docs];

    // Mengurutkan data berdasarkan createdAt yang paling terbaru
    const sortedResults = combinedResults.sort((a, b) => b.data().created_at - a.data().created_at);

    const formData = sortedResults.map((doc) => {
      const data = doc.data();
      return {
        id: doc.id,
        ...data,
      };
    });

    res.json(formData);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Internal Server Error' });
  }
};

// fungsi validasi dan menyimpan gambar
const upload = multer({
  storage: multer.memoryStorage(),
  limits: {
    fileSize: 5 * 1024 * 1024,
  },
});

// fungsi upload foto profil user
const uploadProfilePhoto = async (req, res) => {
  try {
    const userId = req.params.id; // ambil id user

    const userDoc = await db.collection('users').doc(userId).get();
    // cek user ada atau tidak
    if (!userDoc.exists) {
      return res.status(404).json({ error: true, message: 'Pengguna tidak ditemukan!' });
    }
    const username = userDoc.data()?.username;
    const oldPhotoUrl = userDoc.data()?.photo_url;

    // Upload new profile photo
    upload.single('photo')(req, res, async (err) => {
      if (err instanceof multer.MulterError) {
        return res.status(400).json({ error: true, message: 'Ukuran file melebihi batas!' });
      } else if (err) {
        return res.status(500).json({ error: true, message: 'Upload foto gagal!' });
      }

      // cek apakah ada file yg terupload
      if (!req.file) {
        return res.status(400).json({ error: true, message: 'Tidak ada foto yang diupload!' });
      }

      // atur nama file
      const file = req.file;
      const fileName = `user_photos/${username}_${Date.now()}_${file.originalname.replace(/\s+/g, '_')}`;

      try {
        const fileBuffer = file.buffer;
        // upload gambar ke bucket
        const stream = await uploadImage(fileName, fileBuffer, file.mimetype);

        // jika gagal simpan ke bucket
        stream.on('error', (err) => {
          console.error(err);
          res.status(500).json({ error: true, message: 'Upload foto gagal!' });
        });

        // jika berhasi simpan ke bucket
        stream.on('finish', async () => {
          const photoUrl = `https://storage.googleapis.com/${bucket.name}/${fileName}`;

          // hapus foto lama jika ada
          if (oldPhotoUrl) {
            await deleteOldPhoto(oldPhotoUrl);
          }

          // update photoUrl dengan url baru
          await savePhotoUrl(userId, photoUrl);
          res.json({ error: false, message: 'Foto profil berhasil dirubah', photoUrl });
        });

        stream.end(fileBuffer);
      } catch (error) {
        console.error(error);
        res.status(500).json({ error: true, message: 'Internal Server Error' });
      }
    });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Internal Server Error' });
  }
};

// fungsi update photo url
const savePhotoUrl = async (userId, photoUrl) => {
  const userRef = db.collection('users').doc(userId);
  await userRef.update({
    photo_url: photoUrl,
  });
};

// fungsi mengupload gambar ke storage
const uploadImage = async (fileName, buffer, mimeType) => {
  const file = bucket.file(fileName);
  const stream = file.createWriteStream({ metadata: { contentType: mimeType } });
  stream.write(buffer);
  return stream;
};

// fungsi menghapus gambar lama
const deleteOldPhoto = async (oldPhotoUrl) => {
  try {
    const fileName = path.basename(url.parse(oldPhotoUrl).pathname);
    const file = bucket.file('user_photos/' + fileName);
    const [exists] = await file.exists();
    if (exists) {
      await bucket.file('user_photos/' + fileName).delete();
    }
  } catch (error) {
    console.error('Error deleting old photo:', error);
  }
};

// fungsi enkripsi password
function hashPassword(password) {
  const hash = crypto.createHash('sha256');
  hash.update(password);
  return hash.digest('hex');
}

const predict = async (req, res) => {
  try {
    // Ambil teks dari req.body
    const inputText = req.body.text;

    // Kirim permintaan ke API pihak ketiga (Flask API) untuk prediksi
    const predictionResponse = await axios.post('http://127.0.0.1:5000/predict', { text: inputText });

    // Ambil 1 kata awal dari respon prediksi
    const predictedSkill = predictionResponse.data.prediction.split(' ')[0].toLowerCase();

    const usersSnapshot = await db.collection('users').where('personal_data.skill', 'array-contains', predictedSkill).get();

    const users = [];

    // Proses setiap dokumen pengguna
    for (const doc of usersSnapshot.docs) {
      const userData = doc.data();

      // Panggil fungsi untuk mendapatkan rating
      const rating = await getRatingUser(userData.username);

      users.push({
        id: doc.id,
        ...userData,
        rating: rating,
      });
    }

    // Urutkan data berdasarkan average rating tertinggi ke terendah
    users.sort((a, b) => (b.rating.averageRating || 0) - (a.rating.averageRating || 0));

    res.json(users);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Internal Server Error' });
  }
};

const bidangJasa = async (req, res) => {
  try {
    const bidangJasaParam = req.params.bidang.toLowerCase();

    // Logika untuk menentukan skill berdasarkan bidang jasa
    const bidangJasaData = {
      teknisi: ['Teknisi'],
      pelayanan: [
        'Bellman',
        'Waiter',
        'Host',
        'Perawat',
        'Kasir',
        'Pemuat',
        'Penjaga',
        'Pustakawan',
        'Co-Host',
        'Pelayan',
        'Pengantar',
        'Dishwasher',
        'Helper',
        'Pemaket',
        'Asisten',
        'Sopir',
        'Pengasuh',
        'Freelance',
        'Cashier',
        'Pembantu',
        'Kenek',
        'Pemetik',
      ],
      pertukangan: ['Pengrajin', 'Kuli', 'Tukang', 'Buruh', 'Penjahit'],
      media: ['Host', 'Soundman', 'Co-Host', 'Navigator', 'Fotografer', 'Videographer', 'Content'],
      logistik: ['Driver', 'Penanam', 'Sorter', 'Checker', 'Navigator', 'Kurir', 'Pengajar', 'Kurir', 'Pengajar'],
      pendidikan: ['Pengajar', 'Penulis'],
    };

    const skills = bidangJasaData[bidangJasaParam] || [];

    // Ambil pengguna berdasarkan skill dan bidang
    const usersSnapshot = await db.collection('users').where('personal_data.skill', 'array-contains-any', skills).get();

    const users = [];

    // Proses setiap dokumen pengguna
    for (const doc of usersSnapshot.docs) {
      const userData = doc.data();

      // Panggil fungsi untuk mendapatkan rating
      const rating = await getRatingUser(userData.username);

      users.push({
        id: doc.id,
        ...userData,
        rating: rating,
      });
    }

    // Urutkan data berdasarkan average rating tertinggi ke terendah
    users.sort((a, b) => (b.rating.averageRating || 0) - (a.rating.averageRating || 0));

    res.json(users);
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Internal Server Error' });
  }
};

module.exports = {
  getAllUsers,
  getUserById,
  editUser,
  getStatusData,
  uploadProfilePhoto,
  editPassword,
  predict,
  bidangJasa,
};
