const express = require('express');
const cors = require('cors');
require('dotenv').config();
const db = require('./config/db');

const app = express();
app.use(cors());
app.use(express.json());

const zonasRoutes = require('./routes/zonas');
app.use('/api/zonas', zonasRoutes);

const PORT = process.env.PORT || 3000;
const favoritosRoutes = require('./routes/favoritos');

app.use('/api/favoritos', favoritosRoutes);
db.sync().then(() => {
  app.listen(PORT, () => console.log(`🚀 Servidor corriendo en puerto ${PORT}`));
});
app.use(express.static('public'));



const rutasRoutes = require('./routes/rutas');
const busquedaRoutes = require('./routes/busqueda');
const comparacionRoutes = require('./routes/comparacion');


app.use('/api/comparar', comparacionRoutes);

app.use('/api/busqueda', busquedaRoutes);

app.use('/api/zonas', zonasRoutes);
app.use('/api/rutas', rutasRoutes);

app.use(express.static('public'));
