const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');
const authenticateToken = require('../middleware/authenticateToken');

router.post('/register', authController.register);
router.post('/login', authController.login);
router.get('/logout', authenticateToken, authController.logout);

module.exports = router;
