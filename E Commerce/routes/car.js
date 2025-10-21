const express = require('express');
const db = require('../db');
const nodemailer = require('nodemailer');
const router = express.Router();

const transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'tuCorreoVaAqui@gmail.com',
        pass: 'contrasena' //se debe de colocar una contrasenña de aplicacion (en gmail -> Administrar tu cuenta de Google -> seguridad -> contraseña de aplicaciones)
    }
})

router.get('/', (req, res) => {
    const carrito = req.session.carrito || [];
    const total = carrito.reduce((acc, item) => acc + item.precio * item.cantidad, 0);
    res.render('./Car', {carrito, total});
});

router.post('/eliminar', (req, res) => {
    const { idProducto } = req.body;
    if(req.session.carrito) {
        req.session.carrito = req.session.carrito.filter(p => p.idProducto !== parseInt(idProducto));
    }
    res.redirect('/Carrito-de-Compras');
});

router.post('/finalizar-compra', (req, res) => {
    const correo = req.session?.usuario?.correo;
    const carrito = req.session?.carrito || [];

    if(!correo || carrito.length === 0) {
        return res.redirect("/Carrito-de-Compras");
    }

    const total = carrito.reduce((acc, item) => acc + item.precio * item.cantidad, 0);
    
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
                from: 'tuCorreoVaAqui@gmail.com',
                to: correo,
                subject: 'Confirmación de Compra',
                html: `
                    <h2>¡Muchas gracias por tu compra!</h2>
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
            
            console.log("Correo enviado");
            req.session.carrito = [];

            res.redirect('/Carrito-de-Compras');
        });
    });
});

module.exports = router;