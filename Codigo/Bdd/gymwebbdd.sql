DROP DATABASE IF EXISTS gymweb;
CREATE DATABASE gymweb;
USE gymweb;

-------------------------------------------------------
-- USUARIO
-------------------------------------------------------
CREATE TABLE usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  estado TINYINT,
  fecha_alta DATETIME DEFAULT CURRENT_TIMESTAMP,
  rol TINYINT,
  PRIMARY KEY (id_usuario)
);

-------------------------------------------------------
-- PERFIL USUARIO
-------------------------------------------------------
CREATE TABLE perfilusuario (
  id_perfil INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  descripcion VARCHAR(255),
  foto_url VARCHAR(255),
  telefono VARCHAR(255),
  PRIMARY KEY (id_perfil),
  UNIQUE KEY (usuario_id),
  FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario)
);

-------------------------------------------------------
-- PREFERENCIA DE NOTIFICACIÓN
-------------------------------------------------------
CREATE TABLE preferencianotificacion (
  id_pref INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  silencioso TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (id_pref),
  UNIQUE KEY (usuario_id),
  FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario)
);

-------------------------------------------------------
-- CLASE
-------------------------------------------------------
CREATE TABLE clase (
  id_clase INT NOT NULL AUTO_INCREMENT,
  titulo VARCHAR(255),
  descripcion VARCHAR(255),
  cupo INT DEFAULT 20,
  creador_id INT,
  PRIMARY KEY (id_clase),
  FOREIGN KEY (creador_id) REFERENCES usuario(id_usuario)
);

-------------------------------------------------------
-- USUARIO X CLASE
-------------------------------------------------------
CREATE TABLE usuarioxclase (
  id_usu_clase INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  clase_id INT NOT NULL,
  aprobado TINYINT(1) DEFAULT 0,
  PRIMARY KEY (id_usu_clase),
  FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario),
  FOREIGN KEY (clase_id) REFERENCES clase(id_clase)
);

-------------------------------------------------------
-- INVITACIONES A CLASE
-------------------------------------------------------
CREATE TABLE invitacionclase (
  id_invitacion INT NOT NULL AUTO_INCREMENT,
  usu_clase_id INT NOT NULL,
  estado ENUM('pendiente','aceptada','rechazada') DEFAULT 'pendiente',
  fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_invitacion),
  FOREIGN KEY (usu_clase_id) REFERENCES usuarioxclase(id_usu_clase)
);

-------------------------------------------------------
-- MENSAJES EN CLASE
-------------------------------------------------------
CREATE TABLE mensajeclase (
  id_mensaje INT NOT NULL AUTO_INCREMENT,
  clase_id INT NOT NULL,
  fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
  contenido VARCHAR(500),
  PRIMARY KEY (id_mensaje),
  FOREIGN KEY (clase_id) REFERENCES clase(id_clase)
);

-------------------------------------------------------
-- NOTIFICACIONES
-------------------------------------------------------
CREATE TABLE notificacion (
  id_notificacion INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  mensaje LONGTEXT,
  fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
  leida TINYINT(1) DEFAULT 0,
  PRIMARY KEY (id_notificacion),
  FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario)
);

-------------------------------------------------------
-- PLAN
-------------------------------------------------------
CREATE TABLE plan (
  id_plan INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  precio DECIMAL(10,2),
  periodo VARCHAR(255),
  PRIMARY KEY (id_plan)
);

-------------------------------------------------------
-- MEMBRESÍA
-------------------------------------------------------
CREATE TABLE membresia (
  id_membresia INT NOT NULL AUTO_INCREMENT,
  usuario_id INT NOT NULL,
  plan_id INT NOT NULL,
  estado TINYINT,
  fecha_inicio DATETIME(6),
  fecha_fin DATETIME(6),
  PRIMARY KEY (id_membresia),
  FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario),
  FOREIGN KEY (plan_id) REFERENCES plan(id_plan)
);

-------------------------------------------------------
-- PAGOS
-------------------------------------------------------
CREATE TABLE pago (
  id_pago INT NOT NULL AUTO_INCREMENT,
  membresia_id INT NOT NULL,
  fecha DATETIME(6),
  monto DECIMAL(10,2),
  estado ENUM('pendiente','completado','fallido') DEFAULT 'pendiente',
  comprobante_url VARCHAR(255),
  PRIMARY KEY (id_pago),
  FOREIGN KEY (membresia_id) REFERENCES membresia(id_membresia)
);

