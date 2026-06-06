# Guía de Contribución — MerchStock

Esta guía documenta cómo el equipo del Grupo 3 colabora en el desarrollo del proyecto MerchStock.

## 🌳 Estructura de Ramas

- **`main`** — Código estable. Solo recibe merges para entregables APF.
- **`develop`** — Rama de integración del equipo. Aquí se mergean las features.
- **`feature/*`** — Una rama por cada funcionalidad nueva. Ej: `feature/login`, `feature/crud-productos`.

## 🔄 Flujo de Trabajo

### Para agregar una funcionalidad nueva:

1. Actualiza tu develop local:
```bash
   git checkout develop
   git pull origin develop
```

2. Crea una rama feature:
```bash
   git checkout -b feature/nombre-de-la-funcionalidad
```

3. Trabaja en tu funcionalidad (escribe código, prueba, etc.)

4. Haz commits frecuentes:
```bash
   git add .
   git commit -m "feat: descripción del cambio"
```

5. Sube tu rama a GitHub:
```bash
   git push -u origin feature/nombre-de-la-funcionalidad
```

6. Abre un Pull Request (PR) en GitHub apuntando a `develop`
7. Espera review de al menos un compañero
8. Mergea el PR después de aprobación

## 📝 Convención de Commits

Usamos **Conventional Commits** en español.

### Formato

### Tipos válidos

| Prefijo | Cuándo usarlo | Ejemplo |
|---|---|---|
| `feat:` | Nueva funcionalidad | `feat: agregar CRUD de productos` |
| `fix:` | Corregir un bug | `fix: corregir cálculo de stock negativo` |
| `docs:` | Solo documentación | `docs: agregar Javadoc a ProductoService` |
| `style:` | Formato, espacios | `style: aplicar formato a login.html` |
| `refactor:` | Refactorización | `refactor: extraer validación a util` |
| `test:` | Tests | `test: agregar tests a VentaService` |
| `chore:` | Configuración, deps | `chore: agregar dependencia Apache POI` |

### Reglas

- En español
- Verbo en infinitivo (agregar, corregir, implementar)
- NO en gerundio (agregando) ni pasado (agregué)
- Menos de 72 caracteres
- Sin punto al final
- Una sola línea

## 👥 Asignación de Módulos

| Integrante | Módulos asignados |
|---|---|
| Cesar Remuzgo (PM) | Coordinación, arquitectura, integración |
| Luis Fernando Alejos | Auth, productos, base de datos |
| Liz Anyeli Cueva | Documentación técnica, análisis |
| Daleth Correa | Ventas, alertas, frontend Thymeleaf |
| Luis Ernesto Romani | QA, testing, GitHub, control de versiones |

## ⚠️ Reglas Importantes

1. **NUNCA** hagas push directo a `main` — está protegida.
2. **NUNCA** hagas push directo a `develop` sin coordinarlo — usa siempre ramas feature + PR.
3. **SIEMPRE** haz `git pull origin develop` antes de empezar a trabajar.
4. **SIEMPRE** prueba tu código localmente antes de subir.
5. **Cada miembro** debe hacer commits con SU PROPIA cuenta de GitHub.