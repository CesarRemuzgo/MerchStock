-- ============================================================
-- MerchStock - Datos Iniciales (Seed Data)
-- Curso Integrador I - UTP 2026 - Grupo 3
--
-- Ejecutar este script DESPUES de 01-schema.sql
-- ============================================================

USE merchstock_db;

-- ============================================================
-- 1. USUARIOS INICIALES
-- Password en texto plano: "admin123" (hash BCrypt cost=10)
-- ============================================================
INSERT INTO usuarios (username, password_hash, nombre_completo, email, rol, activo) VALUES
('admin',    '$2a$10$N9qo8uLOickgx2ZMRZoMye1VJMBVkH8U5Z7cLW.rN9Y8Z2qz9Yp1G', 'Administrador del Sistema', 'admin@merchstock.pe',    'ADMIN',    TRUE),
('cremuzgo', '$2a$10$N9qo8uLOickgx2ZMRZoMye1VJMBVkH8U5Z7cLW.rN9Y8Z2qz9Yp1G', 'Cesar Remuzgo Angeles',     'cremuzgo@merchstock.pe', 'ADMIN',    TRUE),
('pventa1',  '$2a$10$N9qo8uLOickgx2ZMRZoMye1VJMBVkH8U5Z7cLW.rN9Y8Z2qz9Yp1G', 'Patricia Rojas Lujan',      'patricia@merchstock.pe', 'VENDEDOR', TRUE),
('avendedor','$2a$10$N9qo8uLOickgx2ZMRZoMye1VJMBVkH8U5Z7cLW.rN9Y8Z2qz9Yp1G', 'Andrea Flores Caceres',     'andrea@merchstock.pe',   'VENDEDOR', TRUE);

-- ============================================================
-- 2. CATEGORIAS (8 lineas de productos de MerchStock)
-- ============================================================
INSERT INTO categorias (nombre, descripcion) VALUES
('Tazas',           'Tazas sublimadas personalizadas'),
('Polos',           'Polos estampados con diseno corporativo'),
('Gorras',          'Gorras bordadas y estampadas'),
('Llaveros',        'Llaveros metalicos y de PVC personalizados'),
('Tomatodos',       'Botellas deportivas y tomatodos personalizados'),
('Lapiceros',       'Lapiceros publicitarios personalizados'),
('Bolsas Ecologicas','Bolsas de tela ecologicas personalizadas'),
('Lanyards',        'Cintas porta-credenciales personalizadas');

-- ============================================================
-- 3. PRODUCTOS DE MUESTRA (16 productos)
-- ============================================================
INSERT INTO productos (sku, nombre, descripcion, categoria_id, precio_compra, precio_venta, stock_actual, stock_minimo) VALUES
('TAZ-001', 'Taza Ceramica Blanca 11oz',   'Taza ceramica blanca apta para sublimacion', 1, 5.50,  15.00, 50, 10),
('TAZ-002', 'Taza Magica Negra 11oz',      'Taza magica que revela diseno con calor',    1, 12.00, 28.00, 25, 5),
('POL-001', 'Polo Blanco Algodon M',       'Polo blanco 100% algodon talla M',           2, 18.00, 45.00, 40, 8),
('POL-002', 'Polo Negro Algodon L',        'Polo negro 100% algodon talla L',            2, 18.00, 45.00, 35, 8),
('GOR-001', 'Gorra Trucker Negra',         'Gorra trucker con malla atras color negro',  3, 14.00, 32.00, 30, 6),
('GOR-002', 'Gorra Snapback Roja',         'Gorra snapback ajustable color rojo',        3, 16.00, 38.00, 20, 5),
('LLA-001', 'Llavero Metalico Plateado',   'Llavero metalico rectangular grabado laser', 4, 3.50,  10.00, 100, 20),
('LLA-002', 'Llavero PVC Logo',            'Llavero de PVC con logo corporativo',        4, 2.50,  8.00,  80, 15),
('TOM-001', 'Tomatodo Aluminio 750ml',     'Tomatodo de aluminio deportivo 750ml',       5, 18.00, 42.00, 25, 5),
('TOM-002', 'Botella Plastico BPA-Free',   'Botella plastica libre de BPA 600ml',        5, 8.00,  22.00, 40, 8),
('LAP-001', 'Lapicero Plastico Azul',      'Lapicero plastico publicitario color azul',  6, 1.20,  4.50,  200, 50),
('LAP-002', 'Lapicero Metalico Plateado',  'Lapicero metalico ejecutivo plateado',       6, 6.50,  18.00, 60, 12),
('BOL-001', 'Bolsa Tela Cruda 35x40',      'Bolsa de tela cruda con asas largas',        7, 4.00,  12.00, 70, 15),
('BOL-002', 'Bolsa Yute Mediana',          'Bolsa de yute natural mediana',              7, 7.00,  20.00, 45, 10),
('LAN-001', 'Lanyard Poliester Negro',     'Cinta lanyard de poliester con clip',        8, 2.80,  9.00,  120, 25),
('LAN-002', 'Lanyard Sublimado Color',     'Lanyard sublimado a todo color',             8, 4.50,  14.00, 80, 15);

-- ============================================================
-- VERIFICAR datos insertados
-- ============================================================
SELECT 'Datos iniciales insertados correctamente' AS resultado;
SELECT COUNT(*) AS total_usuarios   FROM usuarios;
SELECT COUNT(*) AS total_categorias FROM categorias;
SELECT COUNT(*) AS total_productos  FROM productos;