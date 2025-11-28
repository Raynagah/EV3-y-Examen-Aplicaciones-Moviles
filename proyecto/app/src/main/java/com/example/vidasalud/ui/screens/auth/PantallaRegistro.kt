package com.example.vidasalud.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vidasalud.presentation.auth.AuthViewModel
import com.example.vidasalud.ui.components.ComponenteTextField
import com.example.vidasalud.ui.navigation.RutasApp
import com.example.vidasalud.ui.theme.BotonOscuro

@Composable
fun PantallaRegistro(
    controladorNavegacion: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navegación tras registro exitoso
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
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Únete a VidaSalud hoy",
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Nombre
            ComponenteTextField(
                valor = uiState.nombre,
                enValorCambiado = { viewModel.onNombreChange(it) },
                etiqueta = "Nombre Completo",
                isError = uiState.errorNombre != null,
                errorTexto = uiState.errorNombre
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Email
            ComponenteTextField(
                valor = uiState.email,
                enValorCambiado = { viewModel.onEmailChange(it) },
                etiqueta = "Correo Electrónico",
                isError = uiState.errorEmail != null,
                errorTexto = uiState.errorEmail
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Contraseña
            ComponenteTextField(
                valor = uiState.contrasena,
                enValorCambiado = { viewModel.onContrasenaChange(it) },
                etiqueta = "Contraseña",
                esContrasena = true,
                isError = uiState.errorContrasena != null,
                errorTexto = uiState.errorContrasena
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Confirmar Contraseña
            ComponenteTextField(
                valor = uiState.confirmarContrasena,
                enValorCambiado = { viewModel.onConfirmarContrasenaChange(it) },
                etiqueta = "Confirmar Contraseña",
                esContrasena = true,
                isError = uiState.errorConfirmarContrasena != null,
                errorTexto = uiState.errorConfirmarContrasena
            )

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

            // Botón Registro
            Button(
                onClick = { viewModel.registrarUsuario() },
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
                    Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Volver a Login
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Ya tienes cuenta?", color = Color.Gray)
                TextButton(onClick = { controladorNavegacion.popBackStack() }) {
                    Text("Inicia Sesión", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}