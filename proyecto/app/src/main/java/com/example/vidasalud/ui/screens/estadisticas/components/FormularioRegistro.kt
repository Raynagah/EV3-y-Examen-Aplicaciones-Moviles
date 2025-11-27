package com.example.vidasalud.ui.screens.estadisticas.components

// Importaciones necesarias para componentes de UI, estilos, íconos y estados de Compose
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioRegistro(
    // Valores actuales ingresados por el usuario
    peso: String,
    calorias: String,
    sueno: String,
    pasos: String,
    // Mensajes de error asociados a cada campo (si existen)
    errorPeso: String?,
    errorCalorias: String?,
    errorSueno: String?,
    errorPasos: String?,
    // Funciones para actualizar los valores en el ViewModel
    onPesoChange: (String) -> Unit,
    onCaloriasChange: (String) -> Unit,
    onSuenoChange: (String) -> Unit,
    onPasosChange: (String) -> Unit,
    // Acción cuando se presiona el botón Guardar y estado de carga
    onGuardarClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    // Tarjeta contenedora del formulario
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Estructura vertical para organizar los campos
        Column(modifier = Modifier.padding(16.dp)) {

            // Título del formulario
            Text(
                text = "Ingresar Datos Diarios",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Campo de Peso (kg) ---
            OutlinedTextField(
                value = peso,
                onValueChange = onPesoChange, // Envía el nuevo valor al ViewModel
                label = { Text("Peso (kg)") },
                leadingIcon = { Icon(Icons.Default.MonitorWeight, contentDescription = "Peso") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), // Permite decimales
                modifier = Modifier.fillMaxWidth(),
                isError = errorPeso != null, // Activa estilo de error si existe mensaje
                supportingText = {
                    if (errorPeso != null) {
                        Text(text = errorPeso) // Mensaje de error visible para el usuario
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- Campo de Calorías Consumidas ---
            OutlinedTextField(
                value = calorias,
                onValueChange = onCaloriasChange,
                label = { Text("Calorías Consumidas (kcal)") },
                leadingIcon = { Icon(Icons.Default.Fastfood, contentDescription = "Calorías") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Solo números enteros
                modifier = Modifier.fillMaxWidth(),
                isError = errorCalorias != null,
                supportingText = {
                    if (errorCalorias != null) {
                        Text(text = errorCalorias)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- Campo de Horas de Sueño ---
            OutlinedTextField(
                value = sueno,
                onValueChange = onSuenoChange,
                label = { Text("Horas de Sueño") },
                leadingIcon = { Icon(Icons.Default.WbSunny, contentDescription = "Sueño") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
                isError = errorSueno != null,
                supportingText = {
                    if (errorSueno != null) {
                        Text(text = errorSueno)
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))

            // --- Campo de Pasos manuales ---
            OutlinedTextField(
                value = pasos,
                onValueChange = onPasosChange,
                label = { Text("Pasos (Manual)") },
                leadingIcon = { Icon(Icons.Default.DirectionsRun, contentDescription = "Pasos") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = errorPasos != null,
                supportingText = {
                    if (errorPasos != null) {
                        Text(text = errorPasos)
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // --- Botón Guardar Registro ---
            Button(
                onClick = onGuardarClick,
                enabled = !isLoading, // Se desactiva mientras carga
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                // Muestra un loader si está procesando
                if (isLoading) {
                    CircularProgressIndicator(Modifier.size(24.dp))
                } else {
                    Text("Guardar Registro")
                }
            }
        }
    }
}
