# 🧪 Plan de Pruebas – MerchStock

> **Sistema web de gestión de inventario para MerchStock Perú E.I.R.L.**
> Proyecto Final - UTP 2026

---

## 📋 1. Información General

| Campo | Descripción |
|---------|-------------|
| 🏷️ Proyecto | MerchStock |
| 🚀 Versión | Proyecto Final |
| ☕ Lenguaje | Java 17 |
| 🌱 Framework | Spring Boot 3 |
| 🧪 Herramientas de prueba | JUnit 5 + Mockito |
| 🛡️ Herramienta de seguridad | OWASP ZAP 2.17.0 |
| 👥 Equipo | Grupo MerchStock |
| 📅 Fecha | 2026 |

---

## 🎯 2. Objetivo

Garantizar la calidad del sistema mediante pruebas unitarias automatizadas que validen las reglas de negocio críticas relacionadas con inventario, ventas, productos y cálculos financieros, complementadas con pruebas activas de seguridad sobre el sistema desplegado.

---

## 📦 3. Alcance

Las pruebas cubren los siguientes módulos:

- ✅ Productos
- ✅ Inventario
- ✅ Ventas
- ✅ Categorías
- ✅ Usuarios
- ✅ Validaciones de negocio
- ✅ Cálculo de IGV
- ✅ Generación de códigos de venta
- ✅ Seguridad de la aplicación desplegada (OWASP ZAP)

---

## 🔄 4. Estrategia TDD

El proyecto adopta el enfoque **Test Driven Development (TDD)**:

```text
🔴 RED
↓
Escribir prueba que falla

🟢 GREEN
↓
Implementar código mínimo

🔵 REFACTOR
↓
Mejorar código manteniendo pruebas exitosas
```

Herramientas utilizadas:

- 🧪 JUnit 5
- 🎭 Mockito
- ⚙️ Maven Surefire

---

## 📑 5. Casos de Prueba

### 🔧 CP-00 Carga del Contexto de la Aplicación

**Clase:** MerchstockAppApplicationTests

**Objetivo:**
Verificar que el contexto de Spring Boot arranca correctamente con toda la configuración (beans, seguridad, JPA) sin errores.

**Resultado esperado:**

- El contexto de la aplicación se inicializa sin excepciones.

**Estado:** ✅ Aprobado

---

### 🛒 CP-01 Reducción de Stock

**Clase:** ProductoServiceImplTest

**Objetivo:**
Validar que el stock disminuya correctamente cuando existe inventario disponible.

**Resultado esperado:**

- Stock actualizado correctamente.
- Sin excepciones.

**Estado:** ✅ Aprobado

---

### 🚫 CP-02 Venta con Stock Insuficiente

**Clase:** ProductoServiceImplTest

**Objetivo:**
Evitar ventas que excedan el inventario disponible.

**Resultado esperado:**

- Lanzar BusinessException.

**Estado:** ✅ Aprobado

---

### 🔢 CP-03 Validación de Cantidad

**Clase:** ProductoServiceImplTest

**Objetivo:**
Verificar que la cantidad ingresada sea mayor a cero.

**Resultado esperado:**

- Rechazo de valores inválidos.

**Estado:** ✅ Aprobado

---

### 🏷️ CP-04 Normalización de SKU

**Clase:** ProductoServiceImplTest

**Objetivo:**
Convertir automáticamente el SKU a mayúsculas.

**Resultado esperado:**

- SKU normalizado.

**Estado:** ✅ Aprobado

---

### ⚠️ CP-05 Detección de SKU Duplicado

**Clase:** ProductoServiceImplTest

**Objetivo:**
Evitar el registro de productos repetidos.

**Resultado esperado:**

- Excepción de validación.

**Estado:** ✅ Aprobado

---

### 💰 CP-06 Cálculo Automático de IGV y Código de Venta

**Clase:** VentaServiceImplTest

**Objetivo:**
Validar el cálculo del desglose de IGV (18%, por desglose inverso sobre el total) y la generación automática del código de venta.

**Resultado esperado:**

- IGV calculado correctamente (Total − Operación Gravada, donde Operación Gravada = Total ÷ 1.18).
- Código con formato `VTA-YYYY-NNNN` (ejemplo: `VTA-2026-0001`).
- Total correcto.

**Estado:** ✅ Aprobado

---

## 📊 6. Resumen de Resultados — Pruebas Unitarias

| Métrica | Resultado |
|----------|-----------|
| 🧪 Total de pruebas | 7 |
| ✅ Pruebas aprobadas | 7 |
| ❌ Pruebas fallidas | 0 |
| ⚠️ Errores | 0 |
| 🚀 Estado final | BUILD SUCCESS |

**Desglose:** 1 prueba de carga de contexto (`contextLoads`) + 5 pruebas de `ProductoServiceImplTest` + 1 prueba de `VentaServiceImplTest`.

---

## 📸 7. Evidencias — Pruebas Unitarias

### 🖥️ Ejecución de pruebas

```bash
Tests run: 7
Failures: 0
Errors: 0
BUILD SUCCESS
```

### 📈 Cobertura funcional

| Módulo | Estado |
|---------|---------|
| Productos | ✅ |
| Inventario | ✅ |
| Ventas | ✅ |
| Validaciones | ✅ |
| IGV | ✅ |
| Generación de códigos | ✅ |

---

## 🛡️ 8. Pruebas de Seguridad (OWASP ZAP)

