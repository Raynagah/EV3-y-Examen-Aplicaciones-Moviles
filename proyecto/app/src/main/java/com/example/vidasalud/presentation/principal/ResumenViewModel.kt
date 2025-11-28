package com.example.vidasalud.presentation.principal

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud.data.repository.AuthRepository
import com.example.vidasalud.data.repository.RegistroDiario
import com.example.vidasalud.domain.LogicaSalud
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ResumenUiState(
    val nombreUsuario: String = "Usuario",
    val recomendacion: String = "Cargando consejos...",
    val ultimosPasos: Int = 0,
    val ultimoSueno: Double = 0.0,
    val metaPasos: Int = 5000,
    val isLoading: Boolean = false
)

class ResumenViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository()
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val sharedPreferences = application.getSharedPreferences("VidaSaludPrefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(ResumenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    fun recargarDatos() {
        cargarDatos()
    }

    fun actualizarMetaPasos(nuevaMeta: Int) {
        viewModelScope.launch {
            sharedPreferences.edit().putInt("meta_pasos", nuevaMeta).apply()
            _uiState.update { it.copy(metaPasos = nuevaMeta) }
            cargarDatos()
        }
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(500)

            val metaGuardada = sharedPreferences.getInt("meta_pasos", 5000)

            // PRIMERO INTENTAMOS CARGAR NOMBRE LOCAL (OFFLINE)
            var nombre = sharedPreferences.getString("nombre_usuario_local", "Usuario") ?: "Usuario"
            var recomendacionTexto = "¡Bienvenido! Registra tus datos en la pestaña 'Datos'."
            var pasos = 0
            var sueno = 0.0

            try {
                // INTENTAMOS CONECTAR (ONLINE)
                val nombreRed = authRepository.obtenerNombreUsuario()
                if (nombreRed != null) {
                    nombre = nombreRed
                    // Si hay red, actualizamos el caché local
                    sharedPreferences.edit().putString("nombre_usuario_local", nombre).apply()
                }

                val uid = auth.currentUser?.uid
                if (uid != null) {
                    val snapshot = db.collection("registros_diarios")
                        .whereEqualTo("userId", uid)
                        .get()
                        .await()

                    if (!snapshot.isEmpty) {
                        val registros = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(RegistroDiario::class.java)
                        }
                        val ultimoRegistro = registros.maxByOrNull { it.fecha }

                        if (ultimoRegistro != null) {
                            pasos = ultimoRegistro.pasos ?: 0
                            sueno = ultimoRegistro.horas_sueno ?: 0.0
                            recomendacionTexto = LogicaSalud.generarConsejo(pasos, sueno, metaGuardada)
                        }
                    }
                }
            } catch (e: Exception) {
                // FALLO DE RED (MODO AVIÓN) -> USAMOS DATOS LOCALES
                // Como ya cargamos 'nombre' y 'metaGuardada' de SharedPreferences arriba,
                // la UI se mostrará con esos datos cacheados.
                recomendacionTexto = "Modo Offline: Mostrando datos guardados. ${LogicaSalud.generarConsejo(pasos, sueno, metaGuardada)}"
            }

            _uiState.update {
                it.copy(
                    nombreUsuario = nombre,
                    recomendacion = recomendacionTexto,
                    ultimosPasos = pasos,
                    ultimoSueno = sueno,
                    metaPasos = metaGuardada,
                    isLoading = false
                )
            }
        }
    }
}