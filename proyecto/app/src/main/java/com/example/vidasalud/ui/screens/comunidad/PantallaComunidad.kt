package com.example.vidasalud.ui.screens.comunidad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vidasalud.presentation.comunidad.ComunidadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaComunidad(
    viewModel: ComunidadViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Degradado Trofeo (Dorado/Naranja)
    val DegradadoTrofeo = Brush.linearGradient(
        colors = listOf(Color(0xFFFFB300), Color(0xFFFF6F00))
    )

    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Text("Comunidad", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.Black)
            Text("Motívate con otros", color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            // --- TARJETA DE DESAFÍO (TROFEO) ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DegradadoTrofeo)
                        .padding(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text("DESAFÍO DEL DÍA", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha = 0.8f))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uiState.desafioDelDia,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- INPUT MENSAJE ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = uiState.nuevoPostContenido,
                    onValueChange = { viewModel.onNuevoPostChange(it) },
                    placeholder = { Text("Comparte tu logro...", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.White,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color(0xFFFF6F00)
                    ),
                    maxLines = 1
                )

                Spacer(modifier = Modifier.width(12.dp))

                IconButton(
                    onClick = { viewModel.publicarPost() },
                    enabled = !uiState.isPosting && uiState.nuevoPostContenido.isNotBlank(),
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color(0xFF1A1A1A), CircleShape)
                        .shadow(4.dp, CircleShape)
                ) {
                    if (uiState.isPosting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                    } else {
                        Icon(Icons.Default.Send, contentDescription = "Enviar", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Recientes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            // --- LISTA ---
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFFF6F00))
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(uiState.publicaciones) { publicacion ->
                        ItemMensaje(
                            nombre = publicacion.nombreUsuario,
                            contenido = publicacion.contenido
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ItemMensaje(nombre: String, contenido: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        // CORREGIDO: Usamos verticalAlignment = Alignment.Top para alinear arriba si el texto es largo
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFE0F7FA), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val inicial = if (nombre.isNotEmpty()) nombre.take(1).uppercase() else "?"
                Text(
                    text = inicial,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF006064)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = nombre, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = contenido,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )
            }
        }
    }
}