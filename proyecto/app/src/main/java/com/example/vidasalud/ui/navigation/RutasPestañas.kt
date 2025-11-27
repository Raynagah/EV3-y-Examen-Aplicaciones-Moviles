package com.example.vidasalud.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

// Clase sellada para gestionar las rutas de las pestañas inferiores (Bottom Bar)
sealed class RutasPestañas(
    val ruta: String,      // Ruta para navegación
    val titulo: String,    // Título que se mostrará en la pestaña
    val icono: ImageVector // Icono de la pestaña
) {
    // Pestaña: Resumen del usuario
    object Resumen : RutasPestañas(
        ruta = "resumen",
        titulo = "Resumen",
        icono = Icons.Default.Home
    )

    // Pestaña: Estadísticas de salud
    object Estadisticas : RutasPestañas(
        ruta = "estadisticas",
        titulo = "Datos",
        icono = Icons.Default.BarChart
    )

    // Pestaña: Comunidad
    object Comunidad : RutasPestañas(
        ruta = "comunidad",
        titulo = "Comunidad",
        icono = Icons.Default.People
    )

    // Pestaña: Perfil del usuario
    object Perfil : RutasPestañas(
        ruta = "perfil",
        titulo = "Perfil",
        icono = Icons.Default.Person
    )
}
