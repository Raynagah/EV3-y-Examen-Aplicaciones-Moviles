package com.example.vidasalud.ui.screens.principal

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vidasalud.ui.navigation.RutasPestañas
import com.example.vidasalud.ui.screens.comunidad.PantallaComunidad
import com.example.vidasalud.ui.screens.estadisticas.PantallaEstadisticas
import com.example.vidasalud.ui.screens.perfil.PantallaPerfil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipalNavegacion(navControllerPrincipal: NavController) {

    val controladorPestañas = rememberNavController()

    val items = listOf(
        RutasPestañas.Resumen,
        RutasPestañas.Estadisticas,
        RutasPestañas.Comunidad,
        RutasPestañas.Perfil,
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
            ) {
                val navBackStackEntry by controladorPestañas.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { pantalla ->
                    NavigationBarItem(
                        label = {
                            Text(
                                text = pantalla.titulo,
                                fontWeight = if (currentDestination?.hierarchy?.any { it.route == pantalla.ruta } == true) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        icon = { Icon(imageVector = pantalla.icono, contentDescription = pantalla.titulo) },
                        selected = currentDestination?.hierarchy?.any { it.route == pantalla.ruta } == true,
                        onClick = {
                            controladorPestañas.navigate(pantalla.ruta) {
                                popUpTo(controladorPestañas.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = controladorPestañas,
            startDestination = RutasPestañas.Resumen.ruta,
            Modifier.padding(innerPadding)
        ) {
            composable(RutasPestañas.Resumen.ruta) { PantallaPrincipal() }
            composable(RutasPestañas.Estadisticas.ruta) { PantallaEstadisticas() }
            composable(RutasPestañas.Comunidad.ruta) { PantallaComunidad() }


            composable(RutasPestañas.Perfil.ruta) {
                PantallaPerfil(navControllerPrincipal = navControllerPrincipal)
            }
        }
    }
}