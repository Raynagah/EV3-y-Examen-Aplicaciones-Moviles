package com.example.vidasalud.ui.screens.comunidad

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vidasalud.presentation.comunidad.ComunidadViewModel
import com.example.vidasalud.ui.theme.BotonOscuro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaComunidad(
    viewModel: ComunidadViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Animación de entrada
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 10.dp)
            ) {
                // TITULO UNIFICADO CON LAS OTRAS PANTALLAS
                Text(
                    text = "Comunidad",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Comparte y motívate con otros",
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
        ) {

            // --- NUEVO: TARJETA DE DESAFÍO (Para llenar espacio) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)), // Amarillo suave
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFB300)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Desafío del Día", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037))
                        Text("¡Camina 2.000 pasos extra hoy!", style = MaterialTheme.typography.bodySmall, color = Color(0xFF795548))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- INPUT DE TEXTO ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.nuevoPostContenido,
                        onValueChange = { viewModel.onNuevoPostChange(it) },
                        placeholder = { Text("Escribe algo aquí...", fontSize = 14.sp) },
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            containerColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent
                        )
                    )

                    IconButton(
                        onClick = { viewModel.publicarPost() },
                        enabled = !uiState.isPosting && uiState.nuevoPostContenido.isNotBlank(),
                        modifier = Modifier
                            .background(BotonOscuro, CircleShape)
                            .size(44.dp)
                    ) {
                        if (uiState.isPosting) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        } else {
                            Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Recientes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            // --- LISTA ANIMADA ---
            AnimatedVisibility(
                visible = visible,
                enter = slideInVertically(initialOffsetY = { 100 }, animationSpec = tween(500))
            ) {
                if (uiState.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(uiState.publicaciones) { publicacion ->
                            ItemPublicacionEstilizado(
                                nombre = publicacion.nombreUsuario,
                                contenido = publicacion.contenido,
                                fecha = "Hace un momento"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemPublicacionEstilizado(nombre: String, contenido: String, fecha: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F2F1)), // Verde muy claro
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(text = nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(text = fecha, fontSize = 12.sp, color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = contenido,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp
            )
        }
    }
}