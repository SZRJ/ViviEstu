const express = require('express');
const router = express.Router();
const { obtenerRutas } = require('../controllers/rutasController');

// ✅ Asegúrate que esta ruta exista y coincida con el nombre del método exportado
router.get('/:id_zona', obtenerRutas);

module.exports = router;
