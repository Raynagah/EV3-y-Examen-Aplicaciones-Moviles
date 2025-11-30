package com.example.vidasalud.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vidasalud.presentation.auth.AuthViewModel
import com.example.vidasalud.ui.navigation.RutasApp
import com.example.vidasalud.ui.theme.PrimaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLogin(
    controladorNavegacion: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { ruta ->
            controladorNavegacion.navigate(ruta) {
                popUpTo(RutasApp.PantallaBienvenida.ruta) { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            // Espacio o botón atrás si lo deseas, por ahora vacío para limpieza
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()), // Habilita scroll para pantallas pequeñas
            // ESTAS DOS LÍNEAS GARANTIZAN EL CENTRADO PERFECTO
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // TÍTULOS (Centrados)
            Text(
                text = "Bienvenido de vuelta",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                lineHeight = 40.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Ingresa tus credenciales para continuar.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // -- INPUTS --

            // Email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryLight,
                    unfocusedBorderColor = Color.LightGray
                ),
                isError = uiState.errorEmail != null
            )
            if (uiState.errorEmail != null) {
                Text(
                    text = uiState.errorEmail!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.Start) // Error alineado al inicio del campo
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Contraseña
            OutlinedTextField(
                value = uiState.contrasena,
                onValueChange = { viewModel.onContrasenaChange(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryLight,
                    unfocusedBorderColor = Color.LightGray
                ),
                isError = uiState.errorContrasena != null,
                visualTransformation = PasswordVisualTransformation()
            )
            if (uiState.errorContrasena != null) {
                Text(
                    text = uiState.errorContrasena!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.align(Alignment.Start)
                )
            }

            if (uiState.errorGeneral != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = uiState.errorGeneral!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // BOTÓN PRINCIPAL
            Button(
                onClick = { viewModel.iniciarSesion() },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryLight),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Ingresar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // FOOTER (Registro)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿No tienes una cuenta? ", color = Color.Gray)
                Text(
                    text = "Crea una cuenta",
                    color = PrimaryLight,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { controladorNavegacion.navigate(RutasApp.PantallaRegistro.ruta) }
                )
            }

            // Espacio final seguro
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}