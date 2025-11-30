package com.example.vidasalud.presentation.principal

import android.app.Application
import android.content.Context
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ResumenUiState(
    val nombreUsuario: String = "Usuario",
    val recomendacion: String = "Cargando consejos...",
    val ultimosPasos: Int = 0,
    val ultimoSueno: Double = 0.0,
    val aguaConsumida: Double = 0.0, // Nuevo: Agua (Litros)
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

    // Guardar Meta Localmente
    fun actualizarMetaPasos(nuevaMeta: Int) {
        viewModelScope.launch {
            sharedPreferences.edit().putInt("meta_pasos", nuevaMeta).apply()
            _uiState.update { it.copy(metaPasos = nuevaMeta) }
            recalcularConsejo(_uiState.value.ultimosPasos, _uiState.value.ultimoSueno, nuevaMeta)
        }
    }

    // Actualizar Agua (Solo en UI por ahora, para persistir se requeriría guardar en Firestore)
    fun actualizarAgua(litros: Double) {
        _uiState.update { it.copy(aguaConsumida = litros) }
    }

    // Actualizar Sueño (Solo UI por ahora)
    fun actualizarSueno(horas: Double) {
        _uiState.update { it.copy(ultimoSueno = horas) }
        recalcularConsejo(_uiState.value.ultimosPasos, horas, _uiState.value.metaPasos)
    }

    private fun recalcularConsejo(pasos: Int, sueno: Double, meta: Int) {
        val consejo = when {
            sueno > 0 && sueno < 6 -> "⚠️ Has dormido poco. Intenta acostarte más temprano hoy para recuperar energía."
            pasos < 2000 -> "🚶 Llevas pocos pasos. ¡Camina de camino a tu casa para avanzar en tu meta!"
            pasos < meta -> "👍 Vas bien, pero te falta un poco. ¿Qué tal una vuelta a la manzana?"
            pasos >= meta -> "🎉 ¡Excelente! Has cumplido tu meta de hoy. ¡Disfruta tu descanso!"
            else -> "Mantén tus hábitos saludables."
        }
        _uiState.update { it.copy(recomendacion = consejo) }
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(500)

            val metaGuardada = sharedPreferences.getInt("meta_pasos", 5000)
            var nombre = sharedPreferences.getString("nombre_usuario_local", "Usuario") ?: "Usuario"

            var pasos = 0
            var sueno = 0.0

            try {
                val nombreRed = authRepository.obtenerNombreUsuario()
                if (nombreRed != null) {
                    nombre = nombreRed
                    sharedPreferences.edit().putString("nombre_usuario_local", nombre).apply()
                }

                val uid = auth.currentUser?.uid
                if (uid != null) {
                    // --- CORRECCIÓN BUG FECHA ---
                    // Obtenemos la fecha de HOY en formato String (ej: "2023-11-29")
                    val fechaHoy = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

                    val snapshot = db.collection("registros_diarios")
                        .whereEqualTo("userId", uid)
                        .whereEqualTo("fecha", fechaHoy) // ¡SOLO TRAEMOS DATOS DE HOY!
                        .get()
                        .await()

                    if (!snapshot.isEmpty) {
                        val registro = snapshot.documents[0].toObject(RegistroDiario::class.java)
                        if (registro != null) {
                            pasos = registro.pasos ?: 0
                            sueno = registro.horas_sueno ?: 0.0
                        }
                    }
                    // Si snapshot está vacío, pasos y sueño se quedan en 0 (Correcto para un día nuevo)
                }
            } catch (e: Exception) {
                // Error silencioso o modo offline
            }

            _uiState.update {
                it.copy(
                    nombreUsuario = nombre,
                    ultimosPasos = pasos,
                    ultimoSueno = sueno,
                    metaPasos = metaGuardada,
                    isLoading = false
                )
            }
            recalcularConsejo(pasos, sueno, metaGuardada)
        }
    }
}