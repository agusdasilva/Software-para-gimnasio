# Software-para-gimnasio
Proyecto para el TP final de la UTN, desarrollado por Agustin Dasilva y Hernan Cerillano.

# Sistema de Gestión de Gimnasio – Java + Angular

Este proyecto es un sistema completo para la administración integral de un gimnasio.
Incluye backend en **Java Spring Boot**, frontend en **Angular**, y base de datos en **MySQL**.

El objetivo es permitir la gestión de usuarios, rutinas, clases grupales, invitaciones, membresías, pagos y notificaciones de forma simple y profesional.

##  Características Principales

### • Usuarios y Autenticación
- Registro e inicio de sesión.
- Roles: Administrador, Entrenador y Cliente.
- Preferencias de notificación.

### • Rutinas y Ejercicios
- CRUD de rutinas.
- Ejercicios con imágenes, series, repeticiones y descanso.
- Detalle ampliado para clientes y entrenadores.

### • Clases Grupales
- Crear, editar, listar y eliminar clases.
- Agregar rutinas a una clase.
- Miembros de la clase.
- Invitaciones.
- Sistema de mensajes dentro de la clase.

### • Membresías y Planes
- Creación y gestión de planes.
- Membresía activa por usuario.
- Historial completo de membresías.

### • Pagos
- Registrar pagos.
- Métodos de pago.
- Ver historial por usuario.

### • Notificaciones
- Notificaciones del sistema.
- Configuración por usuario.
- Eventos: invitaciones, clases nuevas, mensajes, vencimientos.

## Tecnologías Utilizadas

### Backend
- Java 17+
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Validation
- Hibernate
- Maven
- MySQL
- Lombok

### Frontend
- Angular 17
- Standalone Components
- RxJS
- HTML, SCSS

## 🏗 Arquitectura del Proyecto

El backend sigue arquitectura en capas:

Controller → Service → Repository → Entity → Database

El frontend sigue estructura modular con componentes independientes.

## Base de Datos

Las tablas principales son:

usuarios, roles, rutinas, ejercicios, clases, invitaciones, mensajes,
planes, membresias, pagos, notificaciones, preferencias_notificacion

##  Cómo Ejecutar el Backend

1. Clonar el repositorio:
git clone <https://github.com/agusdasilva/Software-para-gimnasio.git>

2. Crear una base de datos en MySQL:
gimnasio_db

3. Configurar application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/gimnasio_db
spring.datasource.username=root
spring.datasource.password=TU_CLAVE
spring.jpa.hibernate.ddl-auto=update

4. Ejecutar:
mvn spring-boot:run

## Cómo Ejecutar el Frontend

1. Clonar el repositorio:
git clone <https://github.com/agusdasilva/Software-para-gimnasio.git>

2. Instalar dependencias:
npm install

3. Ejecutar:
ng serve -o

Abrir en http://localhost:4200

## Endpoints Principales (Resumen)

### Usuarios
POST /api/usuarios
POST /api/login
GET /api/usuarios/{id}

### Clases
GET /api/clases
GET /api/clases/{id}
POST /api/clases
PUT /api/clases/{id}
DELETE /api/clases/{id}
POST /api/clases/{id}/rutinas/{idRutina}
GET /api/clases/{id}/miembros
GET /api/clases/{id}/mensajes
POST /api/clases/{id}/mensajes

### Invitaciones
POST /api/invitaciones
GET /api/invitaciones/{id}
GET /api/invitaciones/usuario/{idUsuario}

### Rutinas
GET /api/rutinas
POST /api/rutinas
PUT /api/rutinas/{id}
DELETE /api/rutinas/{id}

### Membresías y Planes
GET /api/planes
POST /api/planes
PUT /api/planes/{id}
DELETE /api/planes/{id}
POST /api/membresias
GET /api/membresias/activa/{idUsuario}

## Buenas Prácticas Aplicadas

- Uso de DTOs en todas las requests/responses.
- Validaciones con @Valid y @NotNull.
- Control global de excepciones.
- Métodos cortos, legibles y mantenibles.
- Código organizado por capas.
- Naming consistente.

## Autores

- Hernán Cerillano — Backend (Spring Boot)
- Agustin Dasilva — Frontend (Angular) + Base de datos

## Estado del Proyecto

✔ Backend terminado
✔ Frontend terminado


## Licencia

Uso privado o comercial para implementación en gimnasios.
