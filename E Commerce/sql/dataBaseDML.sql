DELIMITER //
CREATE PROCEDURE loginUsuario (
	IN correo VARCHAR(50),
	IN contrasena VARCHAR(50))
BEGIN
	SELECT * FROM usuariosLogin 
		WHERE correoElectronico = correo AND usuarioContrasena = contrasena; 
END//
DELIMITER;

DELIMITER //
CREATE PROCEDURE registrarUsuario (
	IN correo VARCHAR(50),
    IN contrasena VARCHAR(50),
    IN nombre VARCHAR(30),
    IN apellido VARCHAR(30))
BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    
    BEGIN
		ROLLBACK;
	END;
    
    START TRANSACTION;
	INSERT INTO usuariosLogin (correoElectronico, usuarioContrasena)
		VALUES (correo, contrasena);
	INSERT INTO usuariosDatos (correoElectronico, nombreUsuario, apellidoUsuario)
		VALUES (correo, nombre, apellido);
	INSERT INTO usuariosMetodosPago (correoElectronico)
		VALUES (correo);
	COMMIT;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE datosTarjeta (
	IN correo VARCHAR(50),
    IN tipoTar VARCHAR(20),
    IN numeroTar VARCHAR(20),
    IN titular VARCHAR(50),
    IN vencimiento DATE,
    IN cvvTar VARCHAR(3))
BEGIN
	UPDATE usuariosMetodosPago
    SET
		tipoTarjeta = tipoTar,
        numeroTarjeta = numeroTar,
        nombreTitular = titular,
        fechaVencimiento = vencimiento,
		cvv = cvvTar
	WHERE correoElectronico = correo;
END //
DELIMITER ;

DELIMITER //
CREATE PROCEDURE datosDomicilio (
	IN correo VARCHAR(50),
    IN domicilio VARCHAR(100),
    IN telefono VARCHAR(10))
BEGIN
	UPDATE usuariosDatos
    SET
		domicilioUsuario = domicilio,
        telefonoUsuario = telefono
	WHERE correoElectronico = correo;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE mostrarProductos (
	IN termino VARCHAR(50))
BEGIN
	IF termino IS NULL OR termino = '' THEN
		SELECT 
			idProducto,
			imagenProductoRuta,
			nombreProducto,
			descripcionProducto,
			precioProducto
		FROM productos;
	ELSE 
		SELECT 
			idProducto,
			imagenProductoRuta,
			nombreProducto,
			descripcionProducto,
			precioProducto
		FROM productos
		WHERE nombreProducto LIKE CONCAT('%', termino, '%') OR categoriaProducto LIKE CONCAT('%', termino, '%') OR marcaProducto LIKE CONCAT('%', termino, '%');
	END IF;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE mostrarProductosCategoria (
	IN categoria VARCHAR(50))
BEGIN
	SELECT 
		idProducto,
		imagenProductoRuta,
		nombreProducto,
		descripcionProducto,
		precioProducto
	FROM productos
	WHERE categoriaProducto = categoria;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE obtenerProductoID(
	IN productoID INT)
BEGIN
	SELECT idProducto, nombreProducto, imagenProductoRuta, marcaProducto, precioProducto, descripcionProducto, categoriaProducto
    FROM productos
    WHERE idProducto = productoID;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE obtenerProductoCategoria(
	IN productoCategoria VARCHAR(30))
BEGIN
	SELECT idProducto, nombreProducto, imagenProductoRuta, marcaProducto, precioProducto, descripcionProducto, categoriaProducto
    FROM productos
    WHERE categoriaProducto = productoCategoria;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE historialPedidos (
	IN correoUsuario VARCHAR(50))
BEGIN
	SELECT
		p.idPedido,
        p.fechaPedido,
        p.totalPedido,
        pr.nombreProducto,
        pr.imagenProductoRuta,
        dp.cantidad,
        dp.subtotal
	FROM pedidos p
    INNER JOIN detallesPedido dp ON p.idPedido = dp.idPedido
    INNER JOIN productos pr ON dp.idProducto = pr.idProducto
    WHERE p.correoElectronico = correoUsuario
    ORDER BY p.fechaPedido DESC;
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE crearPedido (
	IN correo VARCHAR(50),
	IN total DECIMAL(10, 2),
	OUT nuevoPedido INT)
BEGIN
	INSERT INTO pedidos (correoElectronico, totalPedido)
    VALUES(correo, total);
    SET nuevoPedido = LAST_INSERT_ID();
END//
DELIMITER ;

DELIMITER //
CREATE PROCEDURE agregarDetallesPedido (
	IN idPedido INT, 
	IN idProducto INT, 
	IN cantidad INT, 
	IN subtotal DECIMAL(10, 2))
BEGIN
	INSERT INTO detallesPedido (idPedido, idProducto, cantidad, subtotal)
    VALUES(idPedido, idProducto, cantidad, subtotal);
END//
DELIMITER ;