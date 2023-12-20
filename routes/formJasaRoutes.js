const express = require('express');
const router = express.Router();
const formJasaController = require('../controllers/formJasaController');

router.post('/', formJasaController.addFormJasa);
router.put('/:id', formJasaController.updateFormJasaDone);
router.get('/:id', formJasaController.getFormJasaById);

module.exports = router;
