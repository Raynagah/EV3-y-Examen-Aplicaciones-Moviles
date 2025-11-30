# 💚 VidaSalud App

**VidaSalud App** es una aplicación móvil de bienestar y salud desarrollada en **Kotlin** y **Jetpack Compose**.

Permite a los usuarios registrarse y hacer seguimiento de sus métricas diarias (pasos, peso, sueño, calorías), mostrando un resumen personalizado de su progreso.

---

## ✨ Características Principales

### 🔐 Autenticación Sólida (MVVM + Firebase)

Flujo completo de registro y login con validación de campos (incluyendo formato de email, contraseñas coincidentes y límites lógicos), manejo de estado (`UiState`) y conexión a **Firebase Authentication** y **Cloud Firestore**.

### 👤 Manejo de Perfil

Pantalla dedicada para visualizar datos de usuario y permitir la selección de foto de perfil con acceso a la Galería de Fotos (usando `rememberLauncherForActivityResult` y **Coil**), además de la funcionalidad de **cerrar sesión**.

### 📊 Registro de Datos Diarios

Pantalla de estadísticas con formulario validado para registrar peso, calorías, sueño y pasos.  
La interfaz incluye una vista de **Calendario** (`DatePickerDialog`) que previene la selección de fechas futuras y permite cargar/guardar registros por fecha en **Firestore**.

### 🎨 Diseño Moderno

Interfaz de usuario declarativa basada en **Jetpack Compose**, con navegación inferior de 4 pestañas:
**Resumen**, **Datos**, **Comunidad** y **Perfil**, además de retroalimentación visual mediante *Snackbars* y *Loaders*.

---

## 🛠️ Tecnologías y Arquitectura

| Categoría | Elementos Clave |
|------------|----------------|
| **Lenguaje/UI** | Kotlin (100% nativo) y Jetpack Compose (declarativo). |
| **Arquitectura** | MVVM (Model-View-ViewModel) para una separación limpia de la lógica. |
| **Backend** | Firebase Authentication (gestión de usuarios) y Cloud Firestore (persistencia de datos de usuario y registros). |
| **Gestión de Estado** | StateFlow y UiState (Flujo de datos unidireccional - UDF). |
| **Librerías Clave** | Jetpack Navigation (navegación), Coil (carga de imágenes), Firebase BOM. |

---

## 🚀 Cómo Ejecutar el Proyecto

Para compilar y ejecutar este proyecto localmente, necesitarás configurar tu propio entorno de **Firebase**.

### 1. Clonar el Repositorio

```bash
https://github.com/Raynagah/EV3-y-Examen-Aplicaciones-Moviles.git
```

### 2. Abrir en Android Studio

1. Abre **Android Studio** y selecciona *Open an existing project*.
2. Navega a la carpeta donde clonaste el repositorio y ábrela.

### 3. Configurar Firebase (**Paso Crítico**)

1. Ve a la [Consola de Firebase](https://console.firebase.google.com/) y crea un nuevo proyecto.  
2. Registra una nueva aplicación de Android dentro de tu proyecto.  
3. Usa el **ID de paquete (Package Name):**
   
   ```
   com.example.vidasalud
   ```
5. Descarga el archivo `google-services.json` que Firebase te proporcionará.  
6. Copia este archivo dentro de la carpeta `app/` de tu proyecto en Android Studio.  
7. En la consola de Firebase:
   - Ve a **Authentication** → habilita el proveedor **Email/Password**.  
   - Ve a **Cloud Firestore** → crea una base de datos (puedes empezar en modo de prueba).

### 4. Sincronizar y Ejecutar

1. Espera a que Android Studio sincronice los archivos Gradle (o haz clic en **Sync Now**).  
2. Selecciona un emulador o un dispositivo físico conectado.  
3. Presiona **Run (▶)**.

---

## 🧠 Autores

**Francisco Vera** | **Rodrigo Vargas**  
📍 *Duoc UC — Sede Puerto Montt*  
📘 *Curso: DSY1105 - Desarrollo de Aplicaciones Móviles*
