const express = require('express');
const router = express.Router();
const userController = require('../controllers/userController');

router.get('/', userController.getAllUsers);
router.get('/:id', userController.getUserById);
router.put('/edit-photo/:id', userController.uploadProfilePhoto);
router.post('/get-status-data', userController.getStatusData);
router.put('/edit-profile/:id', userController.editUser);
router.put('/edit-password/:id', userController.editPassword);

module.exports = router;
