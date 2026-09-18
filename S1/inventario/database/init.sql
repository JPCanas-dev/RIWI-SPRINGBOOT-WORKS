-- 1. CREACIÓN DE TABLAS CON REGLAS DE NEGOCIO (CONSTRAINTS)

CREATE TABLE IF NOT EXISTS categoria (
    id SERIAL PRIMARY KEY,
    -- Regla: El nombre no puede ser nulo ni estar vacío o lleno de espacios
    nombre VARCHAR(255) NOT NULL CHECK (TRIM(nombre) <> '')
);

CREATE TABLE IF NOT EXISTS producto (
    id SERIAL PRIMARY KEY,
    -- Regla: El nombre no puede estar vacío
    nombre VARCHAR(255) NOT NULL CHECK (TRIM(nombre) <> ''),

    -- Regla: El precio debe ser obligatoriamente mayor a 0
    precio DOUBLE PRECISION NOT NULL CHECK (precio > 0),

    categoria_id BIGINT NOT NULL,

    -- Regla: Llave foránea que BLOQUEA la eliminación de una categoría si tiene productos (RESTRICT)
    CONSTRAINT fk_categoria FOREIGN KEY (categoria_id)
    REFERENCES categoria(id) ON DELETE RESTRICT
);

-- 2. INSERCIÓN DE DATOS INICIALES (Semillas)

-- Insertar Categorías
INSERT INTO categoria (nombre) VALUES ('Electrónica');    -- ID 1
INSERT INTO categoria (nombre) VALUES ('Lácteos');        -- ID 2
INSERT INTO categoria (nombre) VALUES ('Aseo Personal');  -- ID 3
INSERT INTO categoria (nombre) VALUES ('Ropa');           -- ID 4

-- Insertar Productos
INSERT INTO producto (nombre, precio, categoria_id) VALUES ('Televisor 50 pulgadas', 3500000.50, 1);
INSERT INTO producto (nombre, precio, categoria_id) VALUES ('Leche deslactosada 1L', 7600.20, 2);
INSERT INTO producto (nombre, precio, categoria_id) VALUES ('Queso campesino', 10000.50, 2);
INSERT INTO producto (nombre, precio, categoria_id) VALUES ('Jabón de baño', 11500, 3);
INSERT INTO producto (nombre, precio, categoria_id) VALUES ('Camiseta de algodón', 80000, 4);