const jwt = require('jsonwebtoken');
const db = require('../config/db');
const crypto = require('crypto');

const register = async (req, res) => {
  try {
    const { email, username, password, role } = req.body;

    // Validasi penulisan email
    if (email) {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(email)) {
        return res.status(400).json({ error: 'Penulisan Email tidak valid' });
      }

      // Check if the email is already registered
      const existingEmailUser = await db.collection('users').where('email', '==', email).limit(1).get();
      if (!existingEmailUser.empty) {
        return res.status(400).json({ error: true, message: 'Email is already registered' });
      }
    }

    // Check if the username is already registered
    const existingUsernameUser = await db.collection('users').where('username', '==', username).limit(1).get();

    if (!existingUsernameUser.empty) {
      return res.status(400).json({ error: true, message: 'Username is already registered' });
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
      },
      feedbacks: [],
    };

    const userRef = await db.collection('users').add(userData);

    res.json({ error: false, message: 'Registration successful', userId: userRef.id });
  } catch (error) {
    console.error(error);
    res.status(500).json({ error: true, message: 'Internal Server Error' });
  }
};

const login = async (req, res) => {
  try {
    const { email, password } = req.body;

    const userQuery = await db.collection('users').where('email', '==', email).limit(1).get();

    if (userQuery.empty) {
      return res.status(401).json({ message: 'Invalid email or password' });
    }

    const user = userQuery.docs[0].data();
    const userId = userQuery.docs[0].id; // Mendapatkan ID dokumen

    const isPasswordValid = comparePasswords(password, user.password);

    if (!isPasswordValid) {
      return res.status(401).json({ message: 'Invalid email or password' });
    }

    const token = jwt.sign({ id: userId, username: user.username, role: user.role }, 'your-secret-key', { expiresIn: '30d' });

    res.json({ message: 'Login successful', token, username: user.username, id: userId, role: user.role });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};

const logout = async (req, res) => {
  res.json({ message: 'Logout successful' });
};

function hashPassword(password) {
  const hash = crypto.createHash('sha256');
  hash.update(password);
  return hash.digest('hex');
}

function comparePasswords(enteredPassword, hashedPassword) {
  const hashedEnteredPassword = hashPassword(enteredPassword);
  return hashedEnteredPassword === hashedPassword;
}

module.exports = {
  register,
  login,
  logout,
};
