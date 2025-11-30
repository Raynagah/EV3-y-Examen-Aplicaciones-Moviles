package com.example.vidasalud.ui.screens.principal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vidasalud.presentation.principal.ResumenViewModel
import java.util.Calendar

@Composable
fun PantallaPrincipal(
    viewModel: ResumenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var visible by remember { mutableStateOf(false) }

    // --- PALETA DE DEGRADADOS ---
    val DegradadoPasos = Brush.linearGradient(colors = listOf(Color(0xFF43A047), Color(0xFF1DE9B6)))
    val DegradadoAgua = Brush.verticalGradient(colors = listOf(Color(0xFF4FC3F7), Color(0xFF0288D1)))
    val DegradadoSueno = Brush.verticalGradient(colors = listOf(Color(0xFF9575CD), Color(0xFF512DA8)))

    LaunchedEffect(Unit) {
        visible = true
        viewModel.recargarDatos()
    }

    // --- DIÁLOGOS DE EDICIÓN ---
    var showDialogMeta by remember { mutableStateOf(false) }
    var showDialogAgua by remember { mutableStateOf(false) }
    var showDialogSueno by remember { mutableStateOf(false) }

    // --- LÓGICA CORREGIDA DE DIÁLOGOS ---
    if (showDialogMeta) {
        DialogoInput(
            titulo = "Meta de Pasos",
            valorActual = uiState.metaPasos.toString(),
            onDismiss = { showDialogMeta = false }, // Ahora sí avisa al padre que se cerró
            onConfirm = {
                viewModel.actualizarMetaPasos(it.toIntOrNull() ?: 5000)
                showDialogMeta = false
            }
        )
    }
    if (showDialogAgua) {
        DialogoInput(
            titulo = "Agua (Litros)",
            valorActual = uiState.aguaConsumida.toString(),
            onDismiss = { showDialogAgua = false },
            onConfirm = {
                viewModel.actualizarAgua(it.toDoubleOrNull() ?: 0.0)
                showDialogAgua = false
            }
        )
    }
    if (showDialogSueno) {
        DialogoInput(
            titulo = "Sueño (Horas)",
            valorActual = uiState.ultimoSueno.toString(),
            onDismiss = { showDialogSueno = false },
            onConfirm = {
                viewModel.actualizarSueno(it.toDoubleOrNull() ?: 0.0)
                showDialogSueno = false
            }
        )
    }

    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // HEADER
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = obtenerSaludo(),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = uiState.nombreUsuario,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
                // Botón Configuración Meta (Clickable)
                IconButton(
                    onClick = { showDialogMeta = true },
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .shadow(2.dp, CircleShape)
                ) {
                    Icon(Icons.Default.Settings, null, tint = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- TARJETA PRINCIPAL (GRÁFICO PASOS) ---
            AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .shadow(10.dp, RoundedCornerShape(32.dp))
                        .background(DegradadoPasos, RoundedCornerShape(32.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Actividad Diaria",
                            color = Color.White.copy(alpha = 0.9f),
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        val progreso = (uiState.ultimosPasos.toFloat() / uiState.metaPasos.toFloat()).coerceIn(0f, 1f)
                        Box(contentAlignment = Alignment.Center) {
                            CircularProgressAnimated(1f, 170.dp, Color.White.copy(0.3f), 14.dp)
                            CircularProgressAnimated(progreso, 170.dp, Color.White, 14.dp)

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.DirectionsRun, null, tint = Color.White, modifier = Modifier.size(32.dp))
                                Text(
                                    text = "${uiState.ultimosPasos}",
                                    fontSize = 42.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text("pasos", color = Color.White.copy(0.8f))
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Text("Meta: ${uiState.metaPasos}", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Tus Hábitos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            // --- TARJETAS EDITABLES (AGUA Y SUEÑO) ---
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TarjetaHabitoInteractivo(
                    modifier = Modifier.weight(1f),
                    titulo = "Hidratación",
                    valor = "${uiState.aguaConsumida} / 2.0 L",
                    icono = Icons.Default.WaterDrop,
                    brush = DegradadoAgua,
                    alerta = uiState.aguaConsumida < 1.0,
                    onClick = { showDialogAgua = true }
                )
                TarjetaHabitoInteractivo(
                    modifier = Modifier.weight(1f),
                    titulo = "Descanso",
                    valor = "${uiState.ultimoSueno} / 8 h",
                    icono = Icons.Default.Bedtime,
                    brush = DegradadoSueno,
                    alerta = uiState.ultimoSueno < 6.0,
                    onClick = { showDialogSueno = true }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- CONSEJO CUALITATIVO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFFFF3E0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Lightbulb, null, tint = Color(0xFFFF9800))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = uiState.recomendacion,
                        color = Color.DarkGray,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

// --- COMPONENTES ---

@Composable
fun TarjetaHabitoInteractivo(
    modifier: Modifier,
    titulo: String,
    valor: String,
    icono: ImageVector,
    brush: Brush,
    alerta: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize().background(brush)) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Icon(icono, null, tint = Color.White)
                    if (alerta) {
                        Text("⚠️", fontSize = 18.sp)
                    }
                }
                Column {
                    Text(valor, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text(titulo, color = Color.White.copy(0.8f), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun CircularProgressAnimated(progreso: Float, size: Dp, color: Color, strokeWidth: Dp) {
    val animatedProgress by animateFloatAsState(targetValue = progreso, animationSpec = tween(1500))
    Canvas(modifier = Modifier.size(size)) {
        drawArc(color = color, startAngle = -90f, sweepAngle = animatedProgress * 360f, useCenter = false, style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round))
    }
}

// DIÁLOGO CORREGIDO (Sin estado interno que bloquee la reapertura)
@Composable
fun DialogoInput(
    titulo: String,
    valorActual: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var texto by remember { mutableStateOf(valorActual) }

    AlertDialog(
        onDismissRequest = onDismiss, // IMPORTANTE: Al cancelar, resetea el estado del padre
        title = { Text(titulo) },
        text = {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(texto) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

fun obtenerSaludo(): String {
    val hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    return when (hora) {
        in 6..11 -> "Buenos días,"
        in 12..19 -> "Buenas tardes,"
        else -> "Buenas noches,"
    }
}