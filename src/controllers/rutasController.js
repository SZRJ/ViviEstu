exports.obtenerRutas = async (req, res) => {
  try {
    const { id_zona } = req.params;

    // Datos simulados (puedes conectarlo a la BD si quieres)
    const rutas = [
      { medio: 'Bus', tiempo_aprox: '25 min', costo: 2.5 },
      { medio: 'Metro', tiempo_aprox: '15 min', costo: 3.0 },
      { medio: 'Bicicleta', tiempo_aprox: '30 min', costo: 0 }
    ];

    res.status(200).json({
      zona_id: id_zona,
      mensaje: 'Rutas recomendadas para la zona',
      rutas
    });
  } catch (error) {
    res.status(500).json({ mensaje: 'Error al obtener rutas', error: error.message });
  }
};
