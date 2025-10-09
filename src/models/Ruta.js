const { DataTypes } = require('sequelize');
const db = require('../config/db');
const Zona = require('./zona');

const Ruta = db.define('Ruta', {
  id_ruta: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true,
  },
  destino: DataTypes.STRING,
  medio_transporte: DataTypes.STRING,
  duracion_aprox: DataTypes.STRING,
  costo_aprox: DataTypes.STRING,
}, {
  timestamps: false,
  tableName: 'rutas',
});

// Relación: una zona tiene muchas rutas
Zona.hasMany(Ruta, { foreignKey: 'id_zona' });
Ruta.belongsTo(Zona, { foreignKey: 'id_zona' });

module.exports = Ruta;
