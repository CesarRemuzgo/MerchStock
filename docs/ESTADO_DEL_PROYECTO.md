# 📋 Estado del Proyecto MerchStock

> **Sistema web de gestión de inventario para MerchStock Perú E.I.R.L.**
> **PROYECTO FINAL** — Fase de cierre · UTP 2026

---

## 🎯 Información General

| Campo | Valor |
|-------|-------|
| **Curso** | Integrador I Sistemas Software, Sección 27667 |
| **Universidad** | UTP (Universidad Tecnológica del Perú) |
| **Profesor** | Mag. Marcos Yerren Huima |
| **Grupo** | Grupo 3 |
| **Cliente ficticio** | MerchStock Perú E.I.R.L. |
| **Dirección cliente** | Av. Carlos Izaguirre 845, Los Olivos, Lima |
| **Rubro** | Venta de merchandise (tazas, polos, gorras, llaveros, etc.) |
| **Presupuesto** | S/15,000 (corregido por el profesor) |
| **Alcance comprometido** | 15 de 22 RFs (estrategia conservadora 70%) |
| **Entrega APF3** | ✅ Viernes 13 de junio de 2026 (COMPLETADA) |
| **SUSTENTACIÓN FINAL** | 🔴 **Sábado 25 de julio de 2026** |

---

## 👥 Equipo Grupo 3

| Nombre | Rol | GitHub |
|--------|-----|--------|
| Cesar Gabriel Remuzgo Ángeles | Project Manager | @CesarRemuzgo |
| Luis Fernando Alejos Pérez | Backend Lead | @Luis-1bit |
| Liz Anyeli Cueva Samillán | Analista/Documentación | @LizAnyel / @AnyelCueva |
| Daleth Correa Ángeles | Frontend/UX | @DalethCorreaAngeles |
| Luis Ernesto Romani Villanueva | QA/DevOps | @LuisErnestoRomani |

---

## 🏆 RÚBRICA DEL PROYECTO FINAL (20 pts) — Estado actual

| Criterio | Puntos | Estado | Detalle |
|----------|--------|--------|---------|
| **Pruebas de software y seguridad** | 2 | 🟢 100% | Tests ✅ · Escaneo OWASP ZAP ejecutado, 6 hallazgos documentados y priorizados (sin riesgo Alto/Crítico) |
| **Despliegue del proyecto** | 2 | 🟢 100% | `.jar` empaquetado, ejecución independiente verificada, puerto expuesto en todas las interfaces (`0.0.0.0:8080`) |
| **Monitoreo del proyecto** | 6 | 🟢 100% | Actuator + health de negocio (StockCriticoHealthIndicator) + panel `/monitoreo` + Logback con 3 destinos + heartbeat `@Scheduled` |
| **Mantenimiento del proyecto** | 5 | 🟢 100% | Backups automatizados (`@Scheduled`) + panel manual `/mantenimiento` + política de retención de 30 días |
| **Construcción del producto final** | 3 | 🟢 100% | Completo, coherente, buenas prácticas y autoría ✅ |
| **Sustentación oral** | 2 | 🔴 Pendiente | Preparar PPT final y guion (todos exponen, ~10 min) |
| **TOTAL** | **20** | **18 / 20 asegurados** | 🎯 Solo falta la Sustentación Oral |

> ✅ **Los 4 módulos de mayor peso (Monitoreo, Mantenimiento, Despliegue, Seguridad = 15 pts) están completos y documentados.**
> Todo el detalle técnico, capturas y matrices de responsabilidad están en `MerchStock_Fase_Final_Completa.docx`.

---

## 🏗️ Stack Técnico

### Backend
- **Lenguaje:** Java 17.0.4 LTS
- **Framework:** Spring Boot 3.5.14
- **Build tool:** Maven
- **Base de datos:** MySQL 8 (BD: `merchstock_db`)
- **ORM:** Spring Data JPA / Hibernate
- **Seguridad:** Spring Security + BCrypt (cost-factor 10)
- **Testing:** JUnit 5 + Mockito (7 pruebas: 1 `contextLoads()` + 6 de negocio)
- **Monitoreo:** Spring Boot Actuator + Micrometer ✅
- **Mantenimiento:** BackupService con `mysqldump` vía ProcessBuilder ✅
- **Seguridad activa:** OWASP ZAP 2.17.0 ✅

### Frontend
- **Templating:** Thymeleaf
- **CSS:** Bootstrap 5 (vía CDN) · **Iconos:** Bootstrap Icons
- **JS:** Vanilla JavaScript para carrito dinámico

