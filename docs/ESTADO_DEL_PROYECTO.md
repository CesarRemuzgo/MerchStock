# 📋 Estado del Proyecto MerchStock

> **Sistema web de gestión de inventario para MerchStock Perú E.I.R.L.**
> **PROYECTO FINAL** — Semana 18 · UTP 2026

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

> ⚠️ **PENDIENTE DE UNIFICAR:** el Informe APF3 declara **15 de 22 RF** cubiertos,
> pero este documento y el PPT dicen **~14 de 22**. Antes de la entrega final hay
> que verificar el número real y dejarlo idéntico en el informe, el PPT y aquí.
> La rúbrica califica **coherencia entre documentación y código**.

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

| Criterio | Puntos | Estado | Qué falta |
|----------|--------|--------|-----------|
| **Pruebas de software y seguridad** | 2 | 🟡 50% | Tests ✅ · Falta **reporte de pruebas de seguridad (OWASP ZAP)** con observaciones levantadas |
| **Despliegue del proyecto** | 2 | 🔴 0% | Empaquetado Maven, configuración de servidor y **Plan de Despliegue** |
| **Monitoreo del proyecto** | 6 | 🟡 En curso | Actuator + health/metrics + logs + **Plan de Monitoreo** |
| **Mantenimiento del proyecto** | 5 | 🔴 0% | Cron jobs, backups, scripts y **Plan de Mantenimiento** |
| **Construcción del producto final** | 3 | 🟢 ~100% | Completo, coherente, buenas prácticas y autoría ✅ |
| **Sustentación oral** | 2 | ⚪ Pendiente | Preparar PPT final y guion (todos exponen, ~10 min) |
| **TOTAL** | **20** | **~5.5 / 20** | 🚨 **13 pts dependen de trabajo aún no iniciado** |

> 🚨 **PRIORIDAD MÁXIMA:** Monitoreo (6) + Mantenimiento (5) = **11 puntos**.
> Son los rubros de mayor peso y los menos avanzados. La mayor parte es
> **configuración + documentación**, no reprogramar el sistema.

---

## 🏗️ Stack Técnico

### Backend
- **Lenguaje:** Java 17.0.4 LTS
- **Framework:** Spring Boot 3.5.14
- **Build tool:** Maven
- **Base de datos:** MySQL 8 (BD: `merchstock_db`)
- **ORM:** Spring Data JPA / Hibernate
- **Seguridad:** Spring Security + BCrypt (cost-factor 10)
- **Testing:** JUnit 5 + Mockito
- **Monitoreo:** Spring Boot Actuator + Micrometer *(en implementación)*

### Frontend
- **Templating:** Thymeleaf
- **CSS:** Bootstrap 5 (vía CDN) · **Iconos:** Bootstrap Icons
- **JS:** Vanilla JavaScript para carrito dinámico

### Estructura de paquetes
```
com.merchstock.app
├── config       (SecurityConfig)
├── controller
├── entity       ← las entidades viven aquí (NO en 'model')
├── repository
├── service
│   └── impl
├── monitoreo    ← nuevo (Actuator)
└── util
```

### Librerías de Apoyo (exigidas por rúbrica)

| Librería | Uso real |
|----------|----------|
| ✅ **Google Guava 33.3.1** | `Preconditions.checkNotNull/checkArgument` en services |
| ✅ **Apache POI 5.3.0** | `XSSFWorkbook` para reportes Excel |
| ✅ **Apache Commons Lang 3.17.0** | `StringUtils` en services e importación CSV |
| ✅ **Logback (vía Spring)** | `@Slf4j` + `logback-spring.xml` |

---

## 📦 Repositorio

| Campo | Valor |
|-------|-------|
| **URL** | https://github.com/CesarRemuzgo/MerchStock |
| **Visibilidad** | Público · Licencia MIT |
| **Rama principal** | `main` (protegida con 1 approval) |
| **Rama de trabajo** | `develop` (default) |
| **Ruta local** | `F:\GitHub\MerchStock` |

---

## ✅ Módulos Implementados (10 completos)

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

### Lógica del IGV (¡NO REVERTIR!)
El precio **ya incluye IGV**. El desglose se hace hacia atrás:
```
total     = suma de subtotales
opGravada = total / 1.18
igv       = total − opGravada     ← por RESTA, garantiza exactitud al céntimo
```
El campo `subtotal` en BD almacena la **operación gravada**.

---

## 🚧 TRABAJO PENDIENTE PARA EL PROYECTO FINAL

### 1️⃣ Monitoreo (6 pts) — 🟡 EN CURSO

**Código:**
- [ ] Dependencia `spring-boot-starter-actuator` en `pom.xml`
- [ ] Configuración de endpoints en `application.properties`
- [ ] `StockCriticoHealthIndicator` (health de negocio: alerta si ≥5 productos en stock mínimo)
- [ ] 2 queries nuevas en `ProductoRepository` (`contarProductosEnAlerta`, `contarProductosSinStock`)
- [ ] Proteger `/actuator/**` y `/monitoreo/**` con rol ADMIN en `SecurityConfig`
- [ ] `MonitoreoController` + vista `templates/monitoreo/index.html` (panel visual)
- [ ] `logback-spring.xml` con **rotación** + log de errores + log de negocio
- [ ] Logs de eventos de negocio (`VENTA_REGISTRADA`, `STOCK_CRITICO`)
- [ ] `MonitoreoProgramado` con `@Scheduled` (heartbeat cada 15 min) + `@EnableScheduling`

**Informe:**
- [ ] Sección 5: **Plan de Monitoreo** (objetivos, arquitectura en 3 niveles, endpoints, política de retención de logs, **matriz de indicadores/umbrales/acciones**, responsabilidades)
- [ ] Capturas: `/actuator/health`, panel `/monitoreo`, `/actuator/metrics`, log de negocio

