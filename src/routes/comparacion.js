const express = require('express');
const router = express.Router();
const { compararZonas } = require('../controllers/comparacioncontroller');

router.get('/', compararZonas);

module.exports = router;
