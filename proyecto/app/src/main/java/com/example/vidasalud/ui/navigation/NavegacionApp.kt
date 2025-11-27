package com.example.vidasalud.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vidasalud.ui.screens.auth.PantallaLogin
import com.example.vidasalud.ui.screens.auth.PantallaRegistro
import com.example.vidasalud.ui.screens.bienvenida.PantallaBienvenida
import com.example.vidasalud.ui.screens.principal.PantallaPrincipalNavegacion

@Composable
fun NavegacionApp() {
    val controladorNavegacion = rememberNavController() // Controlador para gestionar las pantallas

    NavHost(
        navController = controladorNavegacion, // Controlador de navegación
        startDestination = RutasApp.PantallaBienvenida.ruta // Pantalla inicial
    ) {
        // Ruta: Pantalla de bienvenida
        composable(route = RutasApp.PantallaBienvenida.ruta) {
            PantallaBienvenida(controladorNavegacion = controladorNavegacion) // Ir a login o registro
        }

        // Ruta: Pantalla de inicio de sesión
        composable(route = RutasApp.PantallaLogin.ruta) {
            PantallaLogin(controladorNavegacion = controladorNavegacion)
        }

        // Ruta: Pantalla de registro
        composable(route = RutasApp.PantallaRegistro.ruta) {
            PantallaRegistro(controladorNavegacion = controladorNavegacion)
        }

        // Ruta: Pantalla principal (con menú inferior)
        composable(route = RutasApp.PantallaPrincipal.ruta) {
            PantallaPrincipalNavegacion(navControllerPrincipal = controladorNavegacion)
        }
    }
}
