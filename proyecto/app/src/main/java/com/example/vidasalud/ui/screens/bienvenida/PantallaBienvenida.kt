package com.example.vidasalud.ui.screens.bienvenida

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.example.vidasalud.ui.theme.PrimaryLight

@Composable
fun PantallaBienvenida(controladorNavegacion: NavController) {
    // Fondo con un degradado muy sutil (Casi blanco a un verde menta muy pálido)
    val fondoSuave = Brush.verticalGradient(
        colors = listOf(Color.White, Color(0xFFF0F7F4))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondoSuave)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // IMAGEN CENTRAL (Protagonista)
        Image(
            painter = painterResource(id = R.drawable.img_onboarding),
            contentDescription = "Yoga",
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(40.dp))

        // TEXTOS (Alineación izquierda para toque moderno o centro para clásico)
        Text(
            text = "Tu salud,\ntu mejor proyecto",
            style = MaterialTheme.typography.displaySmall, // Texto muy grande
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1A1A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Gestiona tus hábitos, monitorea tu sueño y únete a una comunidad activa.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        // BOTÓN MODERNO (Ancho completo, sin sombras excesivas)
        Button(
            onClick = {
                controladorNavegacion.navigate(RutasApp.PantallaLogin.ruta) {
                    popUpTo(RutasApp.PantallaBienvenida.ruta) { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryLight // Tu verde principal
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 2.dp) // Flat design
        ) {
            Text(
                text = "Comenzar ahora",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}