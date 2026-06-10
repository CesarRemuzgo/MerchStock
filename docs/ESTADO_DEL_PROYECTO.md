# 📋 Estado del Proyecto MerchStock

> **Sistema web de gestión de inventario para MerchStock Perú E.I.R.L.**
> Avance de Proyecto Final 3 (APF3) - UTP 2026

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
| **Fecha entrega APF3** | Viernes 13 de junio de 2026 |

---

## 👥 Equipo Grupo 3

| Nombre | Rol | GitHub |
|--------|-----|--------|
| Cesar Gabriel Remuzgo Ángeles | Project Manager | @CesarRemuzgo |
| Luis Fernando Alejos Pérez | Backend Lead | (pendiente) |
| Liz Anyeli Cueva Samillán | Analista/Documentación | (pendiente) |
| Daleth Correa Ángeles | Frontend/UX | @DalethCorreaAngeles |
| Luis Ernesto Romani Villanueva | QA/DevOps | (pendiente) |

---

## 🏗️ Stack Técnico

### Backend
- **Lenguaje:** Java 17.0.4 LTS
- **Framework:** Spring Boot 3.5.14
- **Build tool:** Maven
- **Base de datos:** MySQL 8 (BD: `merchstock_db`)
- **ORM:** Spring Data JPA / Hibernate
- **Seguridad:** Spring Security + BCrypt (cost-factor 10)

### Frontend
- **Templating:** Thymeleaf
- **CSS:** Bootstrap 5 (vía CDN)
- **Iconos:** Bootstrap Icons (vía CDN)
- **JS:** Vanilla JavaScript para carrito dinámico

### Herramientas
- **IDE:** VS Code (consensuado por equipo)
- **Control de versiones:** Git + GitHub
- **Paleta de colores:** Navy `#1F3864`, Steel Blue `#4472C4`, Teal `#2E8B7A`
- **IGV peruano:** 18%

### Librerías de Apoyo (Rúbrica APF3)
Las 4 librerías exigidas por la rúbrica están USÁNDOSE EN CÓDIGO:

| Librería | Uso real |
|----------|----------|
| ✅ **Google Guava 33.3.1** | `Preconditions.checkNotNull/checkArgument` en services |
| ✅ **Apache POI 5.3.0** | `XSSFWorkbook` para generación de reportes Excel |
| ✅ **Apache Commons Lang 3.17.0** | `StringUtils.isBlank/isNotBlank` en services |
| ✅ **Logback (vía Spring)** | `@Slf4j` + `logback-spring.xml` con 3 appenders |

---

## 📦 Repositorio

| Campo | Valor |
|-------|-------|
| **URL** | https://github.com/CesarRemuzgo/MerchStock |
| **Visibilidad** | Público |
| **Licencia** | MIT |
| **Rama principal** | `main` (protegida con 1 approval) |
| **Rama de trabajo** | `develop` (default) |
| **Ruta local** | `F:\GitHub\MerchStock` |

### Configuración de seguridad del repo
- ✅ 2FA habilitado en cuenta Cesar
- ✅ `application.properties` en .gitignore
- ✅ `application-example.properties` como plantilla para el equipo
- ✅ Directorio `/logs/` en .gitignore
- ✅ `/target/` en .gitignore

---

## ✅ Módulos Implementados (7 completos)

### 1. Home Dashboard
- Pantalla principal con cards de acceso rápido
- Contador de alertas de stock bajo
- Navbar con 7 enlaces consistentes en todo el sistema

### 2. Productos CRUD + Alertas (RF15)
- Lista con buscador
- Crear, editar, eliminar lógico
- Vista de alertas de stock bajo
- Validaciones de stock no negativo

### 3. Categorías CRUD
- Lista, crear, editar, eliminar lógico
- Validación de nombre duplicado

### 4. Clientes CRUD
- Tipos de documento: DNI, RUC, CE, PASAPORTE
- Reactivación automática al editar cliente inactivo
- Validación de documento duplicado
- 3 clientes de prueba creados

### 5. Usuarios CRUD con BCrypt
- Encriptación BCrypt cost-factor 10 (estándar OWASP)
- Roles diferenciados: ADMIN (rojo) y VENDEDOR (azul)
- Validación de password mínimo 6 caracteres
- Edición sin perder password actual (campo vacío = mantener)
- 5 usuarios activos en el sistema

### 6. Ventas Transaccional ⭐ (Módulo crítico)
- Carrito dinámico con JavaScript
- Cálculo automático de IGV (18%) en tiempo real
- Generación automática de código único: `VTA-YYYY-NNNN`
- **Defensa en 3 capas para stock no negativo:**
  - Capa SQL: `CHECK (stock_actual >= 0)`
  - Capa Java: `@Min(0)` en entity
  - Capa Service: `BusinessException` antes del save
