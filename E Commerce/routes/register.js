const express = require('express');
const db = require('../db');
const router = express.Router();

router.get('/', (req, res) => {
    res.render('./Register');
});

router.post('/', (req, res) => {
    const { correo, contrasena, nombre, apellido } = req.body;
    const sqlQuery = 'CALL registrarUsuario(?, ?, ?, ?)';

    db.query(sqlQuery, [correo, contrasena, nombre, apellido], (err, results) => {
        console.log("Ingreso al callback (Registro)");

        if(err) {
            console.log(err);
            return res.render('./Register', { error: 'Algo salio mal. Intentelo más tarde'});
        }

        res.redirect('/');
    });
});

module.exports = router;