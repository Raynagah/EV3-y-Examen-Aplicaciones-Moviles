package com.example.vidasalud.presentation.perfil

import com.example.vidasalud.data.repository.Usuario

// Estado de la pantalla de perfil del usuario
data class PerfilUiState(
    val currentUser: Usuario? = null, // Datos del usuario cargado
    val isLoading: Boolean = false, // Indica si se está obteniendo la info
    val mensajeError: String? = null, // Mensaje de error para mostrar en pantalla
)
