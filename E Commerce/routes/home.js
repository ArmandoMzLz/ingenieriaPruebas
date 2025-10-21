const express = require('express');
const db = require('../db');
const { render } = require('ejs');
const router = express.Router();

router.get('/', (req, res) => {
    const query = typeof req.query.q === 'string' ? req.query.q.trim(): '';
    const sqlQuery = 'CALL mostrarProductos(?)';

    db.query(sqlQuery, [query], (err, results) => {
        if(err) {
            console.log(err);
            return res.render('./Home', {productos: [], error: 'Algo salio mal. Intentelo más tarde.'});
        }

        const productos = results[0];
        res.render('./Home', {productos, error: null, query});
    });
});

router.post('/agregarCarrito', (req, res) => {
    const { idProducto, nombre, precio, imagen } = req.body;

    if(!req.session.carrito) {
        req.session.carrito = [];
    }

    const productoExistente = req.session.carrito.find(p => p.idProducto === parseInt(idProducto));

    if(productoExistente) {
        productoExistente.cantidad += 1;
    } else {
        req.session.carrito.push({
            idProducto: parseInt(idProducto),
            nombre,
            precio: parseFloat(precio),
            imagen,
            cantidad: 1
        });
    }
    console.log('Carrito actual: ', req.session.carrito);

    res.redirect('/Pagina-Principal');
});

module.exports = router;