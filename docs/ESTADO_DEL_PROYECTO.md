# 📊 Estado del Proyecto MerchStock - APF3

> **Documento vivo.** Se actualiza al cerrar cada módulo o sesión de trabajo.
> Sirve como handoff entre sesiones, evidencia para el equipo y anexo del informe.

---

## 🎯 Información del Proyecto

| Item | Valor |
|---|---|
| **Curso** | Integrador I: Sistemas Software |
| **Sección** | 27667 |
| **Docente** | Mag. Marcos Teodoro Yerren Huima |
| **Universidad** | Universidad Tecnológica del Perú (UTP) |
| **Período** | 2026 |
| **Grupo** | 3 |
| **Entrega APF3** | 13 de junio de 2026 |
| **Repositorio** | https://github.com/CesarRemuzgo/MerchStock |
| **Rama de trabajo** | `develop` (default) |
| **Rama protegida** | `main` (requiere 1 approval) |

---

## 👥 Equipo

| Integrante | Rol | GitHub |
|---|---|---|
| Cesar Gabriel Remuzgo Angeles | Project Manager | @CesarRemuzgo ✅ |
| Luis Fernando Alejos Pérez | Backend Lead | _Pendiente username_ |
| Liz Anyeli Cueva Samillán | Analista / Documentación | _Pendiente username_ |
| Daleth Correa Ángeles | Frontend / UX | @DalethCorreaAngeles ✅ |
| Luis Ernesto Romani Villanueva | QA / DevOps | _Pendiente username_ |

---

## 🏗️ Stack Tecnológico

- **Java:** 17.0.4 LTS
- **Framework:** Spring Boot 3.5.14
- **Build:** Maven
- **BD:** MySQL 8 (BD: `merchstock_db`)
- **ORM:** Spring Data JPA + Hibernate
- **Vistas:** Thymeleaf + Bootstrap 5
- **Seguridad:** Spring Security + BCrypt (configurado, login pendiente)
- **IDE:** VS Code
- **Versionado:** Git + GitHub + GitHub Desktop

### Librerías de la rúbrica APF3 (4/4) ✅

| Librería | Uso en el proyecto |
|---|---|
| Google Guava | `Preconditions.checkNotNull / checkArgument` en services |
| Apache POI | En pom.xml, listo para reportes Excel (pendiente implementar) |
| Apache Commons Lang | `StringUtils.isNotBlank / isBlank` en services |
| Logback | `@Slf4j` en services y controllers + `logback-spring.xml` personalizado |

---

## 📦 Módulos Implementados

### ✅ Completados y funcionales

| Módulo | RFs cubiertos | Pantallas |
|---|---|---|
| **Home** | Vista principal | `/` |
| **Productos CRUD** | RF05, RF06, RF07, RF08 | `/productos`, `/productos/nuevo`, `/productos/{id}/editar`, `/productos/{id}/eliminar` |
| **Alertas de stock** | RF15 | `/productos/alertas` |
| **Categorías CRUD** | Gestión maestra | `/categorias` (5 endpoints) |
| **Clientes CRUD** | RF gestión clientes | `/clientes` (5 endpoints, soporta DNI/RUC/CE/PASAPORTE) |

**Total RFs cubiertos: ~5-6 de 22.**

### ⏳ Pendientes (orden recomendado)

1. **Módulo Usuarios CRUD** (45 min) — Manejo de roles ADMIN/VENDEDOR
2. **Módulo Ventas** ⭐ (90 min) — El más crítico. Aplica defensa en 3 capas con `reducirStock()`
3. **Reportes con Apache POI** (45 min) — Exportar productos a Excel
4. **Login real con BCrypt + roles** (60 min) — Reemplaza el acceso libre temporal
5. _(Opcional)_ Dashboard con estadísticas, importar CSV con Apache Commons

**Meta final:** cubrir 15 de 22 RFs (~70% del alcance).

---

## 🗄️ Base de Datos

Scripts en `database/`:
- `01-schema.sql` — Crea BD y 7 tablas con constraints
- `02-seed-data.sql` — Inserta 4 usuarios, 8 categorías, 16 productos

### Tablas (7)

1. `usuarios` (4 registros seed)
2. `categorias` (8 registros seed: Tazas, Polos, Gorras, Llaveros, Tomatodos, Lapiceros, Bolsas Ecologicas, Lanyards)
3. `productos` (16 registros seed con SKUs TAZ-001..LAN-002)
4. `clientes` (3 registros creados manualmente)
5. `ventas` (vacía)
6. `venta_detalle` (vacía)
7. `auditoria_stock` (vacía)

### Defensa en 3 capas para stock no negativo
- **Capa SQL:** `CHECK (stock_actual >= 0)` en tabla productos
- **Capa Java/Bean Validation:** `@Min(0)` en entity Producto
- **Capa Service:** validación en `ProductoServiceImpl.reducirStock()` con `BusinessException`

---

## 🏛️ Arquitectura
src/main/java/com/merchstock/app/
├── config/        → SecurityConfig (acceso libre temporal + BCrypt bean)
├── controller/    → HomeController, ProductoController, CategoriaController, ClienteController
├── entity/        → 7 entities JPA (Categoria, Usuario, Cliente, Producto, Venta, VentaDetalle, AuditoriaStock)
├── repository/    → 7 repositories Spring Data JPA
├── service/       → Interfaces (ProductoService, CategoriaService, ClienteService)
│   └── impl/      → Implementaciones (SOLID: Interface Segregation + Dependency Inversion)
├── exception/     → ResourceNotFoundException, BusinessException
├── dto/           → (vacío por ahora)
└── util/          → (vacío por ahora)

