CREATE TABLE IF NOT EXISTS personas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    genero VARCHAR(20),
    edad INTEGER,
    identificacion VARCHAR(30) NOT NULL UNIQUE,
    direccion VARCHAR(200),
    telefono VARCHAR(30)
);

CREATE TABLE IF NOT EXISTS clientes (
    persona_id BIGINT PRIMARY KEY,
    cliente_id VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(120) NOT NULL,
    estado BOOLEAN NOT NULL,

    CONSTRAINT fk_clientes_personas
        FOREIGN KEY (persona_id)
        REFERENCES personas(id)
        ON DELETE CASCADE
);