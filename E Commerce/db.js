const mysql = require('mysql2');

require('dotenv').config();

const connection = mysql.createConnection({
    host: process.env.DB_HOST, //Reemplazar por el nombre del servidor (ej. localhost)
    database: process.env.DB_NAME, //Reemplazar por el nombre de la base de datos
    user: process.env.DB_USER, //Reemplazar por el usuario de la base de datos (ej. root)
    password: process.env.DB_PASS, //Reemplazar por la contraseña del usuario de la base de datos
})

connection.connect(err => {
    if(err) {
        console.log("Error " + err);
        return;
    } else {
        console.log("Exito");
    }
});

module.exports = connection;