- Auditoría automática en tabla `auditoria_stock`
- Anulación de venta con reversa automática de stock
- `@Transactional` para atomicidad completa
- Venta de prueba registrada: VTA-2026-0001 (S/123.90)

### 7. Reportes Excel con Apache POI
- 3 reportes disponibles:
  - `/reportes/productos` - Listado completo
  - `/reportes/stock-bajo` - Alertas RF15
  - `/reportes/ventas` - Historial RF19-20
- Formato `.xlsx` (Excel 2007+ / OpenXML)
- Estilos personalizados: titulo navy, headers blanco/navy
- Auto-ajuste de columnas
- Streaming binario con `Content-Disposition: attachment`
- Nombres con fecha: `MerchStock_TIPO_YYYYMMDD.xlsx`

### 8. Autenticación + Roles (Spring Security) 🔐
- **CustomUserDetailsService** conecta MySQL con Spring Security
- **BCryptPasswordEncoder** cost-factor 10
- **DaoAuthenticationProvider**
- Vista de login bonita con gradient azul-teal
- Vista de Acceso Denegado (403) personalizada
- Dropdown de usuario en navbar de todas las vistas
- Logout con confirmación visual
- Sesión HTTP de 1 sesión por usuario
- Manejo de sesión expirada
- Auditoría de logins en logs

**Reglas de acceso (RBAC):**

| Módulo | ADMIN | VENDEDOR |
|--------|-------|----------|
| Home | ✅ | ✅ |
| Productos (ver) | ✅ | ✅ |
| Productos (crear/editar/eliminar) | ✅ | ❌ |
| Categorías | ✅ | ❌ |
| Clientes | ✅ | ✅ |
| Usuarios | ✅ | ❌ |
| Ventas | ✅ | ✅ |
| Reportes | ✅ | ✅ |
| Alertas | ✅ | ✅ |

---

## 🔑 Credenciales de Prueba

Todos los usuarios tienen hash BCrypt real (verificado en MySQL):

| Username | Password | Rol |
|----------|----------|-----|
| `admin` | `Admin2026` | ADMIN |
| `cremuzgo` | `Cesar2026` | ADMIN |
| `pventa1` | `Patricia2026` | VENDEDOR |
| `avendedor` | `Andrea2026` | VENDEDOR |
| `falejos` | `BackEnd2026` | ADMIN |

---

## 📊 Métricas del Proyecto

| Métrica | Valor |
|---------|-------|
| Pantallas funcionales | 14+ |
| Módulos completos | 7 |
| RFs cubiertos | ~12 de 22 (~55%) |
| Líneas de código Java | ~2500+ |
| Tablas en BD | 7 con constraints e índices |
| Commits en develop | 10+ |
| Patrones aplicados | MVC, DAO, SOLID, RBAC, Defense in Depth |

---

## 🎯 Proyección de Nota APF3

| Criterio | Max | Actual | Meta |
|----------|-----|--------|------|
| Diseño solución (MVC+DAO+SOLID+Seguridad) | 3 | 3/3 ✅ | 3/3 |
| Uso recursos Java (4 librerías) | 2 | 2/2 ✅ | 2/2 |
| Control versiones | 3 | 2.5/3 | 3/3 |
| Interfaces gráficas (100% alcance) | 6 | ~5/6 | 6/6 |
| Construcción producto final | 4 | 3.5/4 | 4/4 |
| Sustentación oral | 2 | 0/2 ⏳ | 2/2 |
| **TOTAL** | **20** | **~16/20** | **18-20/20** |

---

## 📅 Cronograma APF3

### ✅ COMPLETADO

**Sesión 1 (sábado 7/jun):** Setup + Productos CRUD
- Configuración inicial del repo
- Spring Boot generado
- BD MySQL con 7 tablas
- Módulo Productos completo

**Sesión 2 (domingo 8/jun):** Clientes + Usuarios + Ventas
- Módulo Clientes CRUD
- Módulo Usuarios CRUD con BCrypt
- Módulo Ventas transaccional con defensa en 3 capas
- Auditoría de stock

**Sesión 3 (lunes 9/jun):** Reportes + Login
- Módulo Reportes Excel con Apache POI (cierra las 4 librerías)
- Módulo Login + Roles con Spring Security
- 21 archivos en último commit

### ⏳ PENDIENTE

**Martes 10/jun (2.5 horas):**
- Tests unitarios TDD (45 min)
- Dashboard con estadísticas (30 min)
- Importar productos desde CSV con Apache Commons (45 min)
- Generar Javadoc HTML con `.\mvnw javadoc:javadoc` (15 min)
- Coordinar commits del equipo (30 min)

