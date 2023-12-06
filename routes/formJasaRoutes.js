const express = require('express');
const router = express.Router();
const formJasaController = require('../controllers/formJasaController');

router.post('/', formJasaController.addFormJasa);

router.get('/', formJasaController.getAllFormJasa);

router.get('/:id', formJasaController.getFormJasaById);

module.exports = router;
