package com.example.vidasalud.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.vidasalud.presentation.auth.AuthViewModel
import com.example.vidasalud.ui.components.ComponenteTextField
import com.example.vidasalud.ui.navigation.RutasApp
import com.example.vidasalud.ui.theme.BotonOscuro
import com.example.vidasalud.ui.theme.VidaSaludTheme

@Composable
fun PantallaLogin(
    controladorNavegacion: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    // Estado de UI expuesto desde el ViewModel (email, pass, errores, loading, etc)
    val uiState by viewModel.uiState.collectAsState()

    // Escucha eventos de navegación emitidos por el ViewModel
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { ruta ->
            controladorNavegacion.navigate(ruta) {
                // Limpia pantallas anteriores para evitar volver atrás
                popUpTo(RutasApp.PantallaBienvenida.ruta) {
                    inclusive = true
                }
            }
        }
    }

    // UI Principal
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "¡Bienvenido de Nuevo!",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )
            )
            // Subtítulo
            Text(
                text = "Inicia sesión para continuar",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Campo de Email
            ComponenteTextField(
                valor = uiState.email,
                enValorCambiado = viewModel::onEmailChange,
                etiqueta = "Email",
                esContrasena = false,
                isError = uiState.errorEmail != null,
                errorTexto = uiState.errorEmail,
                enabled = !uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de contraseña
            ComponenteTextField(
                valor = uiState.contrasena,
                enValorCambiado = viewModel::onContrasenaChange,
                etiqueta = "Contraseña",
                esContrasena = true,
                isError = uiState.errorContrasena != null,
                errorTexto = uiState.errorContrasena,
                enabled = !uiState.isLoading
            )

            // Muestra error general si existe
            if (uiState.errorGeneral != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.errorGeneral!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de inicio de sesión
            Button(
                onClick = {
                    viewModel.onLoginClicked() // Llama al método de login
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BotonOscuro,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = MaterialTheme.shapes.medium,
                enabled = !uiState.isLoading // Deshabilita si está cargando
            ) {
                // Si está cargando, mostrar loader
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Enlace a registro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes una cuenta?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(4.dp))
                TextButton(
                    onClick = {
                        controladorNavegacion.navigate(RutasApp.PantallaRegistro.ruta)
                    },
                    enabled = !uiState.isLoading
                ) {
                    Text(
                        text = "Regístrate",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}