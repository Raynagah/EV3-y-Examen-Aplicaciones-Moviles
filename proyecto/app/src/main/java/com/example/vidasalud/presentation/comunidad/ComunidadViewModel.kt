package com.example.vidasalud.presentation.comunidad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud.data.repository.AuthRepository
import com.example.vidasalud.data.repository.Publicacion
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Calendar

data class ComunidadUiState(
    val publicaciones: List<Publicacion> = emptyList(),
    val nuevoPostContenido: String = "",
    val isLoading: Boolean = false,
    val isPosting: Boolean = false,
    val desafioDelDia: String = "Cargando desafío..."
)

class ComunidadViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val authRepo = AuthRepository()

    private val _uiState = MutableStateFlow(ComunidadUiState())
    val uiState = _uiState.asStateFlow()

    private val listaDesafios = listOf(
        "🚶 Camina 2.000 pasos extra hoy.",
        "💧 Bebe 2 vasos de agua al despertar.",
        "🥗 Agrega una porción verde a tu almuerzo.",
        "🚫 Cero azúcar en tus bebidas por 24h.",
        "🧘 Haz 5 minutos de estiramiento antes de dormir.",
        "🍎 Come una fruta en lugar de un postre procesado.",
        "🪜 Sube por las escaleras en lugar del ascensor.",
        "📵 30 minutos sin pantallas antes de dormir.",
        "💪 Haz 10 sentadillas cada vez que vayas al baño.",
        "🌞 Sal a tomar sol por 10 minutos."
    )

    init {
        seleccionarDesafioDiario()
        escucharPublicaciones()
    }

    private fun seleccionarDesafioDiario() {
        val diaDelAno = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val indice = diaDelAno % listaDesafios.size
        _uiState.update { it.copy(desafioDelDia = listaDesafios[indice]) }
    }

    private fun escucharPublicaciones() {
        _uiState.update { it.copy(isLoading = true) }

        db.collection("publicaciones")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null || snapshot == null) {
                    _uiState.update { it.copy(isLoading = false) }
                    return@addSnapshotListener
                }

                val lista = snapshot.toObjects(Publicacion::class.java)
                _uiState.update { it.copy(publicaciones = lista, isLoading = false) }
            }
    }

    fun onNuevoPostChange(texto: String) {
        _uiState.update { it.copy(nuevoPostContenido = texto) }
    }

    fun publicarPost() {
        val contenido = _uiState.value.nuevoPostContenido.trim()
        if (contenido.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isPosting = true) }

            val nombre = authRepo.obtenerNombreUsuario() ?: "Anónimo"
            val uid = authRepo.obtenerUid() ?: ""

            // CREACIÓN CORRECTA DE PUBLICACIÓN
            // No enviamos 'fecha', dejamos que sea null para que @ServerTimestamp actúe en el servidor
            val nuevaPublicacion = Publicacion(
                userId = uid,
                nombreUsuario = nombre,
                contenido = contenido
                // id y fecha toman sus valores por defecto (null)
            )

            try {
                db.collection("publicaciones").add(nuevaPublicacion).await()
                _uiState.update { it.copy(nuevoPostContenido = "") }
            } catch (e: Exception) {
                // Manejo de error
            } finally {
                _uiState.update { it.copy(isPosting = false) }
            }
        }
    }
}