package com.example.vidasalud.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.example.vidasalud.ui.theme.PrimaryLight

@Composable
fun EncabezadoCurvo(
    modifier: Modifier = Modifier,
    color: Color = PrimaryLight
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp) // Altura de la cabecera
    ) {
        val path = Path().apply {
            // Empezamos arriba a la izquierda
            moveTo(0f, 0f)
            // Línea hasta abajo (pero no todo el alto)
            lineTo(0f, size.height * 0.7f)

            // Curva de Bezier para hacer la onda suave
            quadraticBezierTo(
                size.width * 0.5f, // Punto de control X (centro)
                size.height * 1.0f, // Punto de control Y (más abajo)
                size.width,        // Punto final X (derecha)
                size.height * 0.7f // Punto final Y (misma altura que inicio)
            )

            // Cerrar la forma
            lineTo(size.width, 0f)
            close()
        }
        drawPath(path = path, color = color)
    }
}