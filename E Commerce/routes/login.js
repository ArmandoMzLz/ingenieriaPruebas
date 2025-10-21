const express = require('express');
const db = require('../db');
const router = express.Router();

router.get('/', (req, res) => {
    res.render('./Login');
});

router.post('/', (req, res) => {
    const { correo, contrasena} = req.body;
    const sqlQuery = 'CALL loginUsuario(?, ?)';

    db.query(sqlQuery, [correo, contrasena], (err, results) => {
        console.log("Ingreso al callback (Login)");
        if(err) {
            console.log(err);
            return res.render('./Login', { error: 'Algo salio mal. Intentelo más tarde'});
        }
        
        const usuario = results[0][0];

        if(!usuario) {
            console.log(err);
            return res.render('./Login', { error: 'Correo o contraseña incorrectos'});
        }

        req.session.usuario = {
            correo: correo
        }

        res.redirect('/Pagina-Principal');
    });
});

module.exports = router;