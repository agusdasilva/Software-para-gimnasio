CREATE DATABASE IF NOT EXISTS gymweb;
use gymweb;
CREATE TABLE Usuario (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  estado ENUM('activo','inactivo') DEFAULT 'activo',
  fecha_alta DATETIME DEFAULT CURRENT_TIMESTAMP,
  rol ENUM('admin','entrenador','cliente') DEFAULT 'cliente'
);

CREATE TABLE PerfilUsuario (
  id_perfil INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  descripcion TEXT,
  foto_url VARCHAR(255),
  telefono VARCHAR(30),
  UNIQUE (usuario_id),
  FOREIGN KEY (usuario_id) REFERENCES Usuario(id_usuario)
);

CREATE TABLE PreferenciaNotificacion (
  id_pref INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL UNIQUE,
  silencioso BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (usuario_id) REFERENCES Usuario(id_usuario)
);

ALTER TABLE PreferenciaNotificacion
DROP FOREIGN KEY PreferenciaNotificacion_ibfk_1;

ALTER TABLE PreferenciaNotificacion
ADD FOREIGN KEY (usuario_id) REFERENCES Notificacion(id_notificacion);


CREATE TABLE Notificacion (
  id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  mensaje TEXT NOT NULL,
  fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
  leida BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (usuario_id) REFERENCES Usuario(id_usuario)
);

CREATE TABLE Plan (
  id_plan INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  precio DECIMAL(10,2) NOT NULL,
  periodo VARCHAR(50) NOT NULL
);

CREATE TABLE Membresia (
  id_membresia INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  plan_id INT NOT NULL,
  estado ENUM('activa','vencida','pendiente') DEFAULT 'pendiente',
  fecha_inicio DATE,
  fecha_fin DATE,
  FOREIGN KEY (usuario_id) REFERENCES Usuario(id_usuario),
  FOREIGN KEY (plan_id) REFERENCES Plan(id_plan)
);

CREATE TABLE Pago (
  id_pago INT AUTO_INCREMENT PRIMARY KEY,
  membresia_id INT NOT NULL,
  fecha DATE DEFAULT (CURRENT_DATE),
  monto DECIMAL(10,2) NOT NULL,
  estado ENUM('pendiente','completado','fallido') DEFAULT 'pendiente',
  comprobante_url VARCHAR(255),
  FOREIGN KEY (membresia_id) REFERENCES Membresia(id_membresia)
);

CREATE TABLE Ejercicio (
  id_ejercicio INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  grupo_muscular VARCHAR(100),
  equipamiento VARCHAR(100),
  estado_contenido ENUM('activo','inactivo') DEFAULT 'activo',
  creado_por INT,
  es_global BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (creado_por) REFERENCES Usuario(id_usuario)
);

CREATE TABLE Rutina (
  id_rutina INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  creador_id INT,
  FOREIGN KEY (creador_id) REFERENCES Usuario(id_usuario)
);

CREATE TABLE EjercicioXRutina (
  id_ejecucion INT AUTO_INCREMENT PRIMARY KEY,
  ejercicio_id INT NOT NULL,
  rutina_id INT NOT NULL,
  fecha DATE,
  FOREIGN KEY (ejercicio_id) REFERENCES Ejercicio(id_ejercicio),
  FOREIGN KEY (rutina_id) REFERENCES Rutina(id_rutina)
);

CREATE TABLE RutinaDetalle (
  id_detalle INT AUTO_INCREMENT PRIMARY KEY,
  rutina_id INT NOT NULL,
  descripcion TEXT,
  imagen VARCHAR(255),
  FOREIGN KEY (rutina_id) REFERENCES Rutina(id_rutina)
);

CREATE TABLE EjercicioDetalle (
  id_ejec_det INT AUTO_INCREMENT PRIMARY KEY,
  rutinaDetalle_id INT NOT NULL,
  ejercicio_id INT NOT NULL,
  descanso_seg INT,
  FOREIGN KEY (rutinaDetalle_id) REFERENCES RutinaDetalle(id_detalle)
);

CREATE TABLE Serie (
  id_serie INT AUTO_INCREMENT PRIMARY KEY,
  ejercicioDetalle_id INT NOT NULL,
  carga VARCHAR(50),
  FOREIGN KEY (ejercicioDetalle_id) REFERENCES EjercicioDetalle(id_ejec_det)
);

CREATE TABLE Clase (
  id_clase INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(100) NOT NULL,
  descripcion TEXT
);

CREATE TABLE ClaseXRutina (
  id_clase_rutina INT AUTO_INCREMENT PRIMARY KEY,
  clase_id INT NOT NULL,
  rutina_id INT NOT NULL,
  FOREIGN KEY (clase_id) REFERENCES Clase(id_clase),
  FOREIGN KEY (rutina_id) REFERENCES Rutina(id_rutina)
);

CREATE TABLE MensajeClase (
  id_mensaje INT AUTO_INCREMENT PRIMARY KEY,
  clase_id INT NOT NULL,
  fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
  contenido TEXT,
  FOREIGN KEY (clase_id) REFERENCES Clase(id_clase)
);

CREATE TABLE UsuarioXClase (
  id_usu_clase INT AUTO_INCREMENT PRIMARY KEY,
  usuario_id INT NOT NULL,
  clase_id INT NOT NULL,
  FOREIGN KEY (usuario_id) REFERENCES Usuario(id_usuario),
  FOREIGN KEY (clase_id) REFERENCES Clase(id_clase)
);

CREATE TABLE InvitacionClase (
  id_invitacion INT AUTO_INCREMENT PRIMARY KEY,
  usu_clase_id INT NOT NULL,
  estado ENUM('pendiente','aceptada','rechazada') DEFAULT 'pendiente',
  fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (usu_clase_id) REFERENCES UsuarioXClase(id_usu_clase)
);
