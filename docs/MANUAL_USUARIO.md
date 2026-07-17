# Manual de Usuario — MerchStock
**Sistema Web de Administración de Inventario**
MerchStock Perú E.I.R.L. · Av. Carlos Izaguirre 845, Los Olivos, Lima

> Versión 2.0 · Proyecto Final · UTP 2026 · Grupo 3

---

## Tabla de Contenidos

1. [Introducción](#1-introducción)
2. [Requisitos para el usuario](#2-requisitos-para-el-usuario)
3. [Acceso al sistema](#3-acceso-al-sistema)
4. [Interfaz principal — Panel de Control](#4-interfaz-principal--panel-de-control)
5. [Módulo Productos](#5-módulo-productos)
6. [Módulo Categorías](#6-módulo-categorías)
7. [Módulo Clientes](#7-módulo-clientes)
8. [Módulo Usuarios](#8-módulo-usuarios)
9. [Módulo Ventas](#9-módulo-ventas)
10. [Módulo Reportes](#10-módulo-reportes)
11. [Módulo Alertas de Stock](#11-módulo-alertas-de-stock)
12. [Importación masiva de productos (CSV)](#12-importación-masiva-de-productos-csv)
13. [Roles y permisos](#13-roles-y-permisos)
14. [Cierre de sesión](#14-cierre-de-sesión)
15. [Mensajes de error frecuentes](#15-mensajes-de-error-frecuentes)

---

## 1. Introducción

**MerchStock** es una aplicación web para la gestión de inventario de MerchStock Perú E.I.R.L., empresa dedicada a la venta de merchandise (tazas, polos, gorras, llaveros y similares). El sistema permite:

- Administrar el catálogo de productos y categorías.
- Registrar ventas con descuento automático de stock.
- Gestionar clientes y usuarios del sistema.
- Generar reportes en formato Excel.
- Importar productos en lote desde archivos CSV.
- Visualizar alertas cuando el stock de un producto cae por debajo del mínimo configurado.

El sistema opera en un navegador web estándar (Chrome, Edge, Firefox) y no requiere instalación en el equipo del usuario.

---

## 2. Requisitos para el usuario

| Requisito | Detalle |
|-----------|---------|
| Navegador | Google Chrome 110+, Microsoft Edge 110+, Mozilla Firefox 110+ |
| Conexión | Red local o acceso a la dirección del servidor |
| Credenciales | Usuario y contraseña proporcionados por el administrador |

No se requiere instalar ningún software adicional. Solo necesitas un navegador actualizado.

---

## 3. Acceso al sistema

### 3.1 Ingresar al sistema

1. Abre tu navegador e ingresa la dirección del sistema (por ejemplo: `http://localhost:8080`).
2. Serás redirigido automáticamente a la pantalla de **inicio de sesión**.
3. Ingresa tu **nombre de usuario** y **contraseña**.
4. Haz clic en el botón **Iniciar Sesión**.

Si las credenciales son correctas, el sistema te llevará al **Panel de Control** (Dashboard).

### 3.2 Credenciales de acceso

Las credenciales de acceso son proporcionadas por el administrador del sistema. Por motivos de seguridad, esta información no se muestra en la pantalla de inicio de sesión.

>  Cambia las contraseñas predeterminadas en el primer inicio de sesión. No compartas tus credenciales con otras personas.

### 3.3 Sesión expirada

Si tu sesión expira por inactividad, el sistema te redirigirá automáticamente a la pantalla de login. Vuelve a ingresar tus credenciales para continuar.

### 3.4 Acceso denegado

Si intentas acceder a una sección para la que no tienes permiso, verás una pantalla de **Acceso Denegado (403)**. Contacta al administrador si necesitas acceso a esa funcionalidad.

---

## 4. Interfaz principal — Panel de Control

Al ingresar al sistema verás el **Panel de Control** con la siguiente información en tiempo real:

### 4.1 Tarjetas de estadísticas

| Tarjeta | Descripción |
|---------|-------------|
| **Total Productos** | Número total de productos activos en el catálogo |
| **Alertas de Stock** | Productos con stock actual menor o igual al mínimo configurado |
| **Ventas del Día** | Número de ventas registradas en el día actual |
| **Ingresos del Día** | Monto total (S/) facturado en el día actual |

### 4.2 Tabla de últimas ventas

Debajo de las tarjetas se muestra una tabla con las ventas más recientes, incluyendo el código de venta, fecha, cliente, vendedor y estado.

### 4.3 Barra de navegación

En la parte superior de todas las pantallas encontrarás la barra de navegación con los siguientes accesos:

- **Productos** — Gestión del catálogo
- **Categorias** — Administración de categorías
- **Clientes** — Registro de clientes
- **Usuarios** — Gestión de usuarios del sistema
- **Ventas** — Registro y consulta de ventas
- **Reportes** — Descarga de reportes Excel
- **Alertas** — Productos con stock bajo (muestra un contador en rojo si hay alertas)
- **Monitoreo** — Panel de estado del sistema (solo visible para ADMIN)
- **Mantenimiento** — Panel de respaldos de base de datos (solo visible para ADMIN)

A la derecha de la barra verás tu nombre de usuario. Haz clic sobre él para acceder al menú de cierre de sesión.

---

## 5. Módulo Productos

Acceso: **Productos** en el menú superior.

### 5.1 Listar productos

La pantalla muestra todos los productos activos con las columnas: SKU, Nombre, Categoría, Precio Venta, Stock Actual, Stock Mínimo y Estado de stock.

- Los productos con stock bajo aparecen con la etiqueta **STOCK BAJO** en rojo.
- Los productos con stock suficiente muestran la etiqueta **OK** en verde.
- Usa el **buscador** en la parte superior de la tabla para filtrar por nombre o SKU.

### 5.2 Crear un producto

1. Haz clic en el botón **Nuevo Producto**.
2. Completa el formulario con los siguientes campos:

| Campo | Descripción | Obligatorio |
|-------|-------------|:-----------:|
| SKU | Código único del producto (se normaliza a mayúsculas automáticamente) | ✅ |
| Nombre | Nombre descriptivo del producto | ✅ |
| Descripción | Descripción detallada (opcional) | ❌ |
| Categoría | Selecciona de la lista desplegable | ✅ |
| Precio de Compra | Costo del producto en S/ | ✅ |
| Precio de Venta | Precio al cliente en S/ | ✅ |
| Stock Actual | Cantidad disponible actualmente | ✅ |
| Stock Mínimo | Cantidad mínima antes de generar alerta | ✅ |
| Unidad de Medida | Por defecto: "unidad" | ❌ |
| URL de Imagen | Enlace a la imagen del producto | ❌ |

3. Haz clic en **Guardar**.

> **Nota:** El SKU debe ser único en el sistema. Si ya existe, recibirás un mensaje de error.

### 5.3 Editar un producto

1. En la lista de productos, haz clic en el botón **Editar** (ícono de lápiz) junto al producto.
2. Modifica los campos que necesites.
3. Haz clic en **Guardar**.

### 5.4 Eliminar un producto

1. En la lista de productos, haz clic en el botón **Eliminar** (ícono de papelera) junto al producto.
2. Confirma la acción en el diálogo.

> El sistema realiza una **eliminación lógica**: el producto no se borra de la base de datos, sino que queda marcado como inactivo y deja de aparecer en el catálogo.

---

## 6. Módulo Categorías

Acceso: **Categorias** en el menú superior.
>  Solo disponible para usuarios con rol **ADMIN**.

### 6.1 Listar categorías

Muestra todas las categorías activas del sistema con su nombre y descripción.

### 6.2 Crear una categoría

1. Haz clic en **Nueva Categoría**.
2. Ingresa el **nombre** (obligatorio) y una **descripción** (opcional).
3. Haz clic en **Guardar**.

> El nombre de categoría debe ser único. Si ya existe, recibirás un error de validación.

### 6.3 Editar / Eliminar una categoría

Funciona igual que en el módulo de Productos: botones **Editar** y **Eliminar** junto a cada fila. La eliminación es lógica.

> **Importante:** No puedes eliminar una categoría que tenga productos asociados activos.

---

## 7. Módulo Clientes

Acceso: **Clientes** en el menú superior.

### 7.1 Listar clientes

Muestra todos los clientes registrados con nombre, tipo de documento, número de documento y estado.

### 7.2 Crear un cliente

1. Haz clic en **Nuevo Cliente**.
2. Completa el formulario:

| Campo | Descripción | Obligatorio |
|-------|-------------|:-----------:|
| Tipo de Documento | DNI / RUC / CE / PASAPORTE | ✅ |
| Número de Documento | Sin espacios ni guiones | ✅ |
| Nombre Completo | Nombre y apellidos o razón social | ✅ |
| Teléfono | Número de contacto | ❌ |
| Email | Correo electrónico | ❌ |
| Dirección | Dirección del cliente | ❌ |

3. Haz clic en **Guardar**.

> El número de documento debe ser único. Si ya existe, el sistema mostrará un error.

### 7.3 Reactivación automática

Si intentas registrar un cliente con un número de documento que ya existe pero está **inactivo**, el sistema lo reactivará automáticamente al editarlo, en lugar de crear un duplicado.

### 7.4 Editar / Eliminar un cliente

Funciona igual que en otros módulos. La eliminación es lógica.

---

## 8. Módulo Usuarios

Acceso: **Usuarios** en el menú superior.
>  Solo disponible para usuarios con rol **ADMIN**.

### 8.1 Listar usuarios

Muestra todos los usuarios activos con su nombre de usuario, nombre completo, rol (ADMIN en rojo, VENDEDOR en azul) y estado.

### 8.2 Crear un usuario

1. Haz clic en **Nuevo Usuario**.
2. Completa el formulario:

| Campo | Descripción | Obligatorio |
|-------|-------------|:-----------:|
| Username | Nombre de usuario para iniciar sesión | ✅ |
| Nombre Completo | Nombre real del usuario | ✅ |
| Email | Correo del usuario | ❌ |
| Contraseña | Mínimo 6 caracteres | ✅ |
| Rol | ADMIN o VENDEDOR | ✅ |

3. Haz clic en **Guardar**.

> Las contraseñas se almacenan encriptadas con BCrypt. El sistema nunca guarda contraseñas en texto plano.

### 8.3 Editar un usuario

Al editar, si dejas el campo **Contraseña vacío**, el sistema conserva la contraseña actual sin cambios. Solo escribe una nueva contraseña si deseas modificarla.

### 8.4 Eliminar un usuario

La eliminación es lógica. El usuario queda inactivo y no podrá iniciar sesión.

---

## 9. Módulo Ventas

Acceso: **Ventas** en el menú superior.

### 9.1 Listar ventas

Muestra el historial de ventas con: código, fecha, cliente, vendedor, total y estado (COMPLETADA / ANULADA / PENDIENTE).

Haz clic en el botón **Ver Detalle** para ver el desglose de productos de una venta.

### 9.2 Registrar una venta

1. Haz clic en **Nueva Venta**.
2. Selecciona el **Cliente** (opcional — puedes dejarlo en blanco para "Cliente genérico").
3. Selecciona el **Vendedor** que registra la venta (obligatorio).
4. **Agrega productos al carrito:**
   - Selecciona un producto del desplegable.
   - Ingresa la cantidad deseada.
   - Haz clic en **Agregar al Carrito**.
   - Repite para cada producto adicional.
5. El sistema calcula automáticamente el desglose de impuestos. Es importante entender que **el precio de cada producto ya incluye el IGV** (18%), por lo que el cálculo se hace de forma inversa:
   - **Total** = suma de los precios de venta de los ítems del carrito (el precio que ves ya incluye IGV)
   - **Operación Gravada** = Total ÷ 1.18
   - **IGV** = Total − Operación Gravada

   Este método garantiza exactitud al céntimo en el desglose, evitando errores de redondeo que ocurrirían si el IGV se calculara multiplicando directamente sobre un subtotal.
6. Selecciona el **Método de Pago**: EFECTIVO, TARJETA, YAPE, PLIN o TRANSFERENCIA.
7. Agrega **Observaciones** si es necesario (opcional).
8. Haz clic en **Registrar Venta**.

Al completarse, el sistema genera automáticamente un **código único** con el formato `VTA-AAAA-NNNN` (por ejemplo: `VTA-2026-0001`) y descuenta el stock de cada producto vendido.

> **Importante:** El sistema valida en tres capas que ningún producto quede con stock negativo. Si la cantidad solicitada supera el stock disponible, la venta no se procesará y verás un mensaje de error.

### 9.3 Ver el detalle de una venta

En la lista de ventas, haz clic en **Ver Detalle** para acceder a la pantalla de detalle, que muestra todos los productos incluidos, cantidades, precios unitarios, IGV y total.

### 9.4 Anular una venta

Desde la pantalla de detalle de una venta, el administrador puede **Anular** la venta. Al anularla, el sistema revierte automáticamente el stock de todos los productos involucrados.

---

## 10. Módulo Reportes

Acceso: **Reportes** en el menú superior.

El sistema genera tres tipos de reportes en formato **Excel (.xlsx)**, listos para abrir con Microsoft Excel o LibreOffice Calc.

| Reporte | Contenido | Botón |
|---------|-----------|-------|
| **Reporte de Productos** | Listado completo del catálogo activo | Descargar |
| **Reporte de Stock Bajo** | Solo los productos con alertas de stock bajo | Descargar |
| **Reporte de Ventas** | Historial completo de ventas registradas | Descargar |

### Cómo descargar un reporte

1. Haz clic en el botón **Descargar** del reporte deseado.
2. El archivo se descargará automáticamente con el nombre `MerchStock_TIPO_YYYYMMDD.xlsx`.
3. Abre el archivo con Excel o cualquier programa compatible.

Los reportes incluyen encabezados con el estilo corporativo (fondo navy, texto blanco) y las columnas se ajustan automáticamente al contenido.

---

## 11. Módulo Alertas de Stock

Acceso: **Alertas** en el menú superior.

Esta pantalla lista todos los productos cuyo **stock actual es menor o igual al stock mínimo** configurado.

La barra de navegación muestra un **contador en rojo** junto al enlace Alertas cuando hay productos en esta condición, para que el administrador y el vendedor estén siempre al tanto.

Desde esta pantalla puedes ver el nombre, SKU, categoría, stock actual y stock mínimo de cada producto afectado. Para reponer el stock, ve a **Productos → Editar** el producto correspondiente.

---

## 12. Importación masiva de productos (CSV)

Esta función permite cargar múltiples productos a la vez desde un archivo de texto en formato CSV, ahorrando tiempo frente al registro individual.

>  Solo disponible para usuarios con rol **ADMIN**.

### 12.1 Acceder a la importación

En la lista de Productos, haz clic en el botón **Importar CSV**.

### 12.2 Formato del archivo CSV

El archivo debe ser texto plano con codificación UTF-8 y comas como separador. La **primera línea debe ser la cabecera** exactamente así:

```
sku,nombre,categoria,precio_compra,precio_venta,stock_actual,stock_minimo
```

**Ejemplo de archivo válido:**

```
sku,nombre,categoria,precio_compra,precio_venta,stock_actual,stock_minimo
TAZ-100,Taza Magica Roja,Tazas,8.50,18.00,50,10
POL-100,Polo Premium Azul,Polos,15.00,35.00,30,5
GOR-050,Gorra Snapback,Gorras,12.00,25.00,20,5
```

**Reglas importantes:**

- El campo `categoria` debe coincidir con una categoría que **ya exista** en el sistema (no distingue mayúsculas/minúsculas).
- El `sku` se normaliza automáticamente a mayúsculas.
- El `sku` no puede estar duplicado en el sistema.
- Los precios deben usar punto (`.`) como separador decimal.
- Las cantidades de stock deben ser números enteros positivos.

### 12.3 Proceso de importación

1. Haz clic en **Seleccionar archivo** y elige tu archivo `.csv`.
2. Haz clic en **Importar Productos**.
3. Al terminar, el sistema muestra un resumen con la cantidad de productos importados exitosamente y los errores encontrados por fila (si los hay).

Una fila con error no cancela el resto del proceso: las filas válidas se importan igualmente.

---

## 13. Roles y permisos

El sistema cuenta con dos roles con distintos niveles de acceso:

| Módulo / Acción | ADMIN | VENDEDOR |
|-----------------|:-----:|:--------:|
| Ver Dashboard | ✅ | ✅ |
| Ver lista de Productos | ✅ | ✅ |
| Crear / Editar / Eliminar Productos | ✅ | ❌ |
| Importar CSV de Productos | ✅ | ❌ |
| Módulo Categorías (completo) | ✅ | ❌ |
| Ver y crear Clientes | ✅ | ✅ |
| Módulo Usuarios (completo) | ✅ | ❌ |
| Registrar Ventas | ✅ | ✅ |
| Anular Ventas | ✅ | ❌ |
| Ver Reportes y descargar Excel | ✅ | ✅ |
| Ver Alertas de Stock | ✅ | ✅ |
| Ver Panel de Monitoreo | ✅ | ❌ |
| Ver Panel de Mantenimiento (respaldos) | ✅ | ❌ |

Si intentas realizar una acción para la que no tienes permiso, el sistema te mostrará la pantalla de **Acceso Denegado**.

---

## 14. Cierre de sesión

1. Haz clic en tu **nombre de usuario** en la esquina superior derecha de la barra de navegación.
2. En el menú desplegable, haz clic en **Cerrar Sesión**.
3. Serás redirigido a la pantalla de inicio de sesión.

> Por seguridad, cierra siempre tu sesión cuando termines de usar el sistema, especialmente en equipos compartidos.

---

## 15. Mensajes de error frecuentes

| Mensaje | Causa | Solución |
|---------|-------|----------|
| "El SKU ya existe en el sistema" | Intentas crear un producto con un SKU ya registrado | Usa un SKU diferente o edita el producto existente |
| "Stock insuficiente para completar la venta" | La cantidad solicitada supera el stock disponible | Reduce la cantidad o actualiza el stock del producto primero |
| "El nombre de la categoría ya existe" | Categoría duplicada | Usa la categoría existente o elige otro nombre |
| "El número de documento ya está registrado" | Cliente duplicado | Busca y edita el cliente existente |
| "La categoría no existe en el sistema" | Al importar CSV, la categoría no fue encontrada | Crea primero la categoría en el módulo Categorías |
| "Credenciales incorrectas" | Usuario o contraseña equivocados | Verifica mayúsculas/minúsculas y vuelve a intentarlo |
| "Acceso Denegado (403)" | Tu rol no tiene permiso para esa acción | Contacta al administrador |

---

## Información del sistema

| Campo | Valor |
|-------|-------|
| Aplicación | MerchStock — Sistema Web de Administración de Inventario |
| Versión | Proyecto Final · 2026 |
| Empresa cliente | MerchStock Perú E.I.R.L. |
| Tecnología | Java 17 · Spring Boot 3.5 · MySQL 8 · Thymeleaf · Bootstrap 5 |
| Desarrollado por | Grupo 3 — Curso Integrador I Sistemas Software, UTP 2026 |
| Contacto soporte | u18208011@utp.edu.pe (Cesar Remuzgo — Project Manager) |

---

*© 2026 MerchStock — Proyecto Final — Grupo 3 — UTP 2026*