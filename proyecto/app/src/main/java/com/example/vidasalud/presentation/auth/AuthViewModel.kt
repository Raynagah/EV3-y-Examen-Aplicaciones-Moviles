package com.example.vidasalud.presentation.auth

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
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

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()
    private val sharedPreferences = application.getSharedPreferences("VidaSaludPrefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<String>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    fun onEmailChange(email: String) { _uiState.update { it.copy(email = email, errorEmail = null) } }
    fun onContrasenaChange(pass: String) { _uiState.update { it.copy(contrasena = pass, errorContrasena = null) } }
    fun onNombreChange(nombre: String) { _uiState.update { it.copy(nombre = nombre, errorNombre = null) } }
    fun onConfirmarContrasenaChange(conf: String) { _uiState.update { it.copy(confirmarContrasena = conf, errorConfirmarContrasena = null) } }

    fun iniciarSesion() {
        if (!validarCamposLogin()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorGeneral = null) }

            val resultado = authRepository.iniciarSesion(_uiState.value.email, _uiState.value.contrasena)

            when (resultado) {
                is ResultadoAuth.Exito -> {
                    // GUARDAR SESIÓN LOCAL
                    sharedPreferences.edit().putBoolean("is_logged_in", true).apply()

                    val nombre = authRepository.obtenerNombreUsuario()
                    if (nombre != null) {
                        sharedPreferences.edit().putString("nombre_usuario_local", nombre).apply()
                    }

                    _navigationEvent.emit(RutasApp.PantallaPrincipal.ruta)
                }
                is ResultadoAuth.Error -> {
                    _uiState.update { it.copy(errorGeneral = resultado.mensaje) }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun registrarUsuario() {
        if (!validarCamposRegistro()) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorGeneral = null) }
            val resultado = authRepository.crearUsuario(_uiState.value.nombre, _uiState.value.email, _uiState.value.contrasena)
            if (resultado is ResultadoAuth.Exito) {
                // GUARDAR SESIÓN LOCAL TAMBIÉN AL REGISTRAR
                sharedPreferences.edit().putBoolean("is_logged_in", true).apply()
                sharedPreferences.edit().putString("nombre_usuario_local", _uiState.value.nombre).apply()
                _navigationEvent.emit(RutasApp.PantallaPrincipal.ruta)
            } else if (resultado is ResultadoAuth.Error) {
                _uiState.update { it.copy(errorGeneral = resultado.mensaje) }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun validarCamposLogin(): Boolean {
        val state = _uiState.value
        var hayErrores = false
        if (state.email.isBlank()) { _uiState.update { it.copy(errorEmail = "Requerido") }; hayErrores = true }
        if (state.contrasena.isBlank()) { _uiState.update { it.copy(errorContrasena = "Requerido") }; hayErrores = true }
        return !hayErrores
    }

    private fun validarCamposRegistro(): Boolean {
        val state = _uiState.value
        var hayErrores = false

        if (state.nombre.isBlank()) { _uiState.update { it.copy(errorNombre = "Campo obligatorio") }; hayErrores = true }

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

    private fun esEmailValido(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}