### Estructura de paquetes
```
com.merchstock.app
├── config          (SecurityConfig)
├── controller
├── entity          ← las entidades viven aquí (NO en 'model')
├── repository
├── service
│   └── impl
├── monitoreo       (Actuator, panel, tareas programadas)
├── mantenimiento   (BackupService, panel, tareas programadas)
└── util
```

### Librerías de Apoyo (exigidas por rúbrica)

| Librería | Uso real |
|----------|----------|
| ✅ **Google Guava 33.3.1** | `Preconditions.checkNotNull/checkArgument` en services |
| ✅ **Apache POI 5.3.0** | `XSSFWorkbook` para reportes Excel |
| ✅ **Apache Commons Lang 3.17.0** | `StringUtils` en services e importación CSV |
| ✅ **Logback (vía Spring)** | `@Slf4j` + `logback-spring.xml` con 3 destinos (general, errores, negocio) |

---

## 📦 Repositorio

| Campo | Valor |
|-------|-------|
| **URL** | https://github.com/CesarRemuzgo/MerchStock |
| **Visibilidad** | Público · Licencia MIT |
| **Rama principal** | `main` (protegida con 1 approval) — ✅ contiene el snapshot estable del Proyecto Final |
| **Rama de trabajo** | `develop` (default) |
| **Ruta local** | `F:\GitHub\MerchStock` |

---

## ✅ Módulos Implementados (12 completos)

1. **Home Dashboard** — 4 tarjetas de estadísticas en tiempo real + tabla de últimas ventas
2. **Productos CRUD + Alertas (RF15)** — buscador, eliminación lógica, alertas de stock bajo
3. **Categorías CRUD** — validación de nombre duplicado
4. **Clientes CRUD** — DNI/RUC/CE/PASAPORTE, reactivación automática
5. **Usuarios CRUD con BCrypt** — cost-factor 10, protección del último ADMIN
6. **Ventas Transaccional** ⭐ — carrito JS, IGV 18%, código `VTA-YYYY-NNNN`, defensa en 3 capas, auditoría, anulación con reversa, `@Transactional`
7. **Reportes Excel (Apache POI)** — productos, stock bajo y ventas en `.xlsx`
8. **Autenticación + Roles (Spring Security)** — RBAC ADMIN/VENDEDOR, login, 403
9. **Importación masiva CSV** — validación con Commons Lang + Guava
10. **Pruebas Unitarias (JUnit 5 + Mockito)** — 7 tests en verde (BUILD SUCCESS)
11. **Monitoreo** ⭐ — Actuator, health de negocio, panel visual, logs estructurados, heartbeat programado
12. **Mantenimiento** ⭐ — backups automatizados y manuales, panel administrativo, política de retención

### Lógica del IGV (¡NO REVERTIR!)
El precio **ya incluye IGV**. El desglose se hace hacia atrás:
```
total     = suma de subtotales
opGravada = total / 1.18
igv       = total − opGravada     ← por RESTA, garantiza exactitud al céntimo
```
El campo `subtotal` en BD almacena la **operación gravada**.

---

## ✅ TRABAJO COMPLETADO — Fase Final

### 1️⃣ Monitoreo (6 pts) — 🟢 COMPLETO

- ✅ `spring-boot-starter-actuator` integrado
- ✅ `StockCriticoHealthIndicator` (health de negocio: ALERTA si ≥5 productos en stock mínimo)
- ✅ `/actuator/**` y `/monitoreo/**` protegidos con rol ADMIN
- ✅ `MonitoreoController` + panel visual `/monitoreo` (tiempo activo, memoria heap, CPU, peticiones HTTP)
- ✅ `logback-spring.xml` con 3 destinos (general, errores, negocio) y rotación
- ✅ Logger `NEGOCIO` con eventos `VENTA_REGISTRADA` y `HEARTBEAT`
- ✅ `MonitoreoProgramado` con `@Scheduled` (heartbeat cada 15 min)
- ✅ Plan de Monitoreo documentado (objetivos, arquitectura en 3 niveles, matriz de indicadores/umbrales/acciones, responsabilidades)

### 2️⃣ Mantenimiento (5 pts) — 🟢 COMPLETO

- ✅ `BackupService` con `mysqldump` vía `ProcessBuilder`
- ✅ `MantenimientoProgramado`: backup automático diario (2:00 a.m.) + limpieza de retención (3:00 a.m.)
- ✅ Panel `/mantenimiento` (backup manual, lista y descarga de respaldos)
- ✅ `/mantenimiento/**` protegido con rol ADMIN
- ✅ Carpeta `backups/` excluida del control de versiones (`.gitignore`)
- ✅ Plan de Mantenimiento documentado (política de retención, seguridad de respaldos, responsabilidades)

