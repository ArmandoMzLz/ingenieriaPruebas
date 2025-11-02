const mysql = require('mysql2');
const fs = require('fs');
require('dotenv').config();

const connection = mysql.createConnection({
    host: process.env.DB_HOST,
    port: process.env.DB_PORT ,
    user: process.env.DB_USER,
    password: process.env.DB_PASS,
    database: process.env.DB_NAME,
    ssl: process.env.DB_SSL === 'true' ? {
        ca: fs.readFileSync(process.env.DB_CA)
    }: false
});

connection.connect(err => {
    if(err) {
        console.log("Error " + err);
        return;
    } else {
        console.log("Exito");
    }
});

module.exports = connection;