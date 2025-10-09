// src/routes/busqueda.js
const express = require('express');
const router = express.Router();

// 👇 Aquí importamos la función correcta desde el controlador
const { buscarPropiedades } = require('../controllers/busqueda.controller');

// ✅ Definimos la ruta GET para la búsqueda
router.get('/', buscarPropiedades);

module.exports = router;