-------------------------------------------------------
-- RUTINA
-------------------------------------------------------
CREATE TABLE rutina (
  id_rutina INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  creador_id INT,
  PRIMARY KEY (id_rutina),
  FOREIGN KEY (creador_id) REFERENCES usuario(id_usuario)
);

-------------------------------------------------------
-- RUTINA DETALLE
-------------------------------------------------------
CREATE TABLE rutinadetalle (
  id_detalle INT NOT NULL AUTO_INCREMENT,
  rutina_id INT NOT NULL,
  descripcion VARCHAR(255),
  imagen VARCHAR(255),
  descanso_seg INT NOT NULL,
  PRIMARY KEY (id_detalle),
  UNIQUE KEY (rutina_id),
  FOREIGN KEY (rutina_id) REFERENCES rutina(id_rutina)
);

-------------------------------------------------------
-- EJERCICIO
-------------------------------------------------------
CREATE TABLE ejercicio (
  id_ejercicio INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255),
  grupo_muscular VARCHAR(255),
  equipamiento VARCHAR(255),
  estado_contenido ENUM('activo','inactivo') DEFAULT 'activo',
  creado_por INT,
  es_global TINYINT(1) DEFAULT 0,
  PRIMARY KEY (id_ejercicio),
  FOREIGN KEY (creado_por) REFERENCES usuario(id_usuario)
);

-------------------------------------------------------
-- EJERCICIO DETALLE
-------------------------------------------------------
CREATE TABLE ejerciciodetalle (
  id_ejec_det INT NOT NULL AUTO_INCREMENT,
  rutina_detalle_id INT NOT NULL,
  ejercicio_id INT NOT NULL,
  descanso_seg INT DEFAULT 60,
  orden INT DEFAULT 1,
  PRIMARY KEY (id_ejec_det),
  FOREIGN KEY (rutina_detalle_id) REFERENCES rutinadetalle(id_detalle),
  FOREIGN KEY (ejercicio_id) REFERENCES ejercicio(id_ejercicio)
);

-------------------------------------------------------
-- SERIES
-------------------------------------------------------
CREATE TABLE serie (
  id_serie INT NOT NULL AUTO_INCREMENT,
  ejercicioDetalle_id INT NOT NULL,
  carga VARCHAR(255),
  repeticiones INT,
  PRIMARY KEY (id_serie),
  FOREIGN KEY (ejercicioDetalle_id) REFERENCES ejerciciodetalle(id_ejec_det)
);

-------------------------------------------------------
-- INSERTAR USUARIOS
-- Los passwords están en bcrypt:
--  password real = "123456"
-------------------------------------------------------

INSERT INTO usuario (nombre, email, password_hash, estado, rol)
VALUES
('Administrador del Sistema', 'admin@gym.com',
 '$2a$10$EBQ0L4bnB93k6Bgd35ci0eQzTJfzQjK7vVbXcX5HjuJpGd8PvXlke', 
 1, 1),

('Profesor de Entrenamiento', 'profesor@gym.com',
 '$2a$10$EBQ0L4bnB93k6Bgd35ci0eQzTJfzQjK7vVbXcX5HjuJpGd8PvXlke',
 1, 2),

('Juan Pérez', 'juanperez@gmail.com',
 '$2a$10$EBQ0L4bnB93k6Bgd35ci0eQzTJfzQjK7vVbXcX5HjuJpGd8PvXlke',
 1, 3);

-------------------------------------------------------
-- INSERTAR PERFILES (usuario_id = id_usuario generado)
-- IMPORTANTE: La FK usuario_id debe existir en "usuario"
-------------------------------------------------------

-- Perfil del admin
INSERT INTO perfilusuario (usuario_id, descripcion, foto_url, telefono)
VALUES
((SELECT id_usuario FROM usuario WHERE email='admin@gym.com'),
 'Administrador general del sistema', 
 NULL,
 '111-222-333');

-- Perfil del profesor
INSERT INTO perfilusuario (usuario_id, descripcion, foto_url, telefono)
VALUES
((SELECT id_usuario FROM usuario WHERE email='profesor@gym.com'),
 'Entrenador certificado de musculación',
 NULL,
 '222-333-444');

-- Perfil del usuario Juan Pérez
INSERT INTO perfilusuario (usuario_id, descripcion, foto_url, telefono)
VALUES
((SELECT id_usuario FROM usuario WHERE email='juanperez@gmail.com'),
 'Socio activo con plan mensual',
 NULL,
 '333-444-555');
