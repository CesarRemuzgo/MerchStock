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
* **`VentaService`:** Servicio transaccional encargado de registrar los flujos de caja. Implementa un principio de *Defense in Depth* para validar el stock en múltiples niveles antes de procesar el cobro y aplicar el cálculo automático del IGV (18%, mediante desglose inverso sobre el total).
* **`ReporteService`:** Utiliza la librería **Apache POI** para estructurar, formatear y exportar dinámicamente libros de trabajo de Excel con los datos consolidados de inventario y ventas del sistema.
* **`ImportacionService`:** Emplea **Apache Commons Lang** para procesar flujos de entrada de archivos CSV, permitiendo la carga e inserción masiva de productos de merchandising directamente en la base de datos de manera segura.
* **`BackupService`:** *(Nuevo — Fase Final)* Genera respaldos de la base de datos mediante `mysqldump` invocado desde Java (`ProcessBuilder`), y gestiona la política de retención de respaldos antiguos.

---

## 3. Mapa de Endpoints Principales

El enrutamiento del backend restringe y mapea el acceso mediante **Spring Security** según el control de acceso basado en roles (RBAC):

### 🔐 Módulo de Autenticación y Seguridad
* `GET /login` : Renderiza la interfaz de inicio de sesión seguro.
* `POST /login` : Procesa las credenciales contrastándolas con el hash de **BCrypt**.
* `POST /logout` : Invalida la sesión actual del usuario de forma segura (se ejecuta vía formulario, no como enlace directo).

### 📊 Módulo de Dashboard e Inventario
* `GET /` o `GET /dashboard` : *(Acceso: ADMIN / VENDEDOR)* Procesa y carga las estadísticas en tiempo real (conteo de productos, alertas activas de stock y total de ingresos).
* `GET /productos` : Muestra la lista de productos y destaca visualmente aquellos en estado crítico de stock.
* `POST /productos/guardar` : *(Acceso: ADMIN)* Registra o actualiza un producto validando sus restricciones físicas.
* `POST /productos/eliminar/{id}` : *(Acceso: ADMIN)* Eliminación lógica de un ítem del catálogo (el producto queda marcado como inactivo, nunca se borra físicamente de la base de datos).

### 🛒 Módulo Transaccional de Ventas
* `GET /ventas/nueva` : Carga el formulario transaccional de venta.
* `POST /ventas/procesar` : Envía el payload con el detalle de la venta para descontar stock y generar el comprobante.

### 📈 Módulo de Reportes e Importación Masiva
* `GET /reportes/productos` : Descarga el reporte de productos generado por Apache POI (.xlsx).
* `GET /reportes/stock-bajo` : Descarga el reporte de productos en alerta de stock bajo.
* `GET /reportes/ventas` : Descarga el reporte del historial de ventas.
* `POST /productos/importar-csv` : *(Acceso: ADMIN)* Recibe el archivo multipart `.csv` para su procesamiento masivo.

### 📡 Módulo de Monitoreo *(Nuevo — Fase Final)*
* `GET /monitoreo` : *(Acceso: ADMIN)* Panel visual que consolida el estado del sistema: tiempo activo, memoria heap, uso de CPU y peticiones HTTP atendidas.
* `GET /actuator/health` : *(Público)* Estado global del sistema (UP, DOWN o ALERTA), incluyendo el detalle de la base de datos, el disco y el indicador de stock crítico.
* `GET /actuator/metrics` : *(Acceso: ADMIN)* Métricas de rendimiento de la JVM.
* `GET /actuator/info` : *(Acceso: ADMIN)* Metadatos de la aplicación.
* `GET /actuator/loggers` : *(Acceso: ADMIN)* Consulta y modificación de los niveles de log en tiempo de ejecución.

### 🛠️ Módulo de Mantenimiento *(Nuevo — Fase Final)*
* `GET /mantenimiento` : *(Acceso: ADMIN)* Panel de respaldos: muestra el listado de backups generados y permite iniciar uno nuevo.
* `POST /mantenimiento/backup` : *(Acceso: ADMIN)* Genera un respaldo manual de la base de datos con `mysqldump`.
* `GET /mantenimiento/descargar/{nombreArchivo}` : *(Acceso: ADMIN)* Descarga un archivo de respaldo específico, con validación anti *path-traversal*.

---

## 4. Tareas Programadas (`@Scheduled`)

Además de los endpoints bajo demanda, el backend ejecuta tareas automáticas sin intervención del usuario:

* **`MonitoreoProgramado`** — cada 15 minutos, registra un *heartbeat* en el log de negocio con el consumo de memoria y la cantidad de productos en alerta.
* **`MantenimientoProgramado`** — genera un respaldo automático diario (2:00 a.m.) y ejecuta la limpieza de respaldos que superan la política de retención (3:00 a.m.).

---

## 5. Consideraciones de Seguridad

* Las contraseñas se almacenan cifradas con **BCrypt** (cost-factor 10), nunca en texto plano.
* El acceso a rutas sensibles (`/usuarios/**`, `/categorias/**`, `/actuator/**`, `/monitoreo/**`, `/mantenimiento/**`) está restringido al rol **ADMIN** mediante `SecurityConfig`, aplicando el principio de mínimo privilegio.
* Las consultas a la base de datos se realizan mediante Spring Data JPA con parámetros, previniendo inyección SQL.
* El sistema fue evaluado con **OWASP ZAP**; los hallazgos identificados (ausencia de tokens CSRF, cabecera CSP no configurada, entre otros) están documentados con su plan de remediación en `PLAN_PRUEBAS.md`.