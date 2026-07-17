# 🧪 Plan de Pruebas – MerchStock

> **Sistema web de gestión de inventario para MerchStock Perú E.I.R.L.**
> Avance de Proyecto Final 3 (APF3) - UTP 2026

---

## 📋 1. Información General

| Campo | Descripción |
|---------|-------------|
| 🏷️ Proyecto | MerchStock |
| 🚀 Versión | APF3 |
| ☕ Lenguaje | Java 21 |
| 🌱 Framework | Spring Boot 3 |
| 🧪 Herramientas de prueba | JUnit 5 + Mockito |
| 👥 Equipo | Grupo MerchStock |
| 📅 Fecha | 2026 |

---

## 🎯 2. Objetivo

Garantizar la calidad del sistema mediante pruebas unitarias automatizadas que validen las reglas de negocio críticas relacionadas con inventario, ventas, productos y cálculos financieros.

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

### 💰 CP-06 Cálculo Automático de IGV

**Clase:** VentaServiceImplTest

**Objetivo:**
Validar el cálculo del impuesto y total de venta.

**Resultado esperado:**

- IGV = 18%
- Total correcto.

**Estado:** ✅ Aprobado

---

### 🧾 CP-07 Generación de Código de Venta

**Clase:** VentaServiceImplTest

**Objetivo:**
Verificar la generación automática del código de venta.

**Resultado esperado:**

```text
VTA-YYYY-NNNN
```

Ejemplo:

```text
VTA-2026-0001
```

**Estado:** ✅ Aprobado

---

## 📊 6. Resumen de Resultados

| Métrica | Resultado |
|----------|-----------|
| 🧪 Total de pruebas | 7 |
| ✅ Pruebas aprobadas | 7 |
| ❌ Pruebas fallidas | 0 |
| ⚠️ Errores | 0 |
| 🚀 Estado final | BUILD SUCCESS |

---

## 📸 7. Evidencias

### 🖥️ Ejecución de pruebas

Insertar captura de:

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

## 🏁 8. Conclusiones

- ✅ Las pruebas validan las reglas críticas del negocio.
- ✅ Mockito permitió aislar la lógica de negocio de la base de datos.
- ✅ La estrategia TDD incrementó la confiabilidad del código.
- ✅ Todas las pruebas fueron ejecutadas exitosamente.
- ✅ El sistema mantiene la integridad del inventario y de las ventas.

---

**MerchStock APF3**
🛠️ Spring Boot • 🧪 JUnit 5 • 🎭 Mockito • ☕ Java 21