package com.example.vidasalud.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.vidasalud.presentation.auth.AuthViewModel
import com.example.vidasalud.ui.theme.PrimaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    controladorNavegacion: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { ruta ->
            // Navegación...
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            IconButton(onClick = { controladorNavegacion.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.Black)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            // ESTAS DOS LÍNEAS HACEN EL CENTRADO PERFECTO COMO EN LOGIN
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // TITULARES (Centrados)
            Text(
                text = "Crea tu cuenta",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                lineHeight = 40.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Comienza tu viaje saludable hoy.",
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // -- INPUTS --

            // Nombre
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryLight,
                    unfocusedBorderColor = Color.LightGray
                ),
                isError = uiState.errorNombre != null
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                isError = uiState.errorEmail != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pass
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

            Spacer(modifier = Modifier.height(16.dp))

            // Confirmar Pass
            OutlinedTextField(
                value = uiState.confirmarContrasena,
                onValueChange = { viewModel.onConfirmarContrasenaChange(it) },
                label = { Text("Repetir Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = PrimaryLight,
                    unfocusedBorderColor = Color.LightGray
                ),
                isError = uiState.errorConfirmarContrasena != null,
                visualTransformation = PasswordVisualTransformation()
            )

            if (uiState.errorGeneral != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(uiState.errorGeneral!!, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // BOTÓN
            Button(
                onClick = { viewModel.registrarUsuario() },
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
                    Text("Registrarse", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // FOOTER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Ya tienes una cuenta? ", color = Color.Gray)
                Text(
                    text = "Inicia Sesión",
                    color = PrimaryLight,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { controladorNavegacion.popBackStack() }
                )
            }

            // Espacio final para asegurar que se vea bien en pantallas pequeñas
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}