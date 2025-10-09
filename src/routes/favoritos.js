const express = require('express');
const router = express.Router();

const {
  obtenerFavoritos,
  agregarFavorito,
  eliminarFavorito
} = require('../controllers/favoritoscontroller');

router.get('/', obtenerFavoritos);
router.post('/', agregarFavorito);
router.delete('/:id', eliminarFavorito)

module.exports = router;
