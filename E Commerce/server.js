const express = require('express');
const session = require('express-session');
var path = require('path');

const PORT = 3000;
const app = express();

const login = require("./routes/login");
const register = require("./routes/register");
const home = require("./routes/home");
const detail = require("./routes/detail");
const paymentMethod = require("./routes/paymentMethod");
const car = require("./routes/car");
const orderHistory = require("./routes/orderHistory");

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));
app.use(express.static(path.join(__dirname, 'public')));
app.use(express.urlencoded({ extended: true }));

app.use(session({
    secret: '123',
    resave: false,
    saveUninitialized: false
}));

app.use('/', login);
app.use('/Registro', register);
app.use('/Pagina-Principal', home);
app.use('/Detalles', detail);
app.use('/Metodos-de-Pago', paymentMethod);
app.use('/Carrito-de-Compras', car);
app.use('/Historial-de-Pedidos', orderHistory);

app.use((req, res) => {
    res.status(404).render('error', { titulo: 'Pagina no encontrada'});
});

app.listen(PORT, () => console.log('Servidor activo en: http://localhost:' + PORT));