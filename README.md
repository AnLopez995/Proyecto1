# Microservicios Proyecto 1

Proyecto backend desarrollado con **Java 17**, **Spring Boot**, **PostgreSQL**, **RabbitMQ** y **Docker Compose**.
El sistema está dividido en dos microservicios:

* `customer-service`: gestión de clientes y personas.
* `account-service`: gestión de cuentas, movimientos y reportes.

## Tecnologías

* Java 17
* Spring Boot 3
* Maven
* PostgreSQL
* RabbitMQ
* Docker / Docker Compose
* JUnit 5
* Mockito
* H2 para pruebas

## Arquitectura general

El proyecto utiliza una estructura orientada a capas:

```text
domain          -> entidades y reglas de negocio
application     -> servicios de aplicación
infrastructure  -> controladores, persistencia, mensajería y configuración
```

## Servicios

| Servicio            | Puerto | Descripción                     |
| ------------------- | -----: | ------------------------------- |
| customer-service    |   8081 | Clientes y personas             |
| account-service     |   8082 | Cuentas, movimientos y reportes |
| RabbitMQ Management |  15672 | Consola web de RabbitMQ         |

## Ejecutar el proyecto

Desde la raíz del proyecto:

```bash
docker compose up --build
```

Para reiniciar completamente las bases de datos y volver a ejecutar la carga inicial:

```bash
docker compose down -v
docker compose up --build
```

## RabbitMQ

Consola web:

```text
http://localhost:15672
```

Credenciales:

```text
usuario: guest
clave: guest
```

## Endpoints principales

### Clientes

```http
POST   /api/clientes
GET    /api/clientes
GET    /api/clientes/{id}
PUT    /api/clientes/{id}
PATCH  /api/clientes/{id}/estado
DELETE /api/clientes/{id}
```

### Cuentas

```http
POST   /api/cuentas
GET    /api/cuentas
GET    /api/cuentas/{id}
PUT    /api/cuentas/{id}
PATCH  /api/cuentas/{id}/estado
```

### Movimientos

```http
POST   /api/movimientos
GET    /api/movimientos
```

### Reportes

```http
GET /api/reportes?clienteId=1&fechaInicio=2026-06-01&fechaFin=2026-06-30
```

## Ejecutar pruebas

### customer-service

```bash
cd customer-service
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

### account-service

```bash
cd account-service
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

## Cobertura de pruebas

El proyecto incluye pruebas unitarias e integración para:

* Creación, consulta, actualización, cambio de estado y eliminación de clientes.
* Creación, consulta, actualización y cambio de estado de cuentas.
* Registro de depósitos y retiros.
* Validación de saldo no disponible.
* Validación de movimientos inválidos.
* Generación de reportes por cliente y rango de fechas.

## Carga inicial de datos

Al ejecutar Docker Compose se realiza una carga inicial automática de clientes, cuentas y movimientos mediante el contenedor `seed-data`.

## Notas técnicas

* Los movimientos no se actualizan directamente para mantener trazabilidad.
* Las respuestas de error se manejan de forma centralizada con `GlobalExceptionHandler`.
* En Docker se usa una configuración controlada de base de datos mediante scripts SQL.
* Las pruebas usan H2 en memoria y no dependen de PostgreSQL ni RabbitMQ reales.
