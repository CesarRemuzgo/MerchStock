-- ============================================================
-- MerchStock - Script de Esquema (DDL)
-- Curso Integrador I - UTP 2026 - Grupo 3
--
-- Ejecutar este script PRIMERO para crear la BD y las 7 tablas.
-- Despues ejecutar 02-seed-data.sql para insertar los datos iniciales.
-- ============================================================

-- Crear la base de datos MerchStock
CREATE DATABASE IF NOT EXISTS merchstock_db
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE merchstock_db;

-- ============================================================
-- 1. TABLA usuarios
-- ============================================================
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    rol ENUM('ADMIN', 'VENDEDOR') DEFAULT 'VENDEDOR' NOT NULL,
    activo BOOLEAN DEFAULT TRUE NOT NULL,
    intentos_fallidos INT DEFAULT 0,
    bloqueado_hasta DATETIME NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ultimo_acceso DATETIME NULL,
    INDEX idx_usuarios_username (username),
    INDEX idx_usuarios_rol (rol)
) ENGINE=InnoDB COMMENT='Usuarios del sistema (Admin y Vendedores)';

-- ============================================================
-- 2. TABLA categorias
-- ============================================================
CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200) NULL,
    activo BOOLEAN DEFAULT TRUE NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
) ENGINE=InnoDB COMMENT='Categorias de productos';

-- ============================================================
-- 3. TABLA productos (con constraint de stock no negativo)
-- ============================================================
CREATE TABLE IF NOT EXISTS productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    sku VARCHAR(30) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT NULL,
    categoria_id BIGINT NOT NULL,
    precio_compra DECIMAL(10,2) NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    stock_actual INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 5,
    unidad_medida VARCHAR(20) DEFAULT 'unidad',
    imagen_url VARCHAR(255) NULL,
    activo BOOLEAN DEFAULT TRUE NOT NULL,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    fecha_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT chk_stock_no_negativo CHECK (stock_actual >= 0),
    CONSTRAINT chk_stock_minimo_positivo CHECK (stock_minimo >= 0),
    CONSTRAINT chk_precio_compra_positivo CHECK (precio_compra >= 0),
    CONSTRAINT chk_precio_venta_positivo CHECK (precio_venta >= 0),
    CONSTRAINT fk_productos_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    INDEX idx_productos_sku (sku),
    INDEX idx_productos_categoria (categoria_id),
    INDEX idx_productos_stock_bajo (stock_actual, stock_minimo)
) ENGINE=InnoDB COMMENT='Catalogo de productos con control de stock';

-- ============================================================
-- 4. TABLA clientes
-- ============================================================
CREATE TABLE IF NOT EXISTS clientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_documento ENUM('DNI', 'RUC', 'CE', 'PASAPORTE') DEFAULT 'DNI' NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    nombre_completo VARCHAR(150) NOT NULL,
    telefono VARCHAR(20) NULL,
    email VARCHAR(100) NULL,
    direccion VARCHAR(200) NULL,
    activo BOOLEAN DEFAULT TRUE NOT NULL,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    INDEX idx_clientes_documento (numero_documento)
) ENGINE=InnoDB COMMENT='Clientes de MerchStock';

-- ============================================================
-- 5. TABLA ventas (cabecera)
-- ============================================================
CREATE TABLE IF NOT EXISTS ventas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo_venta VARCHAR(20) NOT NULL UNIQUE,
    cliente_id BIGINT NULL,
    usuario_id BIGINT NOT NULL,
    fecha_venta DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    igv DECIMAL(10,2) NOT NULL DEFAULT 0,
    total DECIMAL(10,2) NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TARJETA', 'YAPE', 'PLIN', 'TRANSFERENCIA') DEFAULT 'EFECTIVO' NOT NULL,
    estado ENUM('COMPLETADA', 'ANULADA', 'PENDIENTE') DEFAULT 'COMPLETADA' NOT NULL,
    observaciones VARCHAR(255) NULL,
    CONSTRAINT chk_venta_total_positivo CHECK (total >= 0),
    CONSTRAINT fk_ventas_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id),
    CONSTRAINT fk_ventas_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    INDEX idx_ventas_fecha (fecha_venta),
    INDEX idx_ventas_codigo (codigo_venta),
    INDEX idx_ventas_estado (estado)
) ENGINE=InnoDB COMMENT='Cabecera de ventas';

-- ============================================================
-- 6. TABLA venta_detalle
-- ============================================================
CREATE TABLE IF NOT EXISTS venta_detalle (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    venta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT chk_cantidad_positiva CHECK (cantidad > 0),
    CONSTRAINT chk_precio_unitario_positivo CHECK (precio_unitario >= 0),
    CONSTRAINT fk_detalle_venta FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES productos(id),
    INDEX idx_detalle_venta (venta_id),
    INDEX idx_detalle_producto (producto_id)
) ENGINE=InnoDB COMMENT='Detalle de items por venta';

-- ============================================================
-- 7. TABLA auditoria_stock
-- ============================================================
CREATE TABLE IF NOT EXISTS auditoria_stock (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    tipo_movimiento ENUM('ENTRADA', 'SALIDA', 'AJUSTE') NOT NULL,
    cantidad INT NOT NULL,
    stock_anterior INT NOT NULL,
    stock_nuevo INT NOT NULL,
    motivo VARCHAR(200) NULL,
    usuario_id BIGINT NOT NULL,
    venta_id BIGINT NULL,
    fecha_movimiento DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_auditoria_producto FOREIGN KEY (producto_id) REFERENCES productos(id),
    CONSTRAINT fk_auditoria_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_auditoria_venta FOREIGN KEY (venta_id) REFERENCES ventas(id),
    INDEX idx_auditoria_producto (producto_id),
    INDEX idx_auditoria_fecha (fecha_movimiento)
) ENGINE=InnoDB COMMENT='Trazabilidad de movimientos de stock';

-- ============================================================
-- VERIFICAR creacion
-- ============================================================
SELECT 'Esquema MerchStock creado exitosamente' AS resultado;
SHOW TABLES;