**Miércoles 11/jun (3 horas):**
- Informe Word APF3 (90 min)
- PowerPoint ~12 slides (90 min)

**Jueves 12/jun (2 horas):**
- Ensayar exposición con el equipo (60 min)
- Asegurar commits de todos los miembros (30 min)
- Pulir detalles (30 min)

**Viernes 13/jun:** SUSTENTACIÓN ORAL (~10 min total, todos exponemos)

---

## 🧠 Decisiones Importantes Ya Tomadas

### ❌ NO REVERTIR
- ❌ Presupuesto NO es S/37,000 ni S/6,856.50 → es **S/15,000**
- ❌ No usar PowerShell con `Out-File -Encoding UTF8` para crear `.java` (mete BOM)
- ❌ No crear archivos `package-info.java` (problemáticos, innecesarios)
- ❌ No revertir relaciones EAGER a LAZY en Venta, VentaDetalle, Producto.categoria

### ✅ ADOPTADO
- ✅ MVC + DAO + SOLID (TDD opcional pero deseable como diferenciador)
- ✅ Eliminación lógica (`activo = false`) en todos los CRUDs
- ✅ Lombok `@RequiredArgsConstructor` + `@Slf4j` en services
- ✅ Bootstrap 5 vía CDN + Bootstrap Icons
- ✅ Estilos standalone en cada vista (no fragments, por simplicidad)
- ✅ Navbar consistente con 7 enlaces en todas las vistas
- ✅ Dropdown de usuario a la derecha en navbar
- ✅ Footer: `© 2026 MerchStock - APF3 - Grupo 3 - UTP 2026`

---

## 🐛 Bugs Resueltos en el Proceso

| # | Problema | Solución |
|---|----------|----------|
| 1 | `LazyInitializationException` en Cliente al ver detalle de venta | Cambiar `FetchType.LAZY` → `EAGER` en Venta.cliente, Venta.usuario, VentaDetalle.producto |
| 2 | BOM character en archivos `.java` creados con PowerShell | Usar `New-Item` en lugar de `Out-File -Encoding UTF8` |
| 3 | Bug en `renderCarrito()` JS: mensaje vacío que se borraba | Regenerar mensaje vacío en el HTML del container, no usar variable |
| 4 | Dropdown de usuario no abría en navbar | Faltaba `<script src="...bootstrap.bundle.min.js"></script>` en algunas vistas |
| 5 | `BusinessException` no detectaba stock insuficiente | Validación explícita en service ANTES de `save()` |

---

## 🔧 Comandos Útiles

### Arrancar el sistema
```powershell
cd F:\GitHub\MerchStock
.\mvnw spring-boot:run
```

### Limpiar y recompilar
```powershell
.\mvnw clean install -DskipTests
```

### Generar Javadoc HTML
```powershell
.\mvnw javadoc:javadoc
# Salida: target/site/apidocs/
```

### Verificar dependencias
```powershell
.\mvnw dependency:tree
```

---

## 🎓 Conceptos Clave para la Sustentación

### Defensa en 3 Capas (Stock no negativo)
```
Capa 1 (BD):      CHECK (stock_actual >= 0)
Capa 2 (Java):    @Min(0) en entity Producto.stockActual
Capa 3 (Service): validación explícita + BusinessException antes del save
```

### Transaccionalidad
`@Transactional` en `registrarVenta()` garantiza atomicidad:
si falla cualquier paso, se hace rollback de TODA la operación
(stock reducido, venta guardada, auditoría) - no quedan estados parciales.

### Seguridad BCrypt
Función hash unidireccional con cost-factor 10 = 1024 iteraciones.
NUNCA se desencripta. Spring Security compara el password ingresado
encriptado vs el hash almacenado.

### RBAC (Role-Based Access Control)
Cada usuario tiene un rol (ADMIN o VENDEDOR). Las URLs están
mapeadas a roles específicos en `SecurityConfig`. Aplica el principio
de mínimo privilegio.

### MVC + DAO + SOLID
- **MVC:** Controller → Service → Repository → Entity → View
- **DAO:** Spring Data JPA con 7 Repositories que extienden JpaRepository
- **SOLID:**
  - SRP: cada clase tiene una responsabilidad
  - OCP: extensible vía interfaces
  - LSP: implementaciones intercambiables
  - ISP: interfaces específicas (no fat interfaces)
  - DIP: Controllers dependen de Service interfaces, no de impls

---

## 📞 Contacto y Soporte

**Project Manager:** Cesar Gabriel Remuzgo Ángeles
**Email:** u18208011@utp.edu.pe
**GitHub:** @CesarRemuzgo

---

_Última actualización: 9 de junio de 2026, 23:00 horas_
_Próxima sesión planificada: martes 10 de junio, mañana_