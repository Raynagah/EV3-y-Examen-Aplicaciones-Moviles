package com.example.vidasalud.presentation.registro

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud.data.repository.RegistroDiario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class RegistroViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val registrosCollection = db.collection("registros_diarios")
    private val userId = auth.currentUser?.uid

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState

    init {
        cargarRegistroDiario(LocalDate.now())
    }

    // --- FUNCIÓN QUE FALTABA PARA CORREGIR EL ERROR ---
    fun limpiarMensajes() {
        _uiState.update { it.copy(mensajeExito = null, mensajeError = null) }
    }

    fun onFechaSeleccionada(fecha: LocalDate) {
        cargarRegistroDiario(fecha)
    }

    private fun cargarRegistroDiario(fecha: LocalDate) {
        if (userId == null) return

        val fechaStr = fecha.format(DateTimeFormatter.ISO_DATE)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, fechaSeleccionada = fecha) }

            try {
                val snapshot = registrosCollection
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("fecha", fechaStr)
                    .get()
                    .await()

                if (!snapshot.isEmpty) {
                    val documento = snapshot.documents[0]
                    val registro = documento.toObject(RegistroDiario::class.java)

                    if (registro != null) {
                        _uiState.update {
                            it.copy(
                                registroId = documento.id,
                                peso = registro.peso_kg?.toString() ?: "",
                                calorias = registro.calorias_consumidas?.toString() ?: "",
                                sueno = registro.horas_sueno?.toString() ?: "",
                                pasos = registro.pasos?.toString() ?: "",
                                isLoading = false
                            )
                        }
                    }
                } else {
                    // No hay registro, limpiar campos
                    _uiState.update {
                        it.copy(
                            registroId = null,
                            peso = "",
                            calorias = "",
                            sueno = "",
                            pasos = "",
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("RegistroViewModel", "Error al cargar", e)
                _uiState.update { it.copy(isLoading = false, mensajeError = "Error al cargar datos") }
            }
        }
    }

    fun onPesoChange(valor: String) { _uiState.update { it.copy(peso = valor, errorPeso = null) } }
    fun onCaloriasChange(valor: String) { _uiState.update { it.copy(calorias = valor, errorCalorias = null) } }
    fun onSuenoChange(valor: String) { _uiState.update { it.copy(sueno = valor, errorSueno = null) } }
    fun onPasosChange(valor: String) { _uiState.update { it.copy(pasos = valor, errorPasos = null) } }

    fun guardarRegistro() {
        if (!validarFormulario()) return
        if (userId == null) {
            _uiState.update { it.copy(mensajeError = "Usuario no autenticado") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val state = _uiState.value
            val registro = RegistroDiario(
                userId = userId,
                fecha = state.fechaSeleccionada.format(DateTimeFormatter.ISO_DATE),
                peso_kg = state.peso.toDoubleOrNull(),
                calorias_consumidas = state.calorias.toIntOrNull(),
                horas_sueno = state.sueno.toDoubleOrNull(),
                pasos = state.pasos.toIntOrNull()
            )

            try {
                if (state.registroId != null) {
                    // Actualizar existente
                    registrosCollection.document(state.registroId).set(registro).await()
                    _uiState.update { it.copy(mensajeExito = "Datos actualizados correctamente") }
                } else {
                    // Crear nuevo
                    registrosCollection.add(registro).await()
                    // Recargar para obtener el ID nuevo
                    cargarRegistroDiario(state.fechaSeleccionada)
                    _uiState.update { it.copy(mensajeExito = "Registro guardado exitosamente") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(mensajeError = "Error al guardar: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun validarFormulario(): Boolean {
        var hayErrores = false
        val state = _uiState.value

        if (state.peso.isNotBlank() && state.peso.toDoubleOrNull() == null) {
            _uiState.update { it.copy(errorPeso = "Número inválido") }
            hayErrores = true
        }
        if (state.calorias.isNotBlank() && state.calorias.toIntOrNull() == null) {
            _uiState.update { it.copy(errorCalorias = "Debe ser entero") }
            hayErrores = true
        }
        if (state.sueno.isNotBlank() && state.sueno.toDoubleOrNull() == null) {
            _uiState.update { it.copy(errorSueno = "Número inválido") }
            hayErrores = true
        }
        if (state.pasos.isNotBlank() && state.pasos.toIntOrNull() == null) {
            _uiState.update { it.copy(errorPasos = "Debe ser entero") }
            hayErrores = true
        }

        return !hayErrores
    }
}