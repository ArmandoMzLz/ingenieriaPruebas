const mysql = require('mysql2');

const connection = mysql.createConnection({
    host: "localhost",
    database: "eCommerce",
    user: "root",
    password: "admin",
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