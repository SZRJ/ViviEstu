const { DataTypes } = require('sequelize');
const db = require('../config/db');
const Zona = require('./zona');

const Favorito = db.define('Favorito', {
  id_favorito: {
    type: DataTypes.INTEGER,
    primaryKey: true,
    autoIncrement: true,
  },
  id_usuario: {
    type: DataTypes.INTEGER,
    allowNull: false,
  },
  fecha_guardado: {
    type: DataTypes.DATE,
    defaultValue: DataTypes.NOW,
  }
}, {
  timestamps: false,
  tableName: 'favoritos',
});

// Relación con zona
Favorito.belongsTo(Zona, { foreignKey: 'id_zona' });
Zona.hasMany(Favorito, { foreignKey: 'id_zona' });

module.exports = Favorito;
