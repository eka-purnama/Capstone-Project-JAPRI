const jwt = require('jsonwebtoken');
const db = require('../config/db');
const bcrypt = require('bcrypt');

const register = async (req, res) => {
  try {
    const { email, name, username, password, role } = req.body;

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

    const hashedPassword = await bcrypt.hash(password, 10);

    const userData = {
      email,
      name,
      username,
      password: hashedPassword,
      role,
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

    const isPasswordValid = await bcrypt.compare(password, user.password);

    if (!isPasswordValid) {
      return res.status(401).json({ message: 'Invalid email or password' });
    }

    const token = jwt.sign({ id: userQuery.docs[0].id, username: user.username, role: user.role }, 'your-secret-key', { expiresIn: '30d' });

    res.json({ message: 'Login successful', token });
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};
const logout = async (req, res) => {
  res.json({ message: 'Logout successful' });
};

module.exports = {
  register,
  login,
  logout,
};
