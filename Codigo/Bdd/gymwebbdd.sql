CREATE DATABASE IF NOT EXISTS gymweb;
use gymweb;
CREATE TABLE `clase` (
  `id_clase` int NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) DEFAULT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `cupo` int DEFAULT '20',
  `creador_id` int DEFAULT NULL,
  PRIMARY KEY (`id_clase`),
  KEY `fk_clase_creador` (`creador_id`),
  CONSTRAINT `fk_clase_creador` FOREIGN KEY (`creador_id`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `clasexrutina` (
  `id_clase_rutina` int NOT NULL AUTO_INCREMENT,
  `clase_id` int NOT NULL,
  `rutina_id` int NOT NULL,
  PRIMARY KEY (`id_clase_rutina`),
  KEY `clase_id` (`clase_id`),
  KEY `rutina_id` (`rutina_id`),
  CONSTRAINT `clasexrutina_ibfk_1` FOREIGN KEY (`clase_id`) REFERENCES `clase` (`id_clase`),
  CONSTRAINT `clasexrutina_ibfk_2` FOREIGN KEY (`rutina_id`) REFERENCES `rutina` (`id_rutina`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `ejercicio` (
  `id_ejercicio` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `grupo_muscular` varchar(255) DEFAULT NULL,
  `equipamiento` varchar(255) DEFAULT NULL,
  `estado_contenido` enum('activo','inactivo') DEFAULT 'activo',
  `creado_por` int DEFAULT NULL,
  `es_global` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_ejercicio`),
  KEY `creado_por` (`creado_por`),
  CONSTRAINT `ejercicio_ibfk_1` FOREIGN KEY (`creado_por`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `ejerciciodetalle` (
  `id_ejec_det` int NOT NULL AUTO_INCREMENT,
  `rutinaDetalle_id` int NOT NULL,
  `ejercicio_id` int NOT NULL,
  `descanso_seg` int NOT NULL DEFAULT '60',
  `orden` int NOT NULL DEFAULT '1',
  `rutina_detalle_id` int DEFAULT NULL,
  PRIMARY KEY (`id_ejec_det`),
  KEY `rutinaDetalle_id` (`rutinaDetalle_id`),
  KEY `ejerciciodetalle_ibfk_2` (`ejercicio_id`),
  KEY `FKdbt671uxjva16yu0hl1kqqdus` (`rutina_detalle_id`),
  CONSTRAINT `ejerciciodetalle_ibfk_1` FOREIGN KEY (`rutinaDetalle_id`) REFERENCES `rutinadetalle` (`id_detalle`),
  CONSTRAINT `ejerciciodetalle_ibfk_2` FOREIGN KEY (`ejercicio_id`) REFERENCES `ejercicio` (`id_ejercicio`),
  CONSTRAINT `FKdbt671uxjva16yu0hl1kqqdus` FOREIGN KEY (`rutina_detalle_id`) REFERENCES `rutinadetalle` (`id_detalle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `invitacion_clase` (
  `id_invitacion` int NOT NULL AUTO_INCREMENT,
  `estado` tinyint DEFAULT NULL,
  `fecha` datetime(6) DEFAULT NULL,
  `usu_clase_id` int DEFAULT NULL,
  PRIMARY KEY (`id_invitacion`),
  KEY `FK3w08ds1euv7d0wqmtp76ie5un` (`usu_clase_id`),
  CONSTRAINT `FK3w08ds1euv7d0wqmtp76ie5un` FOREIGN KEY (`usu_clase_id`) REFERENCES `usuarioxclase` (`id_usu_clase`),
  CONSTRAINT `invitacion_clase_chk_1` CHECK ((`estado` between 0 and 2))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `invitacionclase` (
  `id_invitacion` int NOT NULL AUTO_INCREMENT,
  `usu_clase_id` int NOT NULL,
  `estado` enum('pendiente','aceptada','rechazada') DEFAULT 'pendiente',
  `fecha` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_invitacion`),
  KEY `usu_clase_id` (`usu_clase_id`),
  CONSTRAINT `invitacionclase_ibfk_1` FOREIGN KEY (`usu_clase_id`) REFERENCES `usuarioxclase` (`id_usu_clase`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `membresia` (
  `id_membresia` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `plan_id` int NOT NULL,
  `estado` tinyint DEFAULT NULL,
  `fecha_inicio` datetime(6) DEFAULT NULL,
  `fecha_fin` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id_membresia`),
  KEY `usuario_id` (`usuario_id`),
  KEY `plan_id` (`plan_id`),
  CONSTRAINT `membresia_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `membresia_ibfk_2` FOREIGN KEY (`plan_id`) REFERENCES `plan` (`id_plan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `mensajeclase` (
  `id_mensaje` int NOT NULL AUTO_INCREMENT,
  `clase_id` int NOT NULL,
  `fecha` datetime DEFAULT CURRENT_TIMESTAMP,
  `contenido` varchar(500) DEFAULT NULL,
  `mensaje` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_mensaje`),
  KEY `clase_id` (`clase_id`),
  CONSTRAINT `mensajeclase_ibfk_1` FOREIGN KEY (`clase_id`) REFERENCES `clase` (`id_clase`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `notificacion` (
  `id_notificacion` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `mensaje` longtext,
  `fecha` datetime DEFAULT CURRENT_TIMESTAMP,
  `leida` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_notificacion`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `notificacion_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `pago` (
  `id_pago` int NOT NULL AUTO_INCREMENT,
  `membresia_id` int NOT NULL,
  `fecha` datetime(6) DEFAULT NULL,
  `monto` decimal(38,2) DEFAULT NULL,
  `estado` enum('pendiente','completado','fallido') DEFAULT 'pendiente',
  `comprobante_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_pago`),
  KEY `membresia_id` (`membresia_id`),
  CONSTRAINT `pago_ibfk_1` FOREIGN KEY (`membresia_id`) REFERENCES `membresia` (`id_membresia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `perfilusuario` (
  `id_perfil` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `foto_url` varchar(255) DEFAULT NULL,
  `telefono` varchar(255) DEFAULT NULL,
  `id_usuario` int DEFAULT NULL,
  PRIMARY KEY (`id_perfil`),
  UNIQUE KEY `usuario_id` (`usuario_id`),
  UNIQUE KEY `UK2045hpaksb4miy3ehcfpixwsk` (`id_usuario`),
  CONSTRAINT `FK7akmj4bg0lh8vls0vi0x4nv7k` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `perfilusuario_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `plan` (
  `id_plan` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `precio` decimal(38,2) DEFAULT NULL,
  `periodo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id_plan`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `preferencianotificacion` (
  `id_pref` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `silencioso` int NOT NULL,
  PRIMARY KEY (`id_pref`),
  UNIQUE KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `preferencianotificacion_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `rutina` (
  `id_rutina` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `creador_id` int DEFAULT NULL,
  PRIMARY KEY (`id_rutina`),
  KEY `creador_id` (`creador_id`),
  CONSTRAINT `rutina_ibfk_1` FOREIGN KEY (`creador_id`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `rutinadetalle` (
  `id_detalle` int NOT NULL AUTO_INCREMENT,
  `rutina_id` int NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `imagen` varchar(255) DEFAULT NULL,
  `descanso_seg` int NOT NULL,
  PRIMARY KEY (`id_detalle`),
  UNIQUE KEY `rutina_detalle_unica` (`rutina_id`),
  CONSTRAINT `rutinadetalle_ibfk_1` FOREIGN KEY (`rutina_id`) REFERENCES `rutina` (`id_rutina`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `serie` (
  `id_serie` int NOT NULL AUTO_INCREMENT,
  `ejercicioDetalle_id` int NOT NULL,
  `carga` varchar(255) DEFAULT NULL,
  `repeticiones` int DEFAULT NULL,
  PRIMARY KEY (`id_serie`),
  KEY `ejercicioDetalle_id` (`ejercicioDetalle_id`),
  CONSTRAINT `serie_ibfk_1` FOREIGN KEY (`ejercicioDetalle_id`) REFERENCES `ejerciciodetalle` (`id_ejec_det`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password_hash` varchar(255) NOT NULL,
  `estado` tinyint DEFAULT NULL,
  `fecha_alta` datetime DEFAULT CURRENT_TIMESTAMP,
  `rol` tinyint DEFAULT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `usuarioxclase` (
  `id_usu_clase` int NOT NULL AUTO_INCREMENT,
  `usuario_id` int NOT NULL,
  `clase_id` int NOT NULL,
  `aprobado` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id_usu_clase`),
  KEY `usuario_id` (`usuario_id`),
  KEY `clase_id` (`clase_id`),
  CONSTRAINT `usuarioxclase_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `usuarioxclase_ibfk_2` FOREIGN KEY (`clase_id`) REFERENCES `clase` (`id_clase`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
