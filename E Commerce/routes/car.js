const express = require('express');
const db = require('../db');
const nodemailer = require('nodemailer');
const router = express.Router();

require('dotenv').config();

const transporter = nodemailer.createTransport({
    host: "smtp.gmail.com",
    port: process.env.MAIL_PORT,
    secure: false,
    auth: {
        user: process.env.MAIL_USER, //escribir una direccion de correo
        pass: process.env.MAIL_PASS //se debe de colocar una contraseña de aplicacion (en gmail -> Administrar tu cuenta de Google -> seguridad -> contraseña de aplicaciones)
    }
});

router.get('/', (req, res) => {
    const carrito = req.session.carrito || [];
    const total = carrito.reduce((acc, item) => acc + item.precio * item.cantidad, 0);
    const correoUsuario = req.session?.usuario?.correo;

    const sqlMetodosPago = 'SELECT tipoTarjeta FROM usuariosMetodosPago WHERE correoElectronico = ?';
    db.query(sqlMetodosPago, [correoUsuario], (err, results) => {
        if(err) {
            console.log(err);
            return res.render('./Car', {carrito, total, metodosPago: []});
        }

        const metodosPago = results.map(r => r.tipoTarjeta). filter(tipo => tipo && tipo.trim() !== '');
        res.render('./Car', {carrito, total, metodosPago});
    });
});

router.post('/agregarCarrito', (req, res) => {
    const { idProducto, nombre, precio, cantidad, imagen, paginaOrigen } = req.body;

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
            cantidad: parseInt(cantidad) || 1,
            imagen
        });
    }

    const redirectUrl = paginaOrigen ? `${paginaOrigen}${paginaOrigen.includes('?') ? '&' : '?'}added=true` : '/Pagina-Principal?added=true';
    res.redirect(redirectUrl);
});

router.post('/eliminar', (req, res) => {
    const { idProducto } = req.body;
    if(req.session.carrito) {
        req.session.carrito = req.session.carrito.filter(p => p.idProducto !== parseInt(idProducto));
    }
    res.redirect('/Carrito-de-Compras');
});

router.post('/finalizar-compra',  (req, res) => {
    const correo = req.session?.usuario?.correo;
    const carrito = req.session?.carrito || [];

    if(!correo || carrito.length === 0) {
        return res.redirect("/Carrito-de-Compras");
    }

    const total = carrito.reduce((acc, item) => acc + item.precio * item.cantidad, 0);

    const sqlRegistrarDatos = 'CALL datosDomicilio(?, ?, ?)';
    const { direccion, telefono } = req.body;
    db.query(sqlRegistrarDatos, [correo, direccion, telefono], (err) => {
        if(err) {
            console.log(err);
        }
    })
    
    const sqlCrearPedido = 'CALL crearPedido(?, ?, @nuevoPedido)';
    db.query(sqlCrearPedido, [correo, total], (err) => {
        if(err) {
            console.log(err);
            return res.render('./Car', { carrito, error: 'Error al crear pedido' });
        }

        db.query('SELECT @nuevoPedido AS idPedido', (err, result) => {
            const idPedido = result[0]?.idPedido;

            if(!idPedido) {
                console.log("error al obtener nuevoPedido");
                return res.render('./Car', { carrito, error: 'No se pudo obtener el pedido.' });
            }

            carrito.forEach(item => {
                const sqlDetallesPedido = 'CALL agregarDetallesPedido(?, ?, ?, ?)';
                const subtotal = item.precio * item.cantidad;

                db.query(sqlDetallesPedido, [idPedido, item.idProducto, item.cantidad, subtotal], (err) => {
                    if(err) {
                        console.log(err)
                    }
                });
            });

            const itemsHtml = carrito.map(item => `
                <tr>
                    <td>${item.nombre}</td>
                    <td>${item.cantidad}</td>
                    <td>$${item.precio.toFixed(2)}</td>
                    <td>$${(item.precio * item.cantidad).toFixed(2)}</td>
                </tr>
            `).join('');
            
            const mailOptions = {
                from: process.env.MAIL_USER, //escribir una direccion de correo
                to: correo,
                subject: '¡Muchas gracias por tu compra!',
                html: `
                    <h2>Gracias por tu reciente compra</h2>
                    <p>Tu pedido con el número <b>${idPedido} ha sido registrado correctamente</b></p>
                    <table border="1" cellpadding="5" cellspacing="0">
                        <tr>
                            <th>Producto</th>
                            <th>Cantidad</th>
                            <th>Precio</th>
                            <th>Subtotal</th>
                        </tr>
                        ${itemsHtml}
                    </table>
                    <h3>Total: $${total.toFixed(2)} MXN</h3>
                    <p>Recibirás otra notificación cuando tu pedido sea enviado.</p>
                `
            };

            transporter.sendMail(mailOptions, (error, info) => {
                if (error) {
                    console.error('Error al enviar correo:', error);
                } else {
                    console.log('Correo enviado:', info.response);
                }
            });

            req.session.carrito = [];

            res.redirect('/Carrito-de-Compras');
        });
    });
});

module.exports = router;