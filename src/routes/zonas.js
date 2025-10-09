const express = require('express');
const router = express.Router();
const { obtenerZona } = require('../controllers/zonasController');

router.get('/:id', obtenerZona);

module.exports = router;