### 3️⃣ Pruebas de Seguridad (2 pts) — 🟢 COMPLETO

- ✅ Escaneo ejecutado con **OWASP ZAP 2.17.0** contra el sistema desplegado
- ✅ 6 hallazgos identificados y mapeados a **OWASP Top 10:2025** (CSP, SRI, CSRF, SameSite, 2 informativos)
- ✅ Sin hallazgos de riesgo Alto o Crítico
- ✅ Priorización de riesgos (Riesgo = Probabilidad × Impacto)
- ✅ Confirma con evidencia real una deuda técnica ya documentada (CSRF deshabilitado en `SecurityConfig`)
- ✅ Reporte completo documentado con plan de remediación

### 4️⃣ Despliegue (2 pts) — 🟢 COMPLETO

- ✅ `.\mvnw clean package` → JAR ejecutable (`merchstock-app-0.0.1-SNAPSHOT.jar`)
- ✅ Ejecución verificada como proceso independiente del IDE (`java -jar`)
- ✅ Puerto verificado escuchando en todas las interfaces (`0.0.0.0:8080`)
- ✅ Regla de firewall configurada y verificada (`MerchStock-8080`, Allow, Any)
- ✅ Conectividad de red confirmada entre equipos (`ping`, 0% pérdida)
- 🟡 **Incidencia documentada:** el acceso remoto vía navegador no se completó de forma consistente en las pruebas realizadas; se aisló la causa probable a un conflicto entre el Firewall de Windows y el antivirus ESET NOD32 del equipo servidor. No invalida el despliegue (evidencia técnica de exposición en red ya confirmada); queda como punto de seguimiento antes de la sustentación si se desea demo en vivo entre dispositivos.
- ✅ Plan de Despliegue documentado con evidencia completa

### 5️⃣ Limpieza de código — 🟢 COMPLETO

- ✅ `pom.xml`: eliminada la dependencia duplicada `thymeleaf-extras-springsecurity6`
- ✅ `ProductoRepository`: unificados los métodos duplicados de conteo de stock bajo (`contarProductosEnAlerta()` como versión única)
- ✅ Confirmado: 7 tests = 1 `contextLoads()` + 6 pruebas de negocio (`ProductoServiceImplTest` × 5, `VentaServiceImplTest` × 1)

### 6️⃣ Documentación — 🟢 COMPLETO

- ✅ `[CÓDIGO]` de los 5 integrantes completados en el Informe
- ✅ Número de RFs cubiertos unificado en **15 de 22** en todos los documentos
- ✅ `MANUAL_USUARIO.md`: corregida la explicación del cálculo de IGV (desglose inverso, no "18% sobre subtotal")
- ✅ `PLAN_PRUEBAS.md`: corregido Java 21→17, agregada sección de Pruebas de Seguridad, "APF3"→"Proyecto Final"
- ✅ `BACKEND.md`: endpoints alineados con el código real (`/reportes/productos`, `/reportes/stock-bajo`, `/reportes/ventas`; `POST /logout`; eliminación lógica explícita)
- ✅ README de base de datos: credenciales actualizadas (5 usuarios reales, no 4; passwords vigentes, no `admin123`)
- ✅ Merge de `develop` a `main` completado (33 commits, aprobado por Daleth Correa, mergeado por Luis Ernesto Romani) — evidencia de trabajo en equipo y control de versiones

---

## 🔴 PENDIENTE ÚNICO

### Sustentación Oral (2 pts)

- [ ] PPT final del proyecto completo (diseño institucional UTP)
- [ ] Guion por integrante (**todos deben exponer**, ~10 min total)
- [ ] Definir división de la exposición entre los 5 integrantes
- [ ] Decidir formato de demo: en vivo, con capturas de respaldo, o combinación
- [ ] Ensayo general con el equipo

### Opcional (no afecta la calificación, mejora la experiencia de demo)

- [ ] Resolver el conflicto Firewall/ESET NOD32 para permitir demo en vivo de acceso remoto entre dispositivos
- [ ] Agregar navbar completo a los paneles `/monitoreo` y `/mantenimiento` (actualmente solo tienen botón "Volver")
- [ ] Agregar `sec:authorize` a los links de Categorías/Usuarios en el navbar para VENDEDOR (actualmente visibles pero devuelven 403 al hacer clic)
- [ ] Implementar la remediación de CSRF identificada en el escaneo de seguridad (reactivar protección CSRF en `SecurityConfig`)

---

## 🔑 Credenciales de Prueba

