const express = require('express');
const db = require('../db');
const router = express.Router();

router.get('/', (req, res) => {
    res.render('./PaymentMethod');
});

/*
router.post('/metodo-pago', (req, res) => {
    const { correo = req.session?.usuario?.correo, tipoTarjeta, numeroTarjeta, titular, vencimiento, cvv } = req.body;
    const sqlQuery = 'CALL datosTarjeta(?, ?, ?, ?, ?, ?)';

    db.query(sqlQuery, [correo, tipoTarjeta, numeroTarjeta, titular, vencimiento, cvv], (err, results) => {
        console.log('Ingreso al callback (Metodos de pago)');

        if(err) {
            console.log(err);
            return res.render('./PaymentMethod');
        }

        res.redirect('/Pagina-Principal');
    });
});
*/
module.exports = router;