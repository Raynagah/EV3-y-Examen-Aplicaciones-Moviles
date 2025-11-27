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

    private val auth = FirebaseAuth.getInstance() // Firebase Auth para obtener usuario
    private val db = FirebaseFirestore.getInstance() // Firestore
    private val registrosCollection = db.collection("registros_diarios") // Colección de registros diarios
    private val userId = auth.currentUser?.uid // UID del usuario actual

    private val _uiState = MutableStateFlow(RegistroUiState()) // Estado interno
    val uiState: StateFlow<RegistroUiState> = _uiState // Estado expuesto a la UI

    init {
        cargarRegistroDiario(LocalDate.now()) // Cargar registro del día actual al iniciar
    }

    // Actualiza fecha seleccionada y carga datos
    fun onFechaSeleccionada(fecha: LocalDate) {
        if (fecha == _uiState.value.fechaSeleccionada) return
        _uiState.update { it.copy(fechaSeleccionada = fecha) }
        cargarRegistroDiario(fecha)
    }

    // Métodos para actualizar campos y limpiar errores
    fun onPesoChange(valor: String) {
        _uiState.update { it.copy(peso = valor, errorPeso = null, mensajeError = null, mensajeExito = null) }
    }
    fun onCaloriasChange(valor: String) {
        _uiState.update { it.copy(calorias = valor, errorCalorias = null, mensajeError = null, mensajeExito = null) }
    }
    fun onSuenoChange(valor: String) {
        _uiState.update { it.copy(sueno = valor, errorSueno = null, mensajeError = null, mensajeExito = null) }
    }
    fun onPasosChange(valor: String) {
        _uiState.update { it.copy(pasos = valor, errorPasos = null, mensajeError = null, mensajeExito = null) }
    }

    // Cargar registro desde Firestore según fecha
    private fun cargarRegistroDiario(fecha: LocalDate) {
        if (userId == null) {
            _uiState.update { it.copy(mensajeError = "Usuario no autenticado.") }
            return
        }
        val fechaString = fecha.format(DateTimeFormatter.ISO_DATE)

        _uiState.update { it.copy(isLoading = true, mensajeError = null, mensajeExito = null) }

        viewModelScope.launch {
            try {
                val querySnapshot = registrosCollection
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("fecha", fechaString)
                    .limit(1)
                    .get()
                    .await()

                if (querySnapshot.isEmpty) {
                    // Si no existe registro para la fecha, limpiar campos
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
                } else {
                    // Si existe, cargar datos en campos
                    val document = querySnapshot.documents.first()
                    val registro = document.toObject(RegistroDiario::class.java)
                    _uiState.update {
                        it.copy(
                            registroId = document.id,
                            peso = registro?.peso_kg?.toString() ?: "",
                            calorias = registro?.calorias_consumidas?.toString() ?: "",
                            sueno = registro?.horas_sueno?.toString() ?: "",
                            pasos = registro?.pasos?.toString() ?: "",
                            isLoading = false
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(mensajeError = "Error al cargar datos: ${e.message}", isLoading = false) }
                e.printStackTrace()
            }
        }
    }

    // Guarda o actualiza el registro en Firestore
    fun guardarRegistro() {
        if (userId == null) {
            _uiState.update { it.copy(mensajeError = "Error: Usuario no autenticado.") }
            return
        }

        if (!validarFormulario()) return // Validaciones

        _uiState.update { it.copy(isLoading = true, mensajeError = null, mensajeExito = null) }

        val state = _uiState.value
        val registro = RegistroDiario(
            id = state.registroId,
            userId = userId,
            fecha = state.fechaSeleccionada.format(DateTimeFormatter.ISO_DATE),
            peso_kg = state.peso.toDoubleOrNull(),
            calorias_consumidas = state.calorias.toIntOrNull(),
            horas_sueno = state.sueno.toDoubleOrNull(),
            pasos = state.pasos.toIntOrNull()
        )

        viewModelScope.launch {
            try {
                if (registro.id == null) {
                    // Crear nuevo registro
                    val result = registrosCollection.add(registro).await()
                    _uiState.update {
                        it.copy(
                            registroId = result.id,
                            mensajeExito = "Registro guardado exitosamente.",
                            isLoading = false
                        )
                    }
                } else {
                    // Actualizar registro existente
                    registrosCollection.document(registro.id).set(registro).await()
                    _uiState.update { it.copy(mensajeExito = "Registro actualizado exitosamente.", isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(mensajeError = "Error al guardar datos: ${e.message}", isLoading = false) }
                e.printStackTrace()
            }
        }
    }

    // Validaciones de los campos del formulario
    private fun validarFormulario(): Boolean {
        val state = _uiState.value
        var hayErrores = false

        _uiState.update { it.copy(errorPeso = null, errorCalorias = null, errorSueno = null, errorPasos = null) }

        if (state.peso.isNotBlank()) {
            val pesoNum = state.peso.toDoubleOrNull()
            if (pesoNum == null) {
                _uiState.update { it.copy(errorPeso = "Número no válido") }
                hayErrores = true
            } else if (pesoNum <= 0) {
                _uiState.update { it.copy(errorPeso = "Debe ser un valor positivo") }
                hayErrores = true
            } else if (pesoNum > 400) {
                _uiState.update { it.copy(errorPeso = "Valor irreal (max 400kg)") }
                hayErrores = true
            }
        }

        if (state.calorias.isNotBlank()) {
            val caloriasNum = state.calorias.toIntOrNull()
            if (caloriasNum == null) {
                _uiState.update { it.copy(errorCalorias = "Número no válido (entero)") }
                hayErrores = true
            } else if (caloriasNum < 0) {
                _uiState.update { it.copy(errorCalorias = "No puede ser negativo") }
                hayErrores = true
            } else if (caloriasNum > 20000) {
                _uiState.update { it.copy(errorCalorias = "Valor irreal (max 20,000)") }
                hayErrores = true
            }
        }

        if (state.sueno.isNotBlank()) {
            val suenoNum = state.sueno.toDoubleOrNull()
            if (suenoNum == null) {
                _uiState.update { it.copy(errorSueno = "Número no válido") }
                hayErrores = true
            } else if (suenoNum <= 0) {
                _uiState.update { it.copy(errorSueno = "Debe ser un valor positivo") }
                hayErrores = true
            } else if (suenoNum > 24) {
                _uiState.update { it.copy(errorSueno = "Máximo 24 horas") }
                hayErrores = true
            }
        }

        if (state.pasos.isNotBlank()) {
            val pasosNum = state.pasos.toIntOrNull()
            if (pasosNum == null) {
                _uiState.update { it.copy(errorPasos = "Número no válido (entero)") }
                hayErrores = true
            } else if (pasosNum < 0) {
                _uiState.update { it.copy(errorPasos = "No puede ser negativo") }
                hayErrores = true
            } else if (pasosNum > 150000) {
                _uiState.update { it.copy(errorPasos = "Valor irreal (max 150,000)") }
                hayErrores = true
            }
        }

        return !hayErrores // True si está todo correcto
    }

    // Limpia mensajes después de mostrarlos
    fun clearMessages() {
        _uiState.update { it.copy(mensajeExito = null, mensajeError = null) }
    }
}