Complementando las pruebas unitarias, se ejecutó un escaneo de seguridad con **OWASP ZAP 2.17.0** contra el sistema desplegado (`.jar` standalone en `http://localhost:8080`), aplicando el marco **OWASP Top 10:2025**.

### 📋 8.1 Metodología

- **Herramienta:** OWASP ZAP 2.17.0 (Automated Scan: Spider + Active Scan)
- **Objetivo escaneado:** `http://localhost:8080`
- **Tipo de escaneo:** Sin autenticación (cobertura de la superficie pública: login, recursos estáticos, cabeceras HTTP globales)
- **Política de escaneo:** Dev Standard

### 📋 8.2 Resumen de hallazgos

| Riesgo | Alerta | Instancias |
|--------|--------|:----------:|
| 🟠 Medio | Cabecera Content Security Policy (CSP) no configurada | 3 |
| 🟠 Medio | Falta atributo de integridad de recursos secundarios (SRI) | 5 |
| 🟠 Medio | Ausencia de Tokens Anti-CSRF | 3 |
| 🟡 Bajo | Cookie sin atributo SameSite | 3 |
| ⚪ Informativo | Petición de Autenticación Identificada | 1 |
| ⚪ Informativo | Respuesta de Gestión de Sesión Identificada | 4 |

**Sin hallazgos de riesgo Alto o Crítico.**

### 🎯 8.3 Hallazgo destacado

El escaneo confirmó, con evidencia real, una deuda técnica ya documentada en el código (`SecurityConfig.java` deshabilita CSRF explícitamente, con un comentario indicando que es temporal para desarrollo). Esto valida la coherencia entre el análisis estático del código y la verificación dinámica del sistema en ejecución.

### 📊 8.4 Priorización de riesgos

Aplicando la fórmula **Riesgo = Probabilidad × Impacto**:

| Prioridad | Hallazgo |
|-----------|----------|
| Alta | Ausencia de Tokens Anti-CSRF |
| Media | Cabecera CSP no configurada |
| Media | Falta de atributo de integridad (SRI) |
| Baja | Cookie sin atributo SameSite |

### ✅ 8.5 Estado

- ✅ Escaneo ejecutado y documentado
- ✅ Hallazgos priorizados y mapeados a OWASP Top 10:2025 y CWE
- ✅ Plan de remediación propuesto

**Reporte completo:** ver sección "Pruebas de Seguridad" del documento `MerchStock_Fase_Final_Completa.docx`.

### 🔒 8.6 Remediación Proactiva Adicional — Bloqueo de Cuenta por Fuerza Bruta

El escaneo de OWASP ZAP se ejecutó **sin autenticación**, por lo que no evaluó la resistencia del endpoint `POST /login` frente a ataques de fuerza bruta (adivinación de contraseñas por intentos repetidos). Este vector de ataque no está cubierto por ninguno de los 6 hallazgos de la sección 8.2, pero fue identificado como riesgo razonable dado que el sistema expone un formulario de login público.

Como medida preventiva, se implementó un mecanismo de bloqueo de cuenta:

- **Política:** 5 intentos fallidos consecutivos → bloqueo de la cuenta por 15 minutos.
- **Reseteo:** el contador de intentos fallidos se reinicia automáticamente al iniciar sesión con éxito.
- **Persistencia del bloqueo:** el bloqueo se mantiene activo incluso si, dentro de la ventana de 15 minutos, se ingresa la contraseña correcta — evita que un atacante que eventualmente acierte la contraseña dentro de una ráfaga de intentos pueda acceder.
- **Implementación:** `CustomUserDetailsService` verifica el estado de bloqueo (`bloqueadoHasta`); `LoginAttemptListener` escucha los eventos de autenticación (éxito, fallo, intento sobre cuenta bloqueada) e incrementa/resetea el contador; `SecurityConfig` distingue mediante un `AuthenticationFailureHandler` si el fallo se debe a credenciales incorrectas o a una cuenta bloqueada, mostrando el mensaje correspondiente al usuario.
- **Verificación:** probado en vivo con el usuario `pventa1` — confirmado que al 5.º intento fallido la cuenta se bloquea, y que un 6.º intento con la contraseña correcta sigue siendo rechazado mientras el bloqueo esté vigente.

Esta mejora no fue exigida por la rúbrica ni detectada por el escaneo de ZAP; se implementó de forma proactiva al identificar la brecha de cobertura entre un escaneo no autenticado y el riesgo real que representa un formulario de login expuesto públicamente.

---

## 🏁 9. Conclusiones

- ✅ Las pruebas unitarias validan las reglas críticas del negocio.
- ✅ Mockito permitió aislar la lógica de negocio de la base de datos.
- ✅ La estrategia TDD incrementó la confiabilidad del código.
- ✅ Todas las pruebas unitarias fueron ejecutadas exitosamente (7/7).
- ✅ El sistema mantiene la integridad del inventario y de las ventas.
- ✅ El escaneo de seguridad con OWASP ZAP no encontró vulnerabilidades de riesgo Alto o Crítico, validando los controles ya implementados (BCrypt, RBAC, consultas parametrizadas).
- ✅ Los hallazgos de seguridad identificados quedan documentados con un plan de remediación priorizado.
- ✅ Se implementó de forma proactiva un mecanismo de bloqueo de cuenta tras 5 intentos fallidos (15 min), mitigando un vector de ataque (fuerza bruta) no cubierto por el escaneo automatizado al ser este sin autenticación.

---

**MerchStock — Proyecto Final**
🛠️ Spring Boot • 🧪 JUnit 5 • 🎭 Mockito • 🛡️ OWASP ZAP • ☕ Java 17