package com.example.vidasalud.ui.screens.estadisticas

// Dependencias necesarias para UI, ViewModel y fecha
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vidasalud.presentation.registro.RegistroViewModel
import com.example.vidasalud.ui.screens.estadisticas.components.FormularioRegistro
import com.example.vidasalud.ui.screens.estadisticas.components.TarjetaResumenEstadisticas
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

// Modelo para mostrar los datos en tarjetas
data class DatoResumen(val icono: ImageVector, val titulo: String, val valor: String, val unidad: String)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEstadisticas(
    viewModel: RegistroViewModel = viewModel() // Obtiene el ViewModel
) {
    val uiState by viewModel.uiState.collectAsState() // Observa el estado UI
    val snackbarHostState = remember { SnackbarHostState() } // Snackbar para mensajes
    val showDatePicker = remember { mutableStateOf(false) } // Controla popup de fecha
    val context = LocalContext.current

    // Estado inicial del date picker (fecha elegida)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.fechaSeleccionada
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        // No usamos 'dateValidator'
    )

    // Efecto para mostrar mensajes de éxito/error
    LaunchedEffect(uiState.mensajeExito, uiState.mensajeError) {
        val mensaje = uiState.mensajeExito ?: uiState.mensajeError
        mensaje?.let {
            snackbarHostState.showSnackbar(message = it, duration = SnackbarDuration.Short)
            viewModel.clearMessages() // Limpia mensajes luego de mostrarlos
        }
    }

    // Si se debe mostrar el selector de fecha
    if (showDatePicker.value) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        // 1. Obtener la fecha seleccionada
                        val selectedDate = Instant.ofEpochMilli(datePickerState.selectedDateMillis ?: 0L)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()

                        // 2. Obtener la fecha de hoy
                        val today = LocalDate.now()

                        // 3. Comparar las fechas
                        if (selectedDate.isAfter(today)) {
                            // 4. Si es una fecha futura, mostrar error y NO cerrar
                            Toast.makeText(
                                context,
                                "No puedes seleccionar una fecha futura.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // 5. Si la fecha es válida, actualizar y cerrar
                            viewModel.onFechaSeleccionada(selectedDate) // Actualiza fecha en ViewModel
                            showDatePicker.value = false
                        }
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker.value = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState) // Muestra selector visual
        }
    }

    // Construye lista dinámica de estadísticas con valores actuales
    val datosResumenDinamicos = remember(uiState.pasos, uiState.calorias, uiState.peso, uiState.sueno) {
        listOf(
            DatoResumen(Icons.Default.DirectionsWalk, "Pasos", uiState.pasos.ifEmpty { "0" }, ""),
            DatoResumen(Icons.Default.LocalFireDepartment, "Calorías", uiState.calorias.ifEmpty { "0" }, "kcal"),
            DatoResumen(Icons.Default.MonitorWeight, "Peso", uiState.peso.ifEmpty { "0.0" }, "kg"),
            DatoResumen(Icons.Default.Bedtime, "Sueño", uiState.sueno.ifEmpty { "0.0" }, "h"),
        )
    }

    // Estructura principal de la pantalla
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Datos", fontWeight = FontWeight.Bold) }, // Título superior
                actions = {
                    // Mostrar la fecha actual seleccionada
                    Text(
                        text = uiState.fechaSeleccionada.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { showDatePicker.value = true }) { // Botón para seleccionar fecha
                        Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar Fecha")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()) // Scroll manual
            ) {

                // Gráfico indicador (placeholder por ahora)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Placeholder Gráfico") // Texto temporal
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Resumen del Día",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Grid con tarjetas de resumen (2 columnas)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth().height(200.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(datosResumenDinamicos) { dato ->
                        TarjetaResumenEstadisticas(
                            icono = dato.icono,
                            titulo = dato.titulo,
                            valor = dato.valor,
                            unidad = dato.unidad
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Formulario para ingresar datos diarios
                FormularioRegistro(
                    peso = uiState.peso,
                    calorias = uiState.calorias,
                    sueno = uiState.sueno,
                    pasos = uiState.pasos,
                    errorPeso = uiState.errorPeso,
                    errorCalorias = uiState.errorCalorias,
                    errorSueno = uiState.errorSueno,
                    errorPasos = uiState.errorPasos,
                    onPesoChange = viewModel::onPesoChange,
                    onCaloriasChange = viewModel::onCaloriasChange,
                    onSuenoChange = viewModel::onSuenoChange,
                    onPasosChange = viewModel::onPasosChange,
                    onGuardarClick = viewModel::guardarRegistro, // Guarda datos
                    isLoading = uiState.isLoading,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(100.dp)) // Espacio final
            }
        }
    }
}