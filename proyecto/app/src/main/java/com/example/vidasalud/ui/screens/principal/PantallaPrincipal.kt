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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vidasalud.presentation.principal.ResumenViewModel
import com.example.vidasalud.ui.theme.PrimaryLight
import java.util.Calendar

@Composable
fun PantallaPrincipal(
    viewModel: ResumenViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        viewModel.recargarDatos()
    }

    var mostrarDialogoMeta by remember { mutableStateOf(false) }

    if (mostrarDialogoMeta) {
        DialogoEditarMeta(
            metaActual = uiState.metaPasos,
            onDismiss = { mostrarDialogoMeta = false },
            onConfirm = { nuevaMeta ->
                viewModel.actualizarMetaPasos(nuevaMeta)
                mostrarDialogoMeta = false
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(scrollState)
        ) {
            // --- HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Hola,", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                    Text(
                        text = uiState.nombreUsuario,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Botón discreto para editar meta
                IconButton(onClick = { mostrarDialogoMeta = true }) {
                    Icon(Icons.Default.Edit, contentDescription = "Meta", tint = PrimaryLight)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- SECCIÓN VISUAL GRANDE: PROGRESO DIARIO ---
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(1000)) + slideInVertically(tween(1000)) { 50 }
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                ) {
                    val progreso = (uiState.ultimosPasos.toFloat() / uiState.metaPasos.toFloat()).coerceIn(0f, 1f)

                    // Gráfico Circular Customizado
                    CircularProgressAnimated(progreso = progreso, size = 180.dp)

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.DirectionsRun, contentDescription = null, tint = PrimaryLight, modifier = Modifier.size(32.dp))
                        Text(
                            text = "${uiState.ultimosPasos}",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "/ ${uiState.metaPasos} pasos",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- TARJETA CONSEJO ---
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(tween(800)) + slideInVertically(tween(800)) { 100 }
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.TipsAndUpdates, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Consejo del día", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uiState.recomendacion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Métricas Clave", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            // --- GRID DE METRICAS ---
            Row(modifier = Modifier.fillMaxWidth()) {
                TarjetaDato(
                    modifier = Modifier.weight(1f),
                    titulo = "Sueño",
                    valor = "${uiState.ultimoSueno}h",
                    icono = Icons.Default.Bedtime,
                    colorFondo = Color(0xFFE1BEE7), // Lila suave
                    colorIcono = Color(0xFF4A148C)
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Tarjeta decorativa extra para llenar espacio
                TarjetaDato(
                    modifier = Modifier.weight(1f),
                    titulo = "Hidratación",
                    valor = "2.0L", // Valor simulado
                    icono = Icons.Default.WaterDrop,
                    colorFondo = Color(0xFFB3E5FC), // Azul suave
                    colorIcono = Color(0xFF01579B)
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun CircularProgressAnimated(progreso: Float, size: Dp) {
    val animatedProgress by animateFloatAsState(
        targetValue = progreso,
        animationSpec = tween(durationMillis = 1500)
    )
    val colorPrincipal = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.size(size)) {
        // Fondo del círculo (gris claro)
        drawArc(
            color = Color.LightGray.copy(alpha = 0.3f),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
        )
        // Progreso (Verde)
        drawArc(
            color = colorPrincipal,
            startAngle = -90f,
            sweepAngle = animatedProgress * 360f,
            useCenter = false,
            style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Composable
fun TarjetaDato(
    modifier: Modifier = Modifier,
    titulo: String,
    valor: String,
    icono: ImageVector,
    colorFondo: Color,
    colorIcono: Color
) {
    Card(
        modifier = modifier.height(110.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo)
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(imageVector = icono, contentDescription = null, tint = colorIcono)
            Column {
                Text(text = valor, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
                Text(text = titulo, fontSize = 12.sp, color = Color.DarkGray)
            }
        }
    }
}

@Composable
fun DialogoEditarMeta(metaActual: Int, onDismiss: () -> Unit, onConfirm: (Int) -> Unit) {
    var textoMeta by remember { mutableStateOf(metaActual.toString()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Meta Diaria") },
        text = { OutlinedTextField(value = textoMeta, onValueChange = { if (it.all { c -> c.isDigit() }) textoMeta = it }, label = { Text("Pasos") }) },
        confirmButton = { Button(onClick = { onConfirm(textoMeta.toIntOrNull() ?: metaActual) }) { Text("Guardar") } }
    )
}