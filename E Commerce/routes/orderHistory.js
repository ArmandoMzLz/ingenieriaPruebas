const express = require('express');
const db = require('../db');
const router = express.Router();

router.get('/', (req, res) => {
    const correoUsuario = req.session?.usuario?.correo;

    if(!correoUsuario) {
        return res.redirect('/');
    }

    const sqlQuery = 'CALL historialPedidos(?)';

    db.query(sqlQuery, [correoUsuario], (err, results) => {
        if(err) {
            console.log(err);
            return res.render('./OrderHistory', {pedidos: [], error: 'Algo salio mal. Intentelo más tarde.'});
        }

        const pedidos = results[0];
        res.render('./OrderHistory', {pedidos, error: null});
    });
});

module.exports = router;