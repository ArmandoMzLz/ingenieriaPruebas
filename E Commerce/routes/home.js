const express = require('express');
const db = require('../db');
const router = express.Router();

router.get('/', (req, res) => {
    const query = typeof req.query.q === 'string' ? req.query.q.trim(): '';
    const categoria = typeof req.query.categoria === 'string' ? req.query.categoria.trim(): '';

    let sqlQuery = '';
    let params = [];

    if(categoria) {
        sqlQuery = 'CALL mostrarProductosCategoria(?)';
        params = [categoria]
    } else {
        sqlQuery = 'CALL mostrarProductos(?)';
        params = [query]
    }
    
    db.query(sqlQuery, params, (err, results) => {
        if(err) {
            console.log(err);
            return res.render('./Home', {productos: [], error: 'Algo salio mal. Intentelo más tarde.'});
        }

        const productos = results[0];
        const added = req.query.added === 'true';
        res.render('./Home', {productos, error: null, query, categoria, added});
    });
});

module.exports = router;