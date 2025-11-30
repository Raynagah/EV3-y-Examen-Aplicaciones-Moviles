package com.example.vidasalud.presentation.perfil

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PerfilUiState(
    val nombre: String = "Cargando...",
    val email: String = "",
    val uid: String = "",
    val edad: String = "",
    val pais: String = "",
    val rango: String = "Novato",
    val fotoUrl: String? = null
)

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepo = AuthRepository()
    private val auth = FirebaseAuth.getInstance()
    private val prefs = application.getSharedPreferences("VidaSaludPrefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarPerfil()
    }

    // Hacemos esta función PÚBLICA para llamarla desde la UI y refrescar el rango
    fun cargarPerfil() {
        viewModelScope.launch {
            val nombre = authRepo.obtenerNombreUsuario() ?: "Usuario"
            val email = auth.currentUser?.email ?: ""
            val uid = auth.currentUser?.uid ?: "ID-UNKNOWN"

            val edadGuardada = prefs.getString("user_edad", "--") ?: "--"
            val paisGuardado = prefs.getString("user_pais", "Chile") ?: "Chile"

            // RANGO DINÁMICO
            val metaActual = prefs.getInt("meta_pasos", 5000)
            val rangoCalculado = when {
                metaActual >= 10000 -> "Atleta 🔥"
                metaActual >= 5000 -> "Explorador 🌿"
                else -> "Novato 🌱"
            }

            _uiState.update {
                it.copy(
                    nombre = nombre,
                    email = email,
                    uid = uid,
                    edad = edadGuardada,
                    pais = paisGuardado,
                    rango = rangoCalculado
                )
            }
        }
    }

    fun guardarEdad(nuevaEdad: String) {
        prefs.edit().putString("user_edad", nuevaEdad).apply()
        _uiState.update { it.copy(edad = nuevaEdad) }
    }

    fun guardarPais(nuevoPais: String) {
        prefs.edit().putString("user_pais", nuevoPais).apply()
        _uiState.update { it.copy(pais = nuevoPais) }
    }

    fun actualizarFotoPerfil(uri: String) {
        _uiState.update { it.copy(fotoUrl = uri) }
    }
}