package com.example.vidasalud.ui.navigation

// Clase sellada para definir las rutas de la app
sealed class RutasApp(val ruta: String) {

    // Ruta: pantalla de bienvenida
    object PantallaBienvenida : RutasApp("pantalla_bienvenida")

    // Ruta: pantalla de inicio de sesión
    object PantallaLogin : RutasApp("pantalla_login")

    // Ruta: pantalla de registro de usuario
    object PantallaRegistro : RutasApp("pantalla_registro")

    // Ruta: pantalla principal después de iniciar sesión
    object PantallaPrincipal : RutasApp("pantalla_principal")
}
