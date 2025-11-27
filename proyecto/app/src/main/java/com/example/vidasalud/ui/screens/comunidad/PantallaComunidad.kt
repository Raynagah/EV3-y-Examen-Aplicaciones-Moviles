package com.example.vidasalud.ui.screens.comunidad

// Importaciones necesarias para el diseño de la pantalla y elementos visuales
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

// Función Composable que representa la pantalla de Comunidad
@Composable
fun PantallaComunidad() {
    // Contenedor principal que ocupa toda la pantalla
    Box(
        modifier = Modifier.fillMaxSize(), // Ajustar al tamaño completo
        contentAlignment = Alignment.Center // Centrar el contenido dentro del contenedor
    ) {
        // Texto temporal que indica el nombre de la pantalla
        Text(text = "Pantalla de Comunidad")
    }
}
