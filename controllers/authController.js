const jwt = require('jsonwebtoken');
const db = require('../config/db');
const crypto = require('crypto');

// fungsi daftar
const register = async (req, res) => {
  try {
    const { email, username, password, role } = req.body;

    // Validasi email apakah penah digunakan
    if (email) {
      const existingEmailUser = await db.collection('users').where('email', '==', email).limit(1).get();
      if (!existingEmailUser.empty) {
        return res.status(400).json({ error: true, message: 'Email sudah terdaftar!' });
      }
    }

    // cek apakah username sudah pernah digunakan
    const existingUsernameUser = await db.collection('users').where('username', '==', username).limit(1).get();
    if (!existingUsernameUser.empty) {
      return res.status(400).json({ error: true, message: 'Username sudah pernah digunakan!' });
    }

    const hashedPassword = hashPassword(password);

    const userData = {
      email,
      username,
      password: hashedPassword,
      role,
      name: '',
      phone_number: '',
      photo_url: '',
      gender: '',
      address: '',
      personal_data: {
        skill: [],
        deskripsi: '',
      }
    };

    const userRef = await db.collection('users').add(userData);
    res.json({ error: false, message: 'Registrasi berhasil!', userId: userRef.id });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Internal Server Error' });
  }
};

// fungsi login
const login = async (req, res) => {
  try {
    const { email, password } = req.body;

    // validasi apakah email terdaftar
    const userQuery = await db.collection('users').where('email', '==', email).limit(1).get();
    if (userQuery.empty) {
      return res.status(401).json({ error: true, message: 'Email atau Password salah!' });
    }

    const user = userQuery.docs[0].data();
    const userId = userQuery.docs[0].id; // Mendapatkan ID dokumen

    // validasi apakah password benar
    const isPasswordValid = comparePasswords(password, user.password);
    if (!isPasswordValid) {
      return res.status(401).json({ error: true, message: 'Email atau Password salah!' });
    }

    const token = jwt.sign({ id: userId, username: user.username, role: user.role }, 'your-secret-key', { expiresIn: '30d' });

    res.json({ error: false, message: 'Login berhasil!', token, username: user.username, id: userId, role: user.role });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Internal Server Error' });
  }
};

// fungsi logout
const logout = async (req, res) => {
  res.json({ error: true, message: 'Logout berhasil!' });
};

// fungsi enkripsi password
function hashPassword(password) {
  const hash = crypto.createHash('sha256');
  hash.update(password);
  return hash.digest('hex');
}

// fungsi membandingkan password
function comparePasswords(enteredPassword, hashedPassword) {
  const hashedEnteredPassword = hashPassword(enteredPassword);
  return hashedEnteredPassword === hashedPassword;
}

module.exports = {
  register,
  login,
  logout,
};
