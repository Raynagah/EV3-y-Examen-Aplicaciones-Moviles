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
fun NavegacionApp(startDestination: String) { // Recibe el destino inicial
    val controladorNavegacion = rememberNavController()

    NavHost(
        navController = controladorNavegacion,
        startDestination = startDestination // Usa el destino calculado
    ) {
        composable(route = RutasApp.PantallaBienvenida.ruta) {
            PantallaBienvenida(controladorNavegacion = controladorNavegacion)
        }
        composable(route = RutasApp.PantallaLogin.ruta) {
            PantallaLogin(controladorNavegacion = controladorNavegacion)
        }
        composable(route = RutasApp.PantallaRegistro.ruta) {
            PantallaRegistro(controladorNavegacion = controladorNavegacion)
        }
        composable(route = RutasApp.PantallaPrincipal.ruta) {
            PantallaPrincipalNavegacion(navControllerPrincipal = controladorNavegacion)
        }
    }
}