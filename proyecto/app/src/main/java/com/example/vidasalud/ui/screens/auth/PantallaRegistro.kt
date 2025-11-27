package com.example.vidasalud.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistro(
    controladorNavegacion: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState() // Observa el estado del ViewModel

    LaunchedEffect(Unit) {
        // Escucha eventos para navegar tras registro exitoso
        viewModel.navigationEvent.collect { ruta ->
            controladorNavegacion.navigate(ruta) {
                popUpTo(RutasApp.PantallaBienvenida.ruta) {
                    inclusive = true // Elimina pantallas previas del stack
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") }, // Barra sin título
                navigationIcon = {
                    IconButton(
                        onClick = {
                            controladorNavegacion.popBackStack() // Regresa a pantalla anterior
                        },
                        enabled = !uiState.isLoading // Deshabilita si está cargando
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver a Login"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Respeta barra superior
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp), // Márgenes laterales
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Crea tu Cuenta",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center
                    )
                )
                Text(
                    text = "Únete a la comunidad de VidaSalud",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Campo Nombre
                ComponenteTextField(
                    valor = uiState.nombre,
                    enValorCambiado = viewModel::onNombreChange,
                    etiqueta = "Nombre",
                    esContrasena = false,
                    isError = uiState.errorNombre != null,
                    errorTexto = uiState.errorNombre,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Email
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

                // Campo Contraseña
                ComponenteTextField(
                    valor = uiState.contrasena,
                    enValorCambiado = viewModel::onContrasenaChange,
                    etiqueta = "Contraseña",
                    esContrasena = true,
                    isError = uiState.errorContrasena != null,
                    errorTexto = uiState.errorContrasena,
                    enabled = !uiState.isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Campo Confirmar contraseña
                ComponenteTextField(
                    valor = uiState.confirmarContrasena,
                    enValorCambiado = viewModel::onConfirmarContrasenaChange,
                    etiqueta = "Confirmar Contraseña",
                    esContrasena = true,
                    isError = uiState.errorConfirmarContrasena != null,
                    errorTexto = uiState.errorConfirmarContrasena,
                    enabled = !uiState.isLoading
                )

                // Muestra error general
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

                // Botón crear cuenta
                Button(
                    onClick = { viewModel.onRegistroClicked() }, // Ejecuta registro
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BotonOscuro,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium,
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = "Crear Cuenta",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Enlace para ir al login
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "¿Ya tienes una cuenta?")

                    Spacer(modifier = Modifier.width(4.dp))

                    TextButton(
                        onClick = { controladorNavegacion.popBackStack() },
                        enabled = !uiState.isLoading
                    ) {
                        Text(
                            text = "Inicia Sesión",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaRegistroPreview() {
    VidaSaludTheme {
        PantallaRegistro(controladorNavegacion = rememberNavController())
    }
}
