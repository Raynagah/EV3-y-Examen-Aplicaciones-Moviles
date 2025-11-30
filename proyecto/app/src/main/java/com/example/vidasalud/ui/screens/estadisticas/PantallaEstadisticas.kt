package com.example.vidasalud.ui.screens.estadisticas

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vidasalud.presentation.registro.RegistroViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEstadisticas(
    viewModel: RegistroViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Estados para diálogos
    var campoEditando by remember { mutableStateOf<String?>(null) }
    var valorEditando by remember { mutableStateOf("") }

    // Estado para el Calendario (DatePicker)
    var mostrarCalendario by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    Scaffold(containerColor = Color(0xFFF8F9FA)) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                // Quitamos el lazy grid interno y usamos scroll columna principal para evitar conflictos visuales
                .verticalScroll(rememberScrollState())
        ) {

            // --- HEADER CON CALENDARIO FUNCIONAL ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Mi Diario", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Registra tu progreso", color = Color.Gray)
                }

                // Botón Fecha (Ahora abre el calendario)
                Surface(
                    onClick = { mostrarCalendario = true }, // <--- AQUI ABRE EL DIALOGO
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 2.dp
                ) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CalendarToday, null, tint = Color(0xFF00C853), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        // Formato fecha amigable
                        val fechaTexto = uiState.fechaSeleccionada.format(DateTimeFormatter.ofPattern("dd MMM"))
                        Text(fechaTexto, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- DASHBOARD GRID (Diseño ajustado) ---
            // Usamos un layout de filas y columnas manual para mejor control dentro del scroll
            val items = listOf(
                DashboardItem("Pasos", uiState.pasos.ifBlank { "0" }, "pasos", Icons.Default.DirectionsRun, Color(0xFF4CAF50), "pasos"),
                DashboardItem("Calorías", uiState.calorias.ifBlank { "0" }, "kcal", Icons.Default.LocalFireDepartment, Color(0xFFFF5722), "calorias"),
                DashboardItem("Sueño", uiState.sueno.ifBlank { "0" }, "h", Icons.Default.Bedtime, Color(0xFF673AB7), "sueno"),
                DashboardItem("Peso", uiState.peso.ifBlank { "--" }, "kg", Icons.Default.MonitorWeight, Color(0xFF2196F3), "peso")
            )

            // Fila 1
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TarjetaDashboard(items[0], Modifier.weight(1f)) { campoEditando = items[0].idCampo; valorEditando = items[0].valor }
                TarjetaDashboard(items[1], Modifier.weight(1f)) { campoEditando = items[1].idCampo; valorEditando = items[1].valor }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Fila 2
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TarjetaDashboard(items[2], Modifier.weight(1f)) { campoEditando = items[2].idCampo; valorEditando = items[2].valor }
                TarjetaDashboard(items[3], Modifier.weight(1f)) { campoEditando = items[3].idCampo; valorEditando = items[3].valor }
            }

            // --- YA NO HAY ESPACIO VACÍO GIGANTE ---
            Spacer(modifier = Modifier.height(30.dp))

            // Botón Guardar (Ahora está visible justo debajo)
            Button(
                onClick = { viewModel.guardarRegistro() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A1A))
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Guardar Registro Diario", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            // Espacio final para scroll cómodo
            Spacer(modifier = Modifier.height(100.dp))
        }
    }

    // --- DIÁLOGO DEL CALENDARIO ---
    if (mostrarCalendario) {
        DatePickerDialog(
            onDismissRequest = { mostrarCalendario = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedDateMillis = datePickerState.selectedDateMillis
                        if (selectedDateMillis != null) {
                            val fechaSeleccionada = Instant.ofEpochMilli(selectedDateMillis)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            viewModel.onFechaSeleccionada(fechaSeleccionada)
                        }
                        mostrarCalendario = false
                    }
                ) { Text("Aceptar") }
            },
            dismissButton = {
                TextButton(onClick = { mostrarCalendario = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // --- DIÁLOGO DE EDICIÓN DE DATOS ---
    if (campoEditando != null) {
        AlertDialog(
            onDismissRequest = { campoEditando = null },
            title = { Text("Editar ${campoEditando?.capitalize()}") },
            text = {
                Column {
                    Text("Ingresa el nuevo valor:", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = valorEditando,
                        onValueChange = { valorEditando = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    when (campoEditando) {
                        "pasos" -> viewModel.onPasosChange(valorEditando)
                        "calorias" -> viewModel.onCaloriasChange(valorEditando)
                        "sueno" -> viewModel.onSuenoChange(valorEditando)
                        "peso" -> viewModel.onPesoChange(valorEditando)
                    }
                    campoEditando = null
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { campoEditando = null }) { Text("Cancelar") }
            }
        )
    }
}

data class DashboardItem(val titulo: String, val valor: String, val unidad: String, val icono: ImageVector, val color: Color, val idCampo: String)

@Composable
fun TarjetaDashboard(item: DashboardItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .height(140.dp) // Altura controlada
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween, // Distribuye espacio equitativamente
            horizontalAlignment = Alignment.Start
        ) {
            // Icono con fondo circular suave
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(item.color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icono, null, tint = item.color, modifier = Modifier.size(20.dp))
            }

            // Textos (Ajustados para no cortarse)
            Column {
                Text(
                    text = item.valor,
                    fontSize = 24.sp, // Tamaño un poco más moderado
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    maxLines = 1 // Evita que salte de línea y empuje
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.titulo,
                        color = item.color,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "(${item.unidad})",
                        color = Color.Gray,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}