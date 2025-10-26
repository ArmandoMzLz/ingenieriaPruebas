const express = require('express');
const db = require('../db');
const router = express.Router();

router.get('/', (req, res) => {
    const correoUsuario = req.session?.usuario?.correo;

    if(!correoUsuario) {
        return res.redirect('/');
    }
    
    res.render('./PaymentMethod', {exito: null, error: null});
});

router.post('/', (req, res) => {
    const correo = req.session?.usuario?.correo;
    const { tipoTarjeta, numeroTarjeta, titular, vencimiento, cvv } = req.body;

    const fechaVencimiento = `${vencimiento}-01`;

    const fechaExpiracion = new Date(fechaVencimiento);
    const fechaActual = new Date();
    fechaActual.setDate(1);

    if(fechaExpiracion < fechaActual) {
        return res.render('./PaymentMethod', { exito: null, error: 'La tarjeta ya está vencida. Por favor, ingrese una tarjeta válida.' });
    }

    const sqlQuery = 'CALL datosTarjeta(?, ?, ?, ?, ?, ?)';
    db.query(sqlQuery, [correo, tipoTarjeta, numeroTarjeta, titular, fechaVencimiento, cvv], (err, results) => {
        if(err) {
            console.log(err);
            return res.render('./PaymentMethod', { exito: null, error: 'Algo salio mal. Intentelo más tarde.' });
        }

        res.render('./PaymentMethod', { exito: 'Metodo de pago registrado correctamente.', error: null });
    });
});

module.exports = router;