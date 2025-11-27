package com.example.vidasalud.ui.screens.bienvenida

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vidasalud.R
import com.example.vidasalud.ui.navigation.RutasApp
import com.example.vidasalud.ui.theme.BotonOscuro

@Composable
fun PantallaBienvenida(controladorNavegacion: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize() // Ocupa toda la pantalla
            .padding(16.dp), // Espaciado exterior
        horizontalAlignment = Alignment.CenterHorizontally, // Centra horizontal
        verticalArrangement = Arrangement.SpaceBetween // Espacia elementos verticalmente
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(60.dp)) // Espacio arriba

            Image(
                painter = painterResource(id = R.drawable.img_onboarding),
                contentDescription = "Persona mejorando su bienestar", // Texto accesibilidad
                modifier = Modifier
                    .fillMaxWidth(0.9f) // Imagen ocupa 90% del ancho
                    .height(320.dp), // Altura fija
                contentScale = ContentScale.Fit // Ajusta imagen sin recortar
            )

            Spacer(modifier = Modifier.height(40.dp)) // Separación

            Text(
                text = "Mejora tu Bienestar Cada Día", // Título principal
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                lineHeight = 36.sp // Altura entre líneas
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Descubre una nueva forma de cuidarte con VidaSalud.", // Subtítulo
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray, // Texto gris para contraste
                fontWeight = FontWeight.Normal
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Navega a pantalla de Login y limpia el stack
                    controladorNavegacion.navigate(RutasApp.PantallaLogin.ruta) {
                        popUpTo(RutasApp.PantallaBienvenida.ruta) {
                            inclusive = true // Elimina pantalla de bienvenida del historial
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth() // Botón ancho completo
                    .height(50.dp), // Altura del botón
                colors = ButtonDefaults.buttonColors(
                    containerColor = BotonOscuro, // Color del botón
                    contentColor = Color.White // Color del texto
                ),
                shape = RoundedCornerShape(12.dp) // Bordes redondeados
            ) {
                Text(
                    text = "Comenzar", // Texto del botón
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Spacer(modifier = Modifier.height(20.dp)) // Espacio final
        }
    }
}
