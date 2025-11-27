package com.example.vidasalud.ui.screens.estadisticas

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable // ESTA FALTABA
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vidasalud.presentation.registro.RegistroViewModel
import com.example.vidasalud.ui.screens.estadisticas.components.FormularioRegistro
import com.example.vidasalud.ui.theme.BotonOscuro
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class DatoResumen(val icono: ImageVector, val titulo: String, val valor: String, val unidad: String)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaEstadisticas(
    viewModel: RegistroViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Estado para animar las barras del gráfico al entrar
    var animarGrafico by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animarGrafico = true
    }

    // Manejo de mensajes (Toast)
    LaunchedEffect(key1 = uiState.mensajeExito, key2 = uiState.mensajeError) {
        uiState.mensajeExito?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.limpiarMensajes()
        }
        uiState.mensajeError?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            viewModel.limpiarMensajes()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 10.dp)
            ) {
                Text(
                    text = "Mis Estadísticas",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Monitorea tu evolución semanal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- GRÁFICO DE BARRAS SIMULADO ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BotonOscuro),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Actividad Semanal (Pasos)",
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall
                    )

                    // El Gráfico
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val dias = listOf("L", "M", "M", "J", "V", "S", "D")
                        val valores = listOf(0.4f, 0.6f, 0.3f, 0.8f, 0.5f, 0.9f, 0.7f)

                        dias.forEachIndexed { index, dia ->
                            BarraGrafico(
                                label = dia,
                                alturaPorcentaje = valores[index],
                                animar = animarGrafico,
                                delay = index * 100
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- FECHA ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Registro Diario",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.clickable { // AHORA SÍ FUNCIONA
                        viewModel.onFechaSeleccionada(LocalDate.now())
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = uiState.fechaSeleccionada.format(DateTimeFormatter.ofPattern("dd MMM")),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- FORMULARIO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
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
                        onGuardarClick = viewModel::guardarRegistro,
                        isLoading = uiState.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- MINITARJETAS DE RESUMEN ---
            val datosResumen = listOf(
                DatoResumen(Icons.Default.MonitorWeight, "Peso", uiState.peso.ifBlank { "--" }, "kg"),
                DatoResumen(Icons.Default.Fastfood, "Calorías", uiState.calorias.ifBlank { "--" }, "kcal"),
                DatoResumen(Icons.Default.Bedtime, "Sueño", uiState.sueno.ifBlank { "--" }, "hrs"),
                DatoResumen(Icons.Default.DirectionsRun, "Pasos", uiState.pasos.ifBlank { "--" }, "pasos")
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MiniTarjeta(dato = datosResumen[0], modifier = Modifier.weight(1f))
                    MiniTarjeta(dato = datosResumen[1], modifier = Modifier.weight(1f))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MiniTarjeta(dato = datosResumen[2], modifier = Modifier.weight(1f))
                    MiniTarjeta(dato = datosResumen[3], modifier = Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun BarraGrafico(label: String, alturaPorcentaje: Float, animar: Boolean, delay: Int) {
    val altura by animateFloatAsState(
        targetValue = if (animar) alturaPorcentaje else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = delay),
        label = "alturaBarra"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        Box(
            modifier = Modifier
                .width(8.dp)
                .fillMaxHeight(altura)
                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = label, color = Color.Gray, fontSize = 10.sp)
    }
}

@Composable
fun MiniTarjeta(dato: DatoResumen, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = dato.icono,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = dato.titulo, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                Text(
                    text = "${dato.valor} ${dato.unidad}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}