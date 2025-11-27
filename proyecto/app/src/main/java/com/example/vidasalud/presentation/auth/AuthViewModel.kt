package com.example.vidasalud.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud.data.repository.AuthRepository
import com.example.vidasalud.data.repository.ResultadoAuth
import com.example.vidasalud.ui.navigation.RutasApp
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de la pantalla de autenticación
data class AuthUiState(
    val email: String = "",
    val contrasena: String = "",
    val errorEmail: String? = null,
    val errorContrasena: String? = null,
    val nombre: String = "",
    val confirmarContrasena: String = "",
    val errorNombre: String? = null,
    val errorConfirmarContrasena: String? = null,
    val isLoading: Boolean = false,
    val errorGeneral: String? = null
)

class AuthViewModel : ViewModel() {

    private val authRepository = AuthRepository() // Repositorio para Firebase Auth

    private val _uiState = MutableStateFlow(AuthUiState()) // Estado mutable
    val uiState = _uiState.asStateFlow() // Estado público inmutable

    private val _navigationEvent = MutableSharedFlow<String>() // Eventos de navegación
    val navigationEvent = _navigationEvent.asSharedFlow()

    // Actualiza email
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorEmail = null, errorGeneral = null) }
    }

    // Actualiza contraseña
    fun onContrasenaChange(contrasena: String) {
        _uiState.update {
            it.copy(
                contrasena = contrasena,
                errorContrasena = null,
                errorConfirmarContrasena = null,
                errorGeneral = null
            )
        }
    }

    // Actualiza nombre
    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, errorNombre = null, errorGeneral = null) }
    }

    // Actualiza confirmación de contraseña
    fun onConfirmarContrasenaChange(contrasena: String) {
        _uiState.update {
            it.copy(
                confirmarContrasena = contrasena,
                errorConfirmarContrasena = null,
                errorGeneral = null
            )
        }
    }

    // Acción botón "Iniciar sesión"
    fun onLoginClicked() {
        if (!validarCamposLogin()) return // Valida campos

        _uiState.update { it.copy(isLoading = true, errorGeneral = null) }

        viewModelScope.launch {
            val resultado = authRepository.iniciarSesion(
                email = _uiState.value.email,
                contrasena = _uiState.value.contrasena
            )
            when (resultado) {
                is ResultadoAuth.Exito -> {
                    _navigationEvent.emit(RutasApp.PantallaPrincipal.ruta) // Navegar a pantalla principal
                }
                is ResultadoAuth.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorGeneral = resultado.mensaje) }
                }
            }
        }
    }

    // Acción botón "Registrar"
    fun onRegistroClicked() {
        if (!validarCamposRegistro()) return // Valida campos

        _uiState.update { it.copy(isLoading = true, errorGeneral = null) }

        viewModelScope.launch {
            val resultado = authRepository.crearUsuario(
                nombre = _uiState.value.nombre,
                email = _uiState.value.email,
                contrasena = _uiState.value.contrasena
            )
            when (resultado) {
                is ResultadoAuth.Exito -> {
                    _navigationEvent.emit(RutasApp.PantallaPrincipal.ruta) // Navegar a inicio
                }
                is ResultadoAuth.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorGeneral = resultado.mensaje) }
                }
            }
        }
    }

    // Valida formato email con regex
    private fun esEmailValido(email: String): Boolean {
        return Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$").matches(email)
    }

    // Validación de inicio de sesión
    private fun validarCamposLogin(): Boolean {
        val email = _uiState.value.email
        val contrasena = _uiState.value.contrasena
        var hayErrores = false

        if (email.isBlank()) {
            _uiState.update { it.copy(errorEmail = "Campo obligatorio") }
            hayErrores = true
        } else if (!esEmailValido(email)) {
            _uiState.update { it.copy(errorEmail = "Formato de email no válido") }
            hayErrores = true
        }

        if (contrasena.isBlank()) {
            _uiState.update { it.copy(errorContrasena = "Campo obligatorio") }
            hayErrores = true
        } else if (contrasena.length < 6) {
            _uiState.update { it.copy(errorContrasena = "Mínimo 6 caracteres") }
            hayErrores = true
        }

        return !hayErrores
    }

    // Validación de formulario de registro
    private fun validarCamposRegistro(): Boolean {
        val state = _uiState.value
        var hayErrores = false

        if (state.nombre.isBlank()) {
            _uiState.update { it.copy(errorNombre = "Campo obligatorio") }
            hayErrores = true
        }

        if (state.email.isBlank()) {
            _uiState.update { it.copy(errorEmail = "Campo obligatorio") }
            hayErrores = true
        } else if (!esEmailValido(state.email)) {
            _uiState.update { it.copy(errorEmail = "Formato de email no válido") }
            hayErrores = true
        }

        if (state.contrasena.isBlank()) {
            _uiState.update { it.copy(errorContrasena = "Campo obligatorio") }
            hayErrores = true
        } else if (state.contrasena.length < 6) {
            _uiState.update { it.copy(errorContrasena = "Mínimo 6 caracteres") }
            hayErrores = true
        }

        if (state.confirmarContrasena.isBlank()) {
            _uiState.update { it.copy(errorConfirmarContrasena = "Campo obligatorio") }
            hayErrores = true
        } else if (state.contrasena.isNotBlank() && state.contrasena != state.confirmarContrasena) {
            _uiState.update { it.copy(errorConfirmarContrasena = "Las contraseñas no coinciden") }
            hayErrores = true
        }

        return !hayErrores
    }
}
