const express = require('express');
const db = require('../db');
const { parse } = require('path');
const router = express.Router();

router.get('/:idProducto/', (req, res) => {
    const idProducto = req.params.idProducto;

    const sqlProductoID = 'CALL obtenerProductoID(?)';

    db.query(sqlProductoID, [idProducto], (err, results) => {
        if(err) {
            console.log(err)
            return res.render('./Detail', {producto: null, error: 'Algo salio mal. Intentelo más tarde'});
        }

        const producto = results[0]?.[0];

        if(!producto) {
            return res.render('./Detail', {producto: null, error: 'Producto no encontrado'});
        }

        const sqlProductoCategoria = 'CALL obtenerProductoCategoria(?)';

        db.query(sqlProductoCategoria, [producto.categoriaProducto], (err2, results2) => {
            if(err2) {
                console.log(err2);
                return res.render('./Detail', { producto: null, similares: [], error: 'Algo salio mal. Intentelo más tarde'});
            }

            const productosSimilares = results2[0].filter(p => p.idProducto !== producto.idProducto);
            const added = req.query.added === 'true';
            res.render('./Detail', {producto, similares: productosSimilares, error: null, added});
        });
    });
});

module.exports = router;