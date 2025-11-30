package com.example.vidasalud.ui.screens.perfil

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.vidasalud.presentation.perfil.PerfilViewModel
import com.example.vidasalud.ui.navigation.RutasApp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun PantallaPerfil(
    navControllerPrincipal: NavController,
    viewModel: PerfilViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // --- RECARGAR DATOS AL ENTRAR (Para que se actualice el rango) ---
    LaunchedEffect(Unit) {
        viewModel.cargarPerfil()
    }

    // --- LAUNCHER FOTO ---
    val launcherImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.actualizarFotoPerfil(it.toString()) }
    }

    val DegradadoHeader = Brush.verticalGradient(
        colors = listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
    )

    var showEditEdad by remember { mutableStateOf(false) }
    var showEditPais by remember { mutableStateOf(false) }

    if (showEditEdad) DialogoEditarPerfil("Editar Edad", uiState.edad, { showEditEdad = false }) { viewModel.guardarEdad(it); showEditEdad = false }
    if (showEditPais) DialogoEditarPerfil("Editar País", uiState.pais, { showEditPais = false }) { viewModel.guardarPais(it); showEditPais = false }

    Scaffold(
        containerColor = Color(0xFFF8F9FA) // Este color es clave para el truco del borde
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- HEADER CURVO MEJORADO ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp) // Un poco más alto para dar aire
            ) {
                // Fondo Verde
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp)
                        .background(
                            brush = DegradadoHeader,
                            shape = RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)
                        )
                ) {
                    Text(
                        text = "Mi Perfil",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 48.dp) // MÁS MARGEN SUPERIOR
                    )
                }

                // --- FOTO DE PERFIL ---
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .align(Alignment.BottomCenter)
                        .offset(y = -20.dp)
                        .shadow(8.dp, CircleShape)
                        // TRUCO DEL BORDE: Un contenedor del color del fondo de la pantalla (#F8F9FA)
                        // crea el efecto de "corte" limpio sobre el verde.
                        .background(Color(0xFFF8F9FA), CircleShape)
                        .padding(6.dp) // Grosor del borde/corte
                        .clip(CircleShape)
                        .background(Color(0xFFE0F2F1))
                        .clickable { launcherImagen.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.fotoUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context)
                                    .data(uiState.fotoUrl)
                                    .crossfade(true)
                                    .build()
                            ),
                            contentDescription = "Foto",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(70.dp),
                            tint = Color(0xFF00695C)
                        )
                    }
                }

                // Icono Cámara (Indicador visual)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(x = 40.dp, y = -20.dp)
                        .size(32.dp)
                        .background(Color.White, CircleShape)
                        .shadow(2.dp, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Editar",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(16.dp)
                    )
                }

                // Rango
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 5.dp) // Ajustado para que no tape la foto
                        .background(Color(0xFFFFC107), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = uiState.rango,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Info Principal
            Text(
                text = uiState.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = uiState.email,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(30.dp))

            // --- INFO EXTRA ---
            Text(
                text = "Información Personal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(vertical = 8.dp)
            ) {
                // Items editables con flecha
                ItemPerfilInfo(Icons.Default.Cake, "Edad", "${uiState.edad} años", true) { showEditEdad = true }
                Divider(color = Color(0xFFF5F5F5), thickness = 1.dp)
                ItemPerfilInfo(Icons.Default.Public, "País", uiState.pais, true) { showEditPais = true }
                Divider(color = Color(0xFFF5F5F5), thickness = 1.dp)

                // ID TÉCNICO (Sin flecha, fuente mono, todo visible)
                ItemPerfilID(uiState.uid)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // BOTÓN SALIR
            Button(
                onClick = {
                    FirebaseAuth.getInstance().signOut()
                    val sharedPreferences = context.getSharedPreferences("VidaSaludPrefs", Context.MODE_PRIVATE)
                    sharedPreferences.edit().clear().apply()
                    navControllerPrincipal.navigate(RutasApp.PantallaBienvenida.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFEBEE),
                    contentColor = Color(0xFFD32F2F)
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

// Item Normal (Editable)
@Composable
fun ItemPerfilInfo(icono: ImageVector, titulo: String, valor: String, editable: Boolean = false, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = editable) { onClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icono, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(valor, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = Color.Black)
        }
        if (editable) {
            Icon(Icons.Default.ChevronRight, null, tint = Color.LightGray)
        }
    }
}

// Item Especial ID (Sin edición, fuente técnica)
@Composable
fun ItemPerfilID(uid: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color(0xFFF5F5F5), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Fingerprint, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("ID de Usuario (Técnico)", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            // Usamos FontFamily.Monospace para que se vea como código y quepa mejor
            Text(
                text = uid,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp, // Letra un poco más chica
                    color = Color.DarkGray
                ),
                lineHeight = 14.sp
            )
        }
        // Sin flecha aquí
    }
}

@Composable
fun DialogoEditarPerfil(titulo: String, valorActual: String, onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var texto by remember { mutableStateOf(valorActual) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(titulo) },
        text = {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                shape = RoundedCornerShape(12.dp)
            )
        },
        confirmButton = { Button(onClick = { onConfirm(texto) }) { Text("Guardar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}