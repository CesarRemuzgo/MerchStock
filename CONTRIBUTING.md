# 🤝 Guía de Contribución — MerchStock

> Equipo Grupo 3 — UTP 2026
> Esta guía te explica **paso a paso** cómo hacer tu aporte al repositorio.
> Si nunca usaste Git, sigue las instrucciones tal cual y no te pierdes.

---

## 🎯 ¿Qué tengo que hacer? (resumen)

Cada integrante debe dejar **2 aportes** en el repositorio:

1. ✏️ **Agregar tu usuario de GitHub** al `README.md` y al `ESTADO_DEL_PROYECTO.md`
2. 📄 **Crear un documento** en la carpeta `docs/` según tu rol (ver tabla abajo)

Cada aporte es un **commit**. Así quedan tus contribuciones visibles en el
historial del repositorio, que es lo que pide la rúbrica de Control de Versiones.

---

## 📋 Tu tarea según tu rol

| Integrante | Rol | Documento a crear en `docs/` |
|---|---|---|
| **Luis Fernando Alejos** | Backend | `docs/BACKEND.md` — describe los endpoints y servicios principales |
| **Liz Anyeli Cueva** | Analista/Docs | `docs/MANUAL_USUARIO.md` — guía de uso del sistema para el usuario final |
| **Luis Ernesto Romani** | QA/DevOps | `docs/PLAN_PRUEBAS.md` — casos de prueba y checklist de calidad |
| **Daleth Correa** | Frontend/UX | `docs/GUIA_ESTILOS.md` — paleta de colores, tipografía y componentes UI |

> 💡 No tiene que ser largo. Media página bien hecha es suficiente. Lo importante
> es que sea **tu aporte** y que entiendas lo que escribiste (la rúbrica evalúa autoría).

---

## ⚙️ Paso 0: Configurar Git (solo la primera vez)

Si nunca configuraste Git en tu PC, abre una terminal y ejecuta
(reemplaza con tus datos reales):

```bash
git config --global user.name "Tu Nombre"
git config --global user.email "tu_correo@utp.edu.pe"
```

> ⚠️ Usa el **mismo correo** que tienes en tu cuenta de GitHub. Así tus commits
> se asocian a tu perfil y aparecen con tu foto en el historial.

---

## 📥 Paso 1: Clonar el proyecto (solo la primera vez)

```bash
git clone https://github.com/CesarRemuzgo/MerchStock
cd MerchStock
```

Si ya lo clonaste antes, solo entra a la carpeta y actualiza:

```bash
cd MerchStock
git checkout develop
git pull origin develop
```

---

## 🌿 Paso 2: Trabajar en la rama `develop`

Siempre trabajamos sobre la rama `develop` (NO sobre `main`):

```bash
git checkout develop
git pull origin develop
```

Esto te asegura tener la última versión antes de hacer tus cambios.

---

## ✏️ Paso 3: Hacer tus cambios

### 3.1 — Agrega tu usuario de GitHub

Abre `README.md` y `docs/ESTADO_DEL_PROYECTO.md`, busca tu fila en la tabla
del equipo y cambia `@pendiente` o `(pendiente)` por tu usuario real.

Ejemplo:
| Backend Lead | Luis Fernando Alejos Pérez | @pendiente |
se convierte en:
| Backend Lead | Luis Fernando Alejos Pérez | @tu-usuario-github |

### 3.2 — Crea tu documento en `docs/`

Crea el archivo que te corresponde según la tabla (ej. `docs/BACKEND.md`) y
escribe tu contenido. Puedes usar esta plantilla mínima:

```markdown
# [Título de tu documento]

> Autor: [Tu Nombre] — [Tu Rol] — Grupo 3

## Introducción
[Breve descripción de qué trata este documento]

## Contenido
[Tu aporte: la información de tu sección]

## Conclusión
[Cierre breve]
```

---

## 💾 Paso 4: Guardar tus cambios (commit)

```bash
git add .
git commit -m "docs: agregar [tu documento] y usuario de [tu nombre]"
```

**Ejemplo real (Luis Fernando):**
```bash
git add .
git commit -m "docs: agregar documentacion de backend y usuario de Luis Alejos"
```

> 📝 Usamos el formato **Conventional Commits**: el mensaje empieza con `docs:`
> porque es documentación. Para código sería `feat:` o `fix:`.

---

## 🚀 Paso 5: Subir tus cambios (push)

```bash
git push origin develop
```

¡Listo! Tu aporte ya está en GitHub. Verifícalo entrando a:
https://github.com/CesarRemuzgo/MerchStock/commits/develop

Deberías ver tu commit con tu nombre y foto en la lista.

---

## ❓ Problemas comunes

### "Updates were rejected" al hacer push
Significa que alguien subió cambios antes que tú. Solución:
```bash
git pull origin develop
git push origin develop
```
Si te pide resolver un conflicto, avisa al PM (Cesar) por el grupo.

### "Please tell me who you are"
No configuraste Git (Paso 0). Vuelve a ese paso.

### No sé qué escribir en mi documento
Revisa el `ESTADO_DEL_PROYECTO.md`, ahí está casi toda la info del sistema.
Adapta lo que corresponde a tu rol. Si tienes dudas, pregunta en el grupo.

---

## 📞 Coordinación

Cualquier duda, escribir al **Project Manager (Cesar)** por el grupo del equipo.
La fecha límite para tener todos los commits es **jueves 12 de junio**.

---

_Gracias por contribuir a MerchStock 🚀_