### Patrones aplicados ✅
- **MVC:** Controller → Service → Repository → Entity → View
- **DAO:** Spring Data JPA con `JpaRepository`
- **SOLID:** Interface Segregation (interfaces + impl) + Dependency Inversion (constructor injection con `@RequiredArgsConstructor`)

---

## 🎨 UI/UX

- **Paleta:** Navy `#1F3864`, Steel Blue `#4472C4`, Teal `#2E8B7A`
- **Framework CSS:** Bootstrap 5 vía CDN
- **Iconos:** Bootstrap Icons
- **Navbar consistente** en todas las vistas: Productos | Categorias | Clientes | Alertas
- **Mensajes flash** con `RedirectAttributes` (verde éxito, rojo error)
- **Footer global** con info del grupo

---

## 📝 Commits realizados (en `develop`)

1. Initial commit
2. Update README.md (info del equipo)
3. docs: agregar guia de contribucion del equipo
4. feat: agregar esqueleto Spring Boot con dependencias APF3
5. feat: implementar modulo de productos completo con MVC, DAO y SOLID
6. docs: agregar scripts SQL e instrucciones de instalacion local
7. feat: agregar modulo categorias CRUD y personalizar Logback
8. feat: agregar modulo clientes CRUD con tipos de documento

---

## 🔐 Configuración local (no en repo)

### `application.properties` (ignorado por Git)
- `spring.datasource.password=` ← password local de MySQL
- `spring.jpa.hibernate.ddl-auto=validate`
- `spring.jpa.open-in-view=false`
- `spring.thymeleaf.cache=false`

### Plantilla compartida en repo
- `application-example.properties` → para que el equipo lo copie y configure su password

---

## ⚠️ Decisiones técnicas importantes

1. **`open-in-view=false`** (mejor práctica) requiere manejo explícito de fetching. La relación `Producto → Categoria` está como `FetchType.EAGER` para evitar `LazyInitializationException` en vistas Thymeleaf.

2. **Eliminación lógica** en todos los módulos (no DELETE físico). Campo `activo = false`.

3. **SKU normalizado a mayúsculas** en `ProductoService.crear()`.

4. **`@Transactional(readOnly = true)`** por defecto en services; se sobreescribe con `@Transactional` para operaciones de escritura.

5. **Password BCrypt** en `usuarios.password_hash` — el hash actual en seed-data NO corresponde a "admin123" real, se debe actualizar al implementar login real.

---

## 🐛 Errores resueltos durante el desarrollo

| Error | Causa | Solución |
|---|---|---|
| `illegal character: '\ufeff'` | BOM en `package-info.java` por PowerShell `Out-File -Encoding UTF8` | Eliminar los 9 `package-info.java` (las carpetas ya tienen archivos reales) |
| `LazyInitializationException` en Categoria | `open-in-view=false` + LAZY fetch en relación | Cambiar `FetchType.LAZY` por `FetchType.EAGER` en Producto.categoria |
| `Unable to access property 'stockBajo'` en Thymeleaf | Ambigüedad con getter `isStockBajo()` (`@Transient boolean`) | Reemplazar `${prod.stockBajo}` por `${prod.stockActual <= prod.stockMinimo}` |
| `Failed to create converter for [%clr]` en Logback | Falta importar defaults de Spring Boot | Agregar `<include resource="org/springframework/boot/logging/logback/defaults.xml"/>` |

---

## 🚀 Cómo retomar el desarrollo

### Pre-requisitos
- MySQL Server corriendo (servicio MySQL80 en Windows)
- VS Code abierto en `F:\GitHub\MerchStock`
- Java 17 disponible

### Arrancar el proyecto
```powershell
cd F:\GitHub\MerchStock
.\mvnw spring-boot:run
```

Verificar en navegador: `http://localhost:8080`

### URLs disponibles
- `/` — Home
- `/productos` — Lista de productos
- `/productos/nuevo` — Nuevo producto
- `/productos/alertas` — Alertas de stock
- `/categorias` — Lista de categorías
- `/clientes` — Lista de clientes

---

## 📅 Próximos pasos (siguiente sesión)

1. ⏳ **Módulo Usuarios CRUD** (Opción A: password editable en texto plano, encriptado con BCrypt al guardar)
2. ⏳ **Módulo Ventas** ⭐ — registro de venta con descuento atómico de stock
3. ⏳ **Reportes Apache POI** — exportar productos a Excel
4. ⏳ **Login real con BCrypt**

---

## 📊 Puntaje proyectado en este momento

| Criterio | Max | Proyección actual |
|---|---|---|
| Diseño de la solución | 3 | 3 / 3 ✅ |
| Uso de recursos Java (4 librerías) | 2 | 2 / 2 ✅ |
| Control de versiones | 3 | 2.5 / 3 (falta más actividad del equipo) |
| Interfaces gráficas | 6 | ~3 / 6 (5-6 RFs cubiertos) |
| Construcción del producto | 4 | 3 / 4 |
| Sustentación oral | 2 | Pendiente |

**Subtotal estimado actual: ~13.5 / 20**
**Meta proyectada al cierre: 17-18 / 20**

---

_Última actualización: [06/07/2026]_
_Mantenido por: Cesar Remuzgo (PM)_