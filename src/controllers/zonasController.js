// src/controllers/zonasController.js
exports.obtenerZona = (req, res) => {
  const { id } = req.params;

  const zonas = {
    1: { nombre: 'Miraflores', descripcion: 'Zona moderna con acceso a transporte y universidades.', lat: -12.121, lng: -77.03 },
    2: { nombre: 'Surco', descripcion: 'Zona tranquila ideal para estudiantes.', lat: -12.15, lng: -76.98 },
    3: { nombre: 'San Miguel', descripcion: 'Cercana al mar y con varias rutas de buses.', lat: -12.08, lng: -77.09 }
  };

  const zona = zonas[id];
  if (!zona) {
    return res.status(404).json({ nombre: 'Zona no encontrada', descripcion: 'N/A', lat: -12.0464, lng: -77.0428 });
  }

  res.json(zona);
};
