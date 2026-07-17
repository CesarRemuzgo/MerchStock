# Base de Datos MerchStock

Scripts SQL para crear la base de datos `merchstock_db` y cargar datos iniciales.

## 📋 Pre-requisitos

- MySQL 8 instalado y corriendo
- MySQL Workbench (recomendado) o cliente MySQL CLI

## 🚀 Instalacion paso a paso

### 1. Abrir MySQL Workbench

Conectar a tu instancia local de MySQL (`localhost:3306`) con tu usuario `root`.

### 2. Ejecutar el script de esquema

1. Abrir el archivo `01-schema.sql` en MySQL Workbench:
   - `File → Open SQL Script...` → seleccionar `01-schema.sql`
2. Ejecutar con `Ctrl + Shift + Enter` o el rayo amarillo
3. Verificar en el Output que aparezcan los mensajes verdes de exito
4. Refrescar el panel Schemas: debe aparecer `merchstock_db` con 7 tablas

### 3. Ejecutar el script de datos iniciales

1. Abrir el archivo `02-seed-data.sql` en MySQL Workbench
2. Ejecutar con `Ctrl + Shift + Enter`
3. Verificar el resultado:
   - 4 usuarios
   - 8 categorias
   - 16 productos

## ✅ Verificar instalacion

Ejecutar en MySQL Workbench:

```sql
USE merchstock_db;
SELECT 'Verificacion' AS test;
SELECT COUNT(*) AS usuarios   FROM usuarios;
SELECT COUNT(*) AS categorias FROM categorias;
SELECT COUNT(*) AS productos  FROM productos;
```

Resultado esperado:
- usuarios: 4
- categorias: 8
- productos: 16

## 🔐 Usuarios iniciales

| Username   | Password    | Rol       |
|------------|-------------|-----------|
| admin      | admin123    | ADMIN     |
| cremuzgo   | admin123    | ADMIN     |
| pventa1    | admin123    | VENDEDOR  |
| avendedor  | admin123    | VENDEDOR  |

*Nota: los passwords estan hasheados con BCrypt. Para que funcione el login*
*real, hay que actualizarlos en la siguiente iteracion del proyecto.*

## 🏗️ Estructura de las 7 tablas

1. **usuarios** - Login del sistema (admin y vendedores)
2. **categorias** - Categorias de productos
3. **productos** - Catalogo con control de stock (CHECK stock >= 0)
4. **clientes** - Clientes que realizan compras
5. **ventas** - Cabecera de ventas
6. **venta_detalle** - Items individuales de cada venta
7. **auditoria_stock** - Trazabilidad de movimientos de stock

## ⚠️ Notas importantes

- El constraint `CHECK (stock_actual >= 0)` en la tabla `productos` es parte
  de la defensa en 3 capas (SQL + Service + UI) para evitar stock negativo.
- Todos los nombres usan `snake_case` (convencion SQL).
- Las foreign keys usan `ON DELETE CASCADE` solo en `venta_detalle`.

## 🔄 Resetear la BD

Si necesitas borrar todo y empezar de nuevo:

```sql
DROP DATABASE IF EXISTS merchstock_db;
```

Luego volver a ejecutar `01-schema.sql` y `02-seed-data.sql`.