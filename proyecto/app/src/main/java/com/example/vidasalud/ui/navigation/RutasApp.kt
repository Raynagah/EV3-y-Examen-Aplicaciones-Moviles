package com.example.vidasalud.ui.navigation

sealed class RutasApp(val ruta: String) {
    object PantallaBienvenida : RutasApp("pantalla_bienvenida")
    object PantallaLogin : RutasApp("pantalla_login")
    object PantallaRegistro : RutasApp("pantalla_registro") // <--- ESTA ES LA IMPORTANTE
    object PantallaPrincipal : RutasApp("pantalla_principal")
}