---

### 2️⃣ Mantenimiento (5 pts) — 🔴 NO INICIADO

**Código:**
- [ ] Script de backup con `mysqldump`
- [ ] Backup automatizado con `@Scheduled` (o cron job del sistema)
- [ ] Script de limpieza de logs antiguos
- [ ] Vista/endpoint para descargar respaldo (RF21)

**Informe:**
- [ ] **Plan de Mantenimiento** (preventivo, correctivo, evolutivo, calendario, responsables)

---

### 3️⃣ Pruebas de Seguridad (completa los 2 pts) — 🔴 NO INICIADO

- [ ] Ejecutar **OWASP ZAP** contra MerchStock en local
- [ ] Documentar hallazgos → severidad → **corrección aplicada** (= "observaciones levantadas")
- [ ] Defender los controles ya existentes: BCrypt, RBAC, consultas parametrizadas (anti-SQLi)
- [ ] Sección de **Reporte de Pruebas de Seguridad** en el informe

---

### 4️⃣ Despliegue (2 pts) — 🔴 NO INICIADO

- [ ] `.\mvnw clean package` → JAR ejecutable
- [ ] Perfil `application-prod.properties`
- [ ] Desplegar en servidor (Render / Railway / AWS free tier)
- [ ] **Plan de Despliegue** en el informe + evidencia de la app corriendo

---

### 5️⃣ Detalles del Informe — 🟡 POR CORREGIR

- [ ] Rellenar los `[CÓDIGO]` de los integrantes (están vacíos):
  - Remuzgo Ángeles, César Gabriel · **U18208011**
  - Alejos Pérez, Luis Fernando · **U23247845**
  - Correa Ángeles, Daleth · **U23225481**
  - Cueva Samillán, Liz Anyeli · **U21321742**
  - Romani Villanueva, Luis Ernesto · **0616158**
- [ ] Generar el **Índice General** (está vacío)
- [ ] Insertar las ~20 imágenes marcadas con `📷 INSERTAR AQUÍ`
- [ ] Unificar el número de RFs cubiertos (15 vs ~14) en informe, PPT y este archivo

---

### 6️⃣ Sustentación Oral (2 pts)

- [ ] PPT final del proyecto completo
- [ ] Guion por integrante (**todos deben exponer**)
- [ ] Duración máxima: ~10 minutos por grupo
- [ ] Cubrir: arquitectura, pruebas, despliegue, monitoreo y mantenimiento
- [ ] Ensayo general con el equipo

---

## 💡 Ideas de Valor Agregado (opcionales, para destacar)

| Idea | Impacto | Esfuerzo |
|------|---------|----------|
| Panel de monitoreo en vivo (Actuator) | 🥇 Alto — ataca los 6 pts | Bajo-Medio |
| App desplegada con URL pública | 🥇 Alto — el profe la abre en su celular | Medio |
| Alertas automáticas por correo/Telegram al bajar el stock | 🥇 Alto | Medio |
| Predicción de reposición de stock (media móvil) | 🥈 Muy alto "wow" | Medio |
| CI/CD con GitHub Actions (tests automáticos en cada push) | 🥈 Alto | Bajo |
| Códigos QR / barras para registrar ventas escaneando | 🥉 Vistoso en demo | Medio |

---

## 🔑 Credenciales de Prueba

| Username | Password | Rol |
|----------|----------|-----|
| `admin` | `Admin2026` | ADMIN |
| `cremuzgo` | `Cesar2026` | ADMIN |
| `pventa1` | `Patricia2026` | VENDEDOR |
| `avendedor` | `Andrea2026` | VENDEDOR |
| `falejos` | `BackEnd2026` | ADMIN |

> ⚠️ Credenciales de demo. `application.properties` está en `.gitignore`.

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

---

## 🔧 Comandos Útiles

```powershell
cd F:\GitHub\MerchStock

.\mvnw spring-boot:run              # Arrancar el sistema
.\mvnw clean compile                # Compilar
.\mvnw test                         # Correr las 7 pruebas unitarias
.\mvnw clean package                # Generar el JAR (despliegue)
.\mvnw javadoc:javadoc              # Javadoc → target/reports/apidocs/
```

**Endpoints de monitoreo** (una vez implementados, como ADMIN):
```
http://localhost:8080/actuator/health
http://localhost:8080/actuator/metrics
http://localhost:8080/monitoreo
```

---

## 🎓 Conceptos Clave para la Sustentación

**Defensa en 3 capas (stock no negativo)**
`CHECK (stock_actual >= 0)` en BD + `@Min(0)` en la entity + `BusinessException` en el service.

**Transaccionalidad** — `@Transactional` en `registrarVenta()` garantiza atomicidad: si algo falla, rollback total (stock, venta y auditoría). No quedan estados parciales.

**BCrypt** — hash unidireccional, cost-factor 10. Nunca se desencripta; se compara.

**RBAC** — cada URL mapeada a un rol, aplicando mínimo privilegio.

**Monitoreo** — 3 ejes: **logs** (trazabilidad), **performance tools** (memoria/CPU) y **health tools** (disponibilidad). Nuestro diferencial: el health indicator también responde *"¿el inventario está sano?"*, no solo *"¿el sistema está vivo?"*.

**MVC + DAO + SOLID** — Controller → Service → Repository → Entity → View, con inyección por constructor (DIP).

---

## 📞 Contacto

**Project Manager:** Cesar Gabriel Remuzgo Ángeles
**Email:** u18208011@utp.edu.pe · **GitHub:** @CesarRemuzgo

---

_Última actualización: 14 de julio de 2026_
_Fase actual: **Proyecto Final** — implementando Monitoreo_
_Días restantes para la sustentación: **11**_