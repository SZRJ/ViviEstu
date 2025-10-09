// src/controllers/favoritoscontroller.js
let favoritos = [
  { id: 1, nombre_propiedad: 'San Miguel', direccion: 'Av. Pardo 321', precio: 2200 },
  { id: 2, nombre_propiedad: 'Pueblo Libre', direccion: 'Av. Brasil 123', precio: 2500 }
];

// Obtener todos los favoritos
exports.obtenerFavoritos = (req, res) => {
  res.json(favoritos);
};

// Agregar un nuevo favorito
exports.agregarFavorito = (req, res) => {
  const nuevo = {
    id: favoritos.length + 1,
    ...req.body
  };
  favoritos.push(nuevo);
  res.status(201).json(nuevo);
};

// Eliminar un favorito
exports.eliminarFavorito = (req, res) => {
  const { id } = req.params;
  favoritos = favoritos.filter(f => f.id != id);
  res.json({ mensaje: 'Favorito eliminado correctamente' });
};
