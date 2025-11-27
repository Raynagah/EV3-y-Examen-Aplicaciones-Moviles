package com.example.vidasalud.presentation.comunidad

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud.data.repository.AuthRepository
import com.example.vidasalud.data.repository.Publicacion
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// Estado de la pantalla de comunidad
data class ComunidadUiState(
    val publicaciones: List<Publicacion> = emptyList(),
    val isLoading: Boolean = false,
    val mensajeError: String? = null,
    val nuevoPostContenido: String = "", // Texto que escribe el usuario
    val isPosting: Boolean = false // Si se está enviando un post
)

class ComunidadViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val authRepo = AuthRepository() // Para obtener el nombre del usuario

    private val _uiState = MutableStateFlow(ComunidadUiState())
    val uiState: StateFlow<ComunidadUiState> = _uiState.asStateFlow()

    init {
        cargarPublicaciones()
    }

    // Escucha en tiempo real las publicaciones
    private fun cargarPublicaciones() {
        _uiState.update { it.copy(isLoading = true) }

        // Ordenamos por fecha descendente
        db.collection("publicaciones")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    _uiState.update { it.copy(isLoading = false, mensajeError = "Error al cargar: ${e.message}") }
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val lista = snapshot.toObjects(Publicacion::class.java)
                    _uiState.update { it.copy(publicaciones = lista, isLoading = false) }
                }
            }
    }

    fun onNuevoPostChange(texto: String) {
        _uiState.update { it.copy(nuevoPostContenido = texto) }
    }

    fun publicarPost() {
        val contenido = _uiState.value.nuevoPostContenido
        val user = auth.currentUser

        if (contenido.isBlank() || user == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isPosting = true) }
            try {
                // Obtenemos el nombre actual del usuario
                val nombre = authRepo.obtenerNombreUsuario() ?: "Usuario"

                val nuevaPublicacion = Publicacion(
                    userId = user.uid,
                    nombreUsuario = nombre,
                    contenido = contenido
                )

                // Guardamos en Firestore
                db.collection("publicaciones").add(nuevaPublicacion).await()

                // Limpiamos el campo de texto
                _uiState.update { it.copy(nuevoPostContenido = "", isPosting = false) }

            } catch (e: Exception) {
                Log.e("ComunidadViewModel", "Error al publicar", e)
                _uiState.update { it.copy(mensajeError = "No se pudo publicar", isPosting = false) }
            }
        }
    }
}