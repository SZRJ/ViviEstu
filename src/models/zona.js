const { DataTypes } = require('sequelize');
const db = require('../config/db');

const Zona = db.define('Zona', {
  id_zona: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true,
  },
  nombre: DataTypes.STRING,
  precio_promedio: DataTypes.FLOAT,
  seguridad: DataTypes.STRING,
  transporte_disponible: DataTypes.STRING,
  distrito: DataTypes.STRING,
  latitud: DataTypes.DECIMAL(10,6),
  longitud: DataTypes.DECIMAL(10,6)

}, {
  timestamps: false,
  tableName: 'zonas',
});

module.exports = Zona;
