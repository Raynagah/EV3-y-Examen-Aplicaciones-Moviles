package com.example.vidasalud.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vidasalud.presentation.auth.AuthViewModel
import com.example.vidasalud.ui.components.ComponenteTextField
import com.example.vidasalud.ui.components.EncabezadoCurvo
import com.example.vidasalud.ui.theme.BotonOscuro

@Composable
fun PantallaRegistro(
    controladorNavegacion: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { ruta ->
            // Manejo de navegación...
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFFBFDF8))) {
        EncabezadoCurvo(modifier = Modifier.align(Alignment.TopCenter))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            // AQUÍ JUGAMOS CON EL ESPACIO PARA CENTRAR
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(40.dp)) // Bajamos un poco el título

            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Únete a VidaSalud hoy",
                color = Color.White.copy(alpha = 0.9f)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // TARJETA DE REGISTRO
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    ComponenteTextField(
                        valor = uiState.nombre,
                        enValorCambiado = { viewModel.onNombreChange(it) },
                        etiqueta = "Nombre Completo",
                        isError = uiState.errorNombre != null,
                        errorTexto = uiState.errorNombre
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ComponenteTextField(
                        valor = uiState.email,
                        enValorCambiado = { viewModel.onEmailChange(it) },
                        etiqueta = "Correo Electrónico",
                        isError = uiState.errorEmail != null,
                        errorTexto = uiState.errorEmail
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ComponenteTextField(
                        valor = uiState.contrasena,
                        enValorCambiado = { viewModel.onContrasenaChange(it) },
                        etiqueta = "Contraseña",
                        esContrasena = true,
                        isError = uiState.errorContrasena != null,
                        errorTexto = uiState.errorContrasena
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ComponenteTextField(
                        valor = uiState.confirmarContrasena,
                        enValorCambiado = { viewModel.onConfirmarContrasenaChange(it) },
                        etiqueta = "Confirmar Contraseña",
                        esContrasena = true,
                        isError = uiState.errorConfirmarContrasena != null,
                        errorTexto = uiState.errorConfirmarContrasena
                    )

                    if (uiState.errorGeneral != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = uiState.errorGeneral!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { viewModel.registrarUsuario() },
                        enabled = !uiState.isLoading,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BotonOscuro)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Registrarse", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("¿Ya tienes una cuenta?", color = Color.Gray)
                TextButton(onClick = { controladorNavegacion.popBackStack() }) {
                    Text("Inicia Sesión", fontWeight = FontWeight.Bold)
                }
            }

            // Espacio final para que se pueda scrollear bien
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}