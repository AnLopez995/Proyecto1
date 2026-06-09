CREATE TABLE IF NOT EXISTS cliente_read_model (
    cliente_id BIGINT PRIMARY KEY,
    codigo_cliente VARCHAR(50) NOT NULL,
    nombre VARCHAR(120) NOT NULL,
    identificacion VARCHAR(30) NOT NULL,
    estado BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS cuentas (
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(30) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(30) NOT NULL,
    saldo_inicial NUMERIC(19, 2) NOT NULL,
    saldo_disponible NUMERIC(19, 2) NOT NULL,
    estado BOOLEAN NOT NULL,
    cliente_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS movimientos (
    id BIGSERIAL PRIMARY KEY,
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(30) NOT NULL,
    valor NUMERIC(19, 2) NOT NULL,
    saldo NUMERIC(19, 2) NOT NULL,
    cuenta_id BIGINT NOT NULL,

    CONSTRAINT fk_movimientos_cuentas
        FOREIGN KEY (cuenta_id)
        REFERENCES cuentas(id)
);