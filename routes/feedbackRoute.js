const express = require('express');
const router = express.Router();
const feedbackController = require('../controllers/feedbackController');

router.post('/:username', feedbackController.addFeedback);
router.get('/:username', feedbackController.getRatingUser);


module.exports = router;
