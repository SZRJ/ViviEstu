const Zona = require('../models/zona');

exports.compararZonas = async (req, res) => {
  try {
    const { ids } = req.query;

    if (!ids) {
      return res.status(400).json({ mensaje: 'Debe especificar los IDs de las zonas a comparar.' });
    }

    const listaIds = ids.split(',').map(id => parseInt(id.trim())).filter(id => !isNaN(id));

    if (listaIds.length === 0 || listaIds.length > 3) {
      return res.status(400).json({ mensaje: 'Debe comparar entre 1 y 3 zonas máximo.' });
    }

    const zonas = await Zona.findAll({ where: { id_zona: listaIds } });

    if (zonas.length === 0) {
      return res.status(404).json({ mensaje: 'No se encontraron las zonas especificadas.' });
    }

   
    const resumen = {
      total: zonas.length,
      zonas_comparadas: zonas.map(z => ({
        id_zona: z.id_zona,
        nombre: z.nombre,
        distrito: z.distrito,
        precio_promedio: z.precio_promedio,
        seguridad: z.seguridad,
        transporte: z.transporte_disponible
      })),
      estadisticas: {
        precio_promedio_general:
          zonas.reduce((acc, z) => acc + z.precio_promedio, 0) / zonas.length,
        distrito_mas_repetido: (() => {
          const conteo = {};
          zonas.forEach(z => conteo[z.distrito] = (conteo[z.distrito] || 0) + 1);
          return Object.keys(conteo).reduce((a, b) => conteo[a] > conteo[b] ? a : b);
        })()
      }
    };

    res.status(200).json(resumen);

  } catch (error) {
    res.status(500).json({ mensaje: 'Error al comparar zonas', error: error.message });
  }
};
