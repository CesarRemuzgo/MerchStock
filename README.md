# MerchStock — Sistema Web de Administración de Inventario

> Curso Integrador I: Sistemas Software — UTP 2026 — Sección 27667
> Docente: Mag. Marcos Teodoro Yerren Huima

## 📋 Descripción

Sistema web para la gestión de inventario de **MerchStock Perú E.I.R.L.**,
empresa de merchandising ubicada en Av. Carlos Izaguirre 845, Los Olivos, Lima.

La aplicación permite administrar productos, registrar ventas con descuento
automático de stock, generar alertas cuando el stock sea menor a 5 unidades,
y producir reportes de ventas y productos más vendidos.

## 🧑‍💻 Equipo — Grupo 3

| Rol | Integrante | GitHub |
|---|---|---|
| Project Manager | Cesar Remuzgo Ángeles | @CesarRemuzgo |
| Backend Lead | Luis Fernando Alejos Pérez | @pendiente |
| Analista / Docs | Liz Anyeli Cueva Samillán | @pendiente |
| Frontend / UX | Daleth Correa Ángeles | @pendiente |
| QA / DevOps | Luis Ernesto Romani Villanueva | @pendiente |

## 🛠️ Stack Tecnológico

- **Lenguaje:** Java 17
- **Framework:** Spring Boot 3.x
- **Vista:** Thymeleaf + Bootstrap 5
- **Base de datos:** MySQL 8
- **Build:** Maven
- **Testing:** JUnit 5
- **Librerías de apoyo:** Apache POI, Google Guava, Apache Commons, Logback

## 🏗️ Arquitectura

Patrones aplicados:
- **MVC** (Model-View-Controller)
- **DAO** (Data Access Object)
- **SOLID** (3 de 4 principios mínimo)

## 📂 Estrategia de Ramas

- `main` — código estable, solo se actualiza en entregables APF
- `develop` — rama de integración del equipo
- `feature/*` — desarrollo de funcionalidades individuales

## 📅 Cronograma del Proyecto

| Hito | Entregable | Fecha |
|---|---|---|
| APF1 | Planificación y Análisis | 18/04/2026 ✅ |
| APF2 | Diseño y Prototipos | 16/05/2026 ✅ |
| APF3 | Avance de codificación | 13/06/2026 🚧 |
| Final | Sustentación final | 25/07/2026 ⏳ |

## 🚀 Cómo levantar el proyecto

*(Pendiente — se documenta cuando exista el esqueleto Spring Boot)*

## 📄 Licencia

MIT License — ver archivo [LICENSE](LICENSE)

## 🗄️ Configuracion de Base de Datos

Los scripts SQL para crear la BD estan en la carpeta [`database/`](database/).

**Pasos rapidos:**
1. Asegurate de tener MySQL 8 corriendo en `localhost:3306`
2. Abrir MySQL Workbench y conectar como `root`
3. Ejecutar `database/01-schema.sql` (crea BD y 7 tablas)
4. Ejecutar `database/02-seed-data.sql` (carga 4 usuarios, 8 categorias, 16 productos)
5. Ver [`database/README.md`](database/README.md) para instrucciones detalladas

## ⚙️ Configurar el proyecto localmente

Cada miembro debe:

1. Clonar el repo: `git clone https://github.com/CesarRemuzgo/MerchStock`
2. Crear la BD ejecutando los scripts SQL (ver seccion anterior)
3. Copiar `src/main/resources/application-example.properties`
   y renombrarlo a `src/main/resources/application.properties`
4. Editar el archivo nuevo: poner tu password de MySQL en
   `spring.datasource.password=TU_PASSWORD`
5. Abrir la carpeta en VS Code (las extensiones recomendadas se sugieren automaticamente)
6. Click derecho en `MerchstockAppApplication.java` → Run Java
7. Abrir `http://localhost:8080` en el navegador

⚠️ **Importante:** NUNCA subas tu `application.properties` a Git
(esta protegido por el `.gitignore`). Tu password de MySQL es personal.