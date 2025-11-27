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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavController
import com.example.vidasalud.R
import com.example.vidasalud.ui.navigation.RutasApp
import com.example.vidasalud.ui.theme.BotonOscuro

@Composable
fun PantallaBienvenida(controladorNavegacion: NavController) {
    // Usamos Scaffold para asegurar que el fondo sea el del Tema (BackgroundLight)
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp), // Margen estándar de 24dp como en las otras pantallas
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(40.dp)) // Un poco más de aire arriba

                // TU IMAGEN ORIGINAL
                Image(
                    painter = painterResource(id = R.drawable.img_onboarding),
                    contentDescription = "Bienestar",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp), // Le damos buen protagonismo
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Mejora tu Bienestar\nCada Día",
                    style = MaterialTheme.typography.headlineMedium, // Usa fuente del tema (Inter)
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Descubre una nueva forma de cuidarte con VidaSalud.",
                    style = MaterialTheme.typography.bodyLarge, // Usa fuente del tema
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.outline // Gris suave del tema
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        controladorNavegacion.navigate(RutasApp.PantallaLogin.ruta) {
                            popUpTo(RutasApp.PantallaBienvenida.ruta) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp), // Altura estándar táctil
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BotonOscuro,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp) // Bordes un poco más suaves
                ) {
                    Text(
                        text = "Comenzar",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}