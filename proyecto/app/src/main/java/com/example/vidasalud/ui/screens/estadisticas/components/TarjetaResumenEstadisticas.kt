package com.example.vidasalud.ui.screens.estadisticas.components

// Importaciones necesarias para crear el layout y estilos visuales
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TarjetaResumenEstadisticas(
    // Ícono representativo de la estadística (ej: peso, pasos, etc.)
    icono: ImageVector,
    // Texto principal (ej: "Peso", "Pasos")
    titulo: String,
    // Valor numérico o texto a mostrar (ej: "75", "10.000")
    valor: String,
    // Unidad asociada al valor (ej: "kg", "pasos", "hrs")
    unidad: String
) {
    // Contenedor tipo tarjeta para mostrar la estadística
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Sombra ligera
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), // Fondo suave
        modifier = Modifier.padding(vertical = 4.dp) // Espaciado vertical
    ) {
        // Fila que contiene ícono y texto alineados horizontalmente
        Row(
            modifier = Modifier.padding(12.dp), // Espaciado interno
            verticalAlignment = Alignment.CenterVertically // Alineación vertical centrada
        ) {
            // Ícono de la categoría de estadística
            Icon(
                imageVector = icono,
                contentDescription = titulo, // Accesibilidad
                tint = MaterialTheme.colorScheme.primary, // Color basado en el tema
                modifier = Modifier.size(32.dp) // Tamaño del ícono
            )

            Spacer(modifier = Modifier.width(12.dp)) // Espacio entre ícono y texto

            // Columna que contiene título y valor
            Column {
                // Título de la estadística
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.labelMedium
                )

                // Valor y unidad, con estilo destacado
                Text(
                    text = "$valor ${unidad.lowercase()}", // Formato "75 kg"
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}
