package com.example.vidasalud.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vidasalud.R
import com.example.vidasalud.presentation.auth.AuthViewModel
import com.example.vidasalud.ui.components.ComponenteTextField
import com.example.vidasalud.ui.navigation.RutasApp
import com.example.vidasalud.ui.theme.BotonOscuro

@Composable
fun PantallaLogin(
    controladorNavegacion: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Escuchar eventos de navegación (Login exitoso)
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { ruta ->
            controladorNavegacion.navigate(ruta) {
                popUpTo(RutasApp.PantallaBienvenida.ruta) { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()), // Scroll por si el teclado tapa
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Logo o Imagen
            Image(
                painter = painterResource(id = R.drawable.img_onboarding), // Reusamos la imagen o pon tu logo
                contentDescription = "Login",
                modifier = Modifier.height(200.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Bienvenido de nuevo",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Campo Email
            ComponenteTextField(
                valor = uiState.email,
                enValorCambiado = { viewModel.onEmailChange(it) },
                etiqueta = "Correo Electrónico",
                isError = uiState.errorEmail != null,
                errorTexto = uiState.errorEmail
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            ComponenteTextField(
                valor = uiState.contrasena,
                enValorCambiado = { viewModel.onContrasenaChange(it) },
                etiqueta = "Contraseña",
                esContrasena = true,
                isError = uiState.errorContrasena != null,
                errorTexto = uiState.errorContrasena
            )

            // Mensaje de Error General (Login fallido)
            if (uiState.errorGeneral != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.errorGeneral!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Login
            Button(
                onClick = { viewModel.iniciarSesion() }, // <--- AQUÍ ESTABA EL ERROR
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BotonOscuro)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Iniciar Sesión", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ir a Registro
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿No tienes cuenta?", color = Color.Gray)
                TextButton(onClick = { controladorNavegacion.navigate(RutasApp.PantallaRegistro.ruta) }) {
                    Text("Regístrate", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}