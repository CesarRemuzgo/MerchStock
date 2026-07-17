# Documentación del Backend - MerchStock

Este documento técnico detalla la arquitectura del lado del servidor, los servicios de negocio principales y el mapa de endpoints que gobiernan el sistema web de administración de inventarios para **MerchStock Perú E.I.R.L.**

## 1. Arquitectura y Capas del Servidor (Spring Boot 3.5)
El backend está construido sobre **Java 17** y **Spring Boot 3.5**, siguiendo el patrón arquitectónico **MVC (Modelo-Vista-Controlador)**. La lógica de la aplicación se desacopla estrictamente en tres capas para cumplir con los principios SOLID:

1. **Capa de Controladores (`@Controller`):** Gestiona las peticiones HTTP, valida los datos de entrada y coordina la respuesta enviando los modelos a las vistas de Thymeleaf.
2. **Capa de Servicios (`@Service`):** Contiene la lógica de negocio central, la gestión transaccional (`@Transactional`) y las reglas de validación (como el descuento automático de existencias).
3. **Capa de Persistencia / Repositorios (`@Repository`):** Implementa el patrón DAO mediante Spring Data JPA para la comunicación directa con el motor relacional MySQL 8.

---

## 2. Servicios Principales de Negocio

El núcleo del backend expone servicios optimizados con librerías de la rúbrica (Google Guava, Apache POI, Apache Commons):

* **`ProductoService`:** Gobierna el CRUD de productos. Incorpora la lógica de **Alertas de Stock Bajo**, disparando notificaciones al Dashboard en tiempo real cuando la cantidad actual es menor o igual al stock mínimo configurado.
* **`VentaService`:** Servicio transaccional encargado de registrar los flujos de caja. Implementa un principio de *Defense in Depth* para validar el stock en múltiples niveles antes de procesar el cobro y aplicar el cálculo automático del IGV (18%).
* **`ReporteService`:** Utiliza la librería **Apache POI** para estructurar, formatear y exportar dinámicamente libros de trabajo de Excel con los datos consolidados de inventario y ventas del sistema.
* **`ImportacionService`:** Emplea **Apache Commons Lang** para procesar flujos de entrada de archivos CSV, permitiendo la carga e inserción masiva de productos de merchandising directamente en la base de datos de manera segura.

---

## 3. Mapa de Endpoints Principales

El enrutamiento del backend restringe y mapeo el acceso mediante **Spring Security** según el control de acceso basado en roles (RBAC):

### 🔐 Módulo de Autenticación y Seguridad
* `GET /login` : Renderiza la interfaz de inicio de sesión seguro.
* `POST /login` : Procesa las credenciales contrastándolas con el hash de **BCrypt**.
* `GET /logout` : Invalida la sesión actual del usuario de forma segura.

### 📊 Módulo de Dashboard e Inventario
* `GET /` o `GET /dashboard` : *(Acceso: ADMIN / VENDEDOR)* Procesa y carga las estadísticas en tiempo real (conteo de productos, alertas activas de stock y total de ingresos).
* `GET /productos` : Muestra la lista de productos y destaca visualmente aquellos en estado crítico de stock.
* `POST /productos/guardar` : *(Acceso: ADMIN)* Registra o actualiza un producto validando sus restricciones físicas.
* `POST /productos/eliminar/{id}` : *(Acceso: ADMIN)* Eliminación lógica o física de un ítem del catálogo.

### 🛒 Módulo Transaccional de Ventas
* `GET /ventas/nueva` : Carga el formulario transaccional de venta.
* `POST /ventas/procesar` : Envía el payload con el detalle de la venta para descontar stock y generar el comprobante.

### 📈 Módulo de Reportes e Importación Masiva
* `GET /reportes/excel/productos` : Descarga el reporte generado por Apache POI.
* `POST /productos/importar-csv` : *(Acceso: ADMIN)* Recibe el archivo multipart `.csv` para su procesamiento masivo.