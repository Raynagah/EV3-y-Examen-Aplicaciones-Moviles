package com.example.vidasalud.presentation.principal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado para la pantalla de resumen principal
data class ResumenUiState(
    val nombreUsuario: String = "..." // Nombre que se mostrará en la pantalla
)

class ResumenViewModel : ViewModel() {

    private val authRepository = AuthRepository() // Fuente para obtener datos del usuario

    private val _uiState = MutableStateFlow(ResumenUiState()) // Estado interno mutable
    val uiState = _uiState.asStateFlow() // Estado expuesto a la UI

    init {
        cargarNombreUsuario() // Cargar el nombre al crear el ViewModel
    }

    // Obtiene el nombre del usuario desde Firebase
    private fun cargarNombreUsuario() {
        viewModelScope.launch {
            val nombre = authRepository.obtenerNombreUsuario()
            if (nombre != null) {
                _uiState.update { it.copy(nombreUsuario = nombre) } // Si existe, mostrarlo
            } else {
                _uiState.update { it.copy(nombreUsuario = "Usuario") } // Nombre por defecto
            }
        }
    }
}
