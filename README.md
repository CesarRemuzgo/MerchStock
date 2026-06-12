# MerchStock — Sistema Web de Administración de Inventario

> Curso Integrador I: Sistemas Software — UTP 2026 — Sección 27667
> Docente: Mag. Marcos Teodoro Yerren Huima

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)
![MySQL](https://img.shields.io/badge/MySQL-8-blue)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

## 📋 Descripción

Sistema web para la gestión de inventario de **MerchStock Perú E.I.R.L.**,
empresa de merchandising ubicada en Av. Carlos Izaguirre 845, Los Olivos, Lima.

La aplicación permite administrar productos, registrar ventas con descuento
automático de stock, generar alertas cuando el stock sea menor o igual al
mínimo configurado, producir reportes en Excel, e importar productos
masivamente desde archivos CSV. Incluye autenticación con roles (ADMIN /
VENDEDOR) y un panel de control con estadísticas en tiempo real.

## 🧑‍💻 Equipo — Grupo 3

| Rol | Integrante | GitHub |
|---|---|---|
| Project Manager | Cesar Remuzgo Ángeles | @CesarRemuzgo |
| Backend Lead | Luis Fernando Alejos Pérez | @Luis-1bit |
| Analista / Docs | Liz Anyel Cueva Samillán | @AnyelCueva |
| Frontend / UX | Daleth Correa Ángeles | @DalethCorreaAngeles |
| QA / DevOps | Luis Ernesto Romani Villanueva | @LuisErnestoRomani |

> 👋 **¿Eres del equipo?** Lee [`CONTRIBUTING.md`](CONTRIBUTING.md) para saber
> cómo hacer tu aporte al repositorio paso a paso.

## 🛠️ Stack Tecnológico

- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.5
- **Vista:** Thymeleaf + Bootstrap 5
- **Base de datos:** MySQL 8
- **Seguridad:** Spring Security + BCrypt
- **Build:** Maven
- **Testing:** JUnit 5 + Mockito
- **Librerías de apoyo:** Apache POI, Google Guava, Apache Commons Lang, Logback

## 🏗️ Arquitectura

Patrones aplicados:
- **MVC** (Model-View-Controller)
- **DAO** (Data Access Object con Spring Data JPA)
- **SOLID** (los 5 principios)
- **RBAC** (control de acceso por roles)
- **Defense in Depth** (validación de stock en 3 capas)

## ✨ Funcionalidades Principales

- 📊 **Dashboard** con estadísticas en tiempo real (productos, alertas, ventas, ingresos)
- 📦 **Productos** — CRUD completo + alertas de stock bajo (RF15)
- 🏷️ **Categorías** — CRUD completo
- 👤 **Clientes** — CRUD con tipos de documento (DNI/RUC/CE/PASAPORTE)
- 🔐 **Usuarios** — CRUD con encriptación BCrypt y roles
- 🛒 **Ventas** — transaccional, con IGV 18%, código único y auditoría de stock
- 📈 **Reportes Excel** — productos, stock bajo y ventas (Apache POI)
- 📥 **Importación CSV** — carga masiva de productos (Apache Commons Lang)
- 🔑 **Login + Roles** — Spring Security con perfiles ADMIN y VENDEDOR
- ✅ **Pruebas unitarias** — JUnit 5 + Mockito

## 📂 Estrategia de Ramas

- `main` — código estable, solo se actualiza en entregables APF
- `develop` — rama de integración del equipo (rama de trabajo por defecto)
- `feature/*` — desarrollo de funcionalidades individuales

## 📅 Cronograma del Proyecto

| Hito | Entregable | Fecha |
|---|---|---|
| APF1 | Planificación y Análisis | 18/04/2026 ✅ |
| APF2 | Diseño y Prototipos | 16/05/2026 ✅ |
| APF3 | Avance de codificación | 13/06/2026 🚧 |
| Final | Sustentación final | 25/07/2026 ⏳ |

## 🗄️ Configuración de Base de Datos

Los scripts SQL para crear la BD están en la carpeta [`database/`](database/).

**Pasos rápidos:**
1. Asegúrate de tener MySQL 8 corriendo en `localhost:3306`
2. Abrir MySQL Workbench y conectar como `root`
3. Ejecutar `database/01-schema.sql` (crea BD y 7 tablas)
4. Ejecutar `database/02-seed-data.sql` (carga usuarios, categorías y productos)
5. Ver [`database/README.md`](database/README.md) para instrucciones detalladas

## 🚀 Cómo levantar el proyecto

### Requisitos previos
- Java 17 (JDK)
- MySQL 8 corriendo en `localhost:3306`
- Git
- VS Code (recomendado)

### Pasos

1. **Clonar el repositorio:**
```bash
   git clone https://github.com/CesarRemuzgo/MerchStock
   cd MerchStock
```

2. **Crear la base de datos** ejecutando los scripts SQL (ver sección anterior).

3. **Configurar tu conexión local:**
   - Copia `src/main/resources/application-example.properties`
   - Renómbralo a `src/main/resources/application.properties`
   - Edita la línea del password con tu contraseña de MySQL:
```properties
     spring.datasource.password=TU_PASSWORD
```

4. **Arrancar la aplicación:**
```bash
   ./mvnw spring-boot:run
```
   En Windows (PowerShell):
```powershell
   .\mvnw spring-boot:run
```

5. **Abrir en el navegador:** http://localhost:8080

   Serás redirigido a la pantalla de login.

### 🔑 Credenciales de prueba

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `admin` | `Admin2026` | ADMIN |
| `pventa1` | `Patricia2026` | VENDEDOR |

> ⚠️ **Importante:** NUNCA subas tu `application.properties` a Git
> (está protegido por el `.gitignore`). Tu password de MySQL es personal.

## 🧪 Ejecutar las pruebas

```bash
./mvnw test
```

Ejecuta las pruebas unitarias de la capa de servicios (JUnit 5 + Mockito).

## 📖 Documentación adicional

- [`docs/ESTADO_DEL_PROYECTO.md`](docs/ESTADO_DEL_PROYECTO.md) — estado detallado del avance
- [`CONTRIBUTING.md`](CONTRIBUTING.md) — guía de contribución para el equipo

## 📄 Licencia

MIT License — ver archivo [LICENSE](LICENSE)
