// src/controllers/busquedaController.js
exports.buscarPropiedades = (req, res) => {
  const { filtro } = req.query;

  const propiedades = [
    { nombre: 'San Miguel', precio: 2200 },
    { nombre: 'Pueblo Libre', precio: 2500 },
    { nombre: 'Jesús María', precio: 2700 },
    { nombre: 'Miraflores', precio: 3600 }
  ];

  const match = filtro?.match(/precio\s*<\s*(\d+)/);
  let resultados = propiedades;

  if (match) {
    const limite = parseFloat(match[1]);
    resultados = propiedades.filter(p => p.precio < limite);
  }

  res.json({ filtro, resultados });
};
