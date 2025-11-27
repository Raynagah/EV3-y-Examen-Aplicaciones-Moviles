package com.example.vidasalud.presentation.principal

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud.data.repository.AuthRepository
import com.example.vidasalud.data.repository.RegistroDiario
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
    val metaPasos: Int = 5000, // Dato local
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

    // Función para guardar la meta localmente
    fun actualizarMetaPasos(nuevaMeta: Int) {
        viewModelScope.launch {
            // Guardar en disco (Local)
            sharedPreferences.edit().putInt("meta_pasos", nuevaMeta).apply()
            // Actualizar UI
            _uiState.update { it.copy(metaPasos = nuevaMeta) }
            // Recalcular recomendación con la nueva meta
            cargarDatos()
        }
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Simular pequeña carga para que se vea la animación
            delay(500)

            // 1. Cargar Meta Local (Sin internet)
            val metaGuardada = sharedPreferences.getInt("meta_pasos", 5000)

            // 2. Obtener Nombre
            val nombre = authRepository.obtenerNombreUsuario() ?: "Usuario"

            // 3. Obtener datos de Firebase
            var recomendacionTexto = "¡Bienvenido! Registra tus datos en la pestaña 'Datos'."
            var pasos = 0
            var sueno = 0.0

            try {
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
                        // Buscamos el más reciente manualmente
                        val ultimoRegistro = registros.maxByOrNull { it.fecha }

                        if (ultimoRegistro != null) {
                            pasos = ultimoRegistro.pasos ?: 0
                            sueno = ultimoRegistro.horas_sueno ?: 0.0

                            // Pasamos la meta guardada para generar el consejo
                            recomendacionTexto = generarConsejo(pasos, sueno, metaGuardada)
                        }
                    }
                }
            } catch (e: Exception) {
                recomendacionTexto = "Modo Offline: Mostrando datos cacheados."
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

    private fun generarConsejo(pasos: Int, sueno: Double, meta: Int): String {
        return when {
            pasos >= meta -> "¡Increíble! Has superado tu meta de $meta pasos. 🎉"
            pasos > (meta / 2) -> "¡Bien! Llevas el ${(pasos * 100) / meta}% de tu meta diaria."
            sueno > 0 && sueno < 6 -> "Has dormido poco ($sueno hrs). Prioriza tu descanso hoy."
            pasos > 0 && pasos < 4000 -> "Llevas $pasos pasos. ¡Intenta salir a caminar un poco!"
            sueno >= 7 && sueno <= 9 -> "Tu descanso de $sueno horas es óptimo. ¡Sigue así!"
            pasos == 0 && sueno == 0.0 -> "No hay datos recientes. ¡Ve a la pestaña 'Datos' y registra tu día!"
            else -> "Mantén tus hábitos saludables y registra tu progreso."
        }
    }
}