| Username | Password | Rol |
|----------|----------|-----|
| `admin` | `Admin2026` | ADMIN |
| `cremuzgo` | `Cesar2026` | ADMIN |
| `falejos` | `BackEnd2026` | ADMIN |
| `pventa1` | `Patricia2026` | VENDEDOR |
| `avendedor` | `Andrea2026` | VENDEDOR |

> ⚠️ Credenciales de demo, vigentes y verificadas. `application.properties` está en `.gitignore`.
> La pantalla de login **ya no muestra** estas credenciales en pantalla (se corrigió por seguridad antes del despliegue).

---

## 🐛 Bugs Resueltos (aprendizajes)

| # | Problema | Solución |
|---|----------|----------|
| 1 | `LazyInitializationException` al ver detalle de venta | `FetchType.LAZY` → `EAGER` en Venta.cliente, Venta.usuario, VentaDetalle.producto |
| 2 | BOM en archivos `.java` creados con PowerShell | Usar `New-Item`, no `Out-File -Encoding UTF8` |
| 3 | Bug en `renderCarrito()` JS | No destruir elementos referenciados con `innerHTML`; regenerar dentro del container |
| 4 | Dropdown de usuario no abría | Faltaba `bootstrap.bundle.min.js` antes de `</body>` — **revisar en TODA vista nueva** |
| 5 | `BusinessException` no detectaba stock insuficiente | Validación explícita en el service ANTES del `save()` |
| 6 | Carpeta `test` no aparecía en VS Code | `mkdir [ruta] -Force` antes de `New-Item` |
| 7 | `Property 'nombre' cannot be found on Cliente` | El campo real es `nombreCompleto` — revisar la entity antes de generar vistas |
| 8 | `BUILD FAILURE` en Javadoc | Evitar el símbolo `<` en comentarios Javadoc |
| 9 | Backup manual daba error de acceso al `.jar` | Verificar carpeta activa en PowerShell (`cd F:\GitHub\MerchStock`) antes de correr comandos relativos |
| 10 | Acceso remoto entre dispositivos fallaba pese a firewall configurado | Aislado a conflicto entre Windows Firewall y ESET NOD32; pendiente de resolución definitiva |

---

## 🔧 Comandos Útiles

```powershell
cd F:\GitHub\MerchStock

.\mvnw spring-boot:run              # Arrancar el sistema (modo desarrollo)
.\mvnw clean compile                # Compilar
.\mvnw test                         # Correr las 7 pruebas unitarias
.\mvnw clean package                # Generar el JAR (despliegue)
java -jar target\merchstock-app-0.0.1-SNAPSHOT.jar   # Correr el JAR standalone
.\mvnw javadoc:javadoc              # Javadoc → target/reports/apidocs/
```

**Endpoints de monitoreo** (como ADMIN):
```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/metrics
http://localhost:8080/monitoreo
```

**Endpoint de mantenimiento** (como ADMIN):
```
http://localhost:8080/mantenimiento
```

---

## 🎓 Conceptos Clave para la Sustentación

**Defensa en 3 capas (stock no negativo)**
`CHECK (stock_actual >= 0)` en BD + `@Min(0)` en la entity + `BusinessException` en el service.

**Transaccionalidad** — `@Transactional` en `registrarVenta()` garantiza atomicidad: si algo falla, rollback total (stock, venta y auditoría). No quedan estados parciales.

**BCrypt** — hash unidireccional, cost-factor 10. Nunca se desencripta; se compara.

**RBAC** — cada URL mapeada a un rol, aplicando mínimo privilegio.

**Monitoreo** — 3 ejes: **logs** (trazabilidad), **performance tools** (memoria/CPU) y **health tools** (disponibilidad). Nuestro diferencial: el health indicator también responde *"¿el inventario está sano?"*, no solo *"¿el sistema está vivo?"*.

**Mantenimiento** — respaldo automatizado (`@Scheduled`) + respaldo manual bajo demanda, con política de retención de 30 días y exclusión de `backups/` del control de versiones por seguridad.

**Despliegue** — empaquetado como JAR autocontenido (Spring Boot fat jar), verificado como proceso independiente del entorno de desarrollo, con exposición confirmada en todas las interfaces de red.

**Pruebas de Seguridad** — escaneo activo con OWASP ZAP, no solo revisión de código; el hallazgo de CSRF demuestra coherencia entre la deuda técnica documentada y su verificación dinámica.

**MVC + DAO + SOLID** — Controller → Service → Repository → Entity → View, con inyección por constructor (DIP).

---

## 📞 Contacto

**Project Manager:** Cesar Gabriel Remuzgo Ángeles
**Email:** u18208011@utp.edu.pe · **GitHub:** @CesarRemuzgo

---

_Última actualización: 16 de julio de 2026_
