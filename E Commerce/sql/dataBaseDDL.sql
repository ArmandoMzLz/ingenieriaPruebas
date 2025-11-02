CREATE DATABASE eCommerce;
USE eCommerce;

CREATE TABLE usuariosLogin(
correoElectronico VARCHAR(50) NOT NULL,
usuarioContrasena VARCHAR(50) NOT NULL,
PRIMARY KEY(correoElectronico)
);

CREATE TABLE usuariosDatos(
correoElectronico VARCHAR(50) NOT NULL,
nombreUsuario VARCHAR(30) NOT NULL,
apellidoUsuario VARCHAR(30) NOT NULL,
domicilioUsuario VARCHAR(100),
telefonoUsuario VARCHAR(10),
PRIMARY KEY(correoElectronico),
FOREIGN KEY(correoElectronico) 
	REFERENCES usuariosLogin(correoElectronico)
);

CREATE TABLE usuariosMetodosPago(
correoElectronico VARCHAR(50) NOT NULL,
tipoTarjeta VARCHAR(20),
numeroTarjeta VARCHAR(20),
nombreTitular VARCHAR(50),
fechaVencimiento DATE,
cvv INT(3),
PRIMARY KEY(correoElectronico),
FOREIGN KEY(correoElectronico)
	REFERENCES usuariosLogin(correoElectronico)
);

CREATE TABLE productos(
idProducto INT AUTO_INCREMENT,
nombreProducto VARCHAR(150) NOT NULL,
imagenProductoRuta VARCHAR(50) NOT NULL,
marcaProducto VARCHAR(30) NOT NULL,
precioProducto DECIMAL(10, 2) NOT NULL,
cantidadProducto INT NOT NULL,
descripcionProducto VARCHAR(300) NOT NULL,
categoriaProducto VARCHAR(30) NOT NULL,
PRIMARY KEY(idProducto)
);

CREATE TABLE pedidos(
idPedido INT AUTO_INCREMENT NOT NULL,
correoElectronico VARCHAR(50) NOT NULL,
fechaPedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
totalPedido DECIMAL(10, 2) NOT NULL,
PRIMARY KEY(idPedido),
FOREIGN KEY(correoElectronico)
	REFERENCES usuariosLogin(correoElectronico)
);

CREATE TABLE detallesPedido(
idPedidoDetalles INT AUTO_INCREMENT NOT NULL,
idPedido INT NOT NULL,
idProducto INT NOT NULL,
cantidad INT NOT NULL,
subtotal DECIMAL(10, 2) NOT NULL,
PRIMARY KEY (idPedidoDetalles),
FOREIGN KEY(idPedido)
	REFERENCES pedidos(idPedido),
FOREIGN KEY(idProducto)
	REFERENCES productos(idProducto)
);