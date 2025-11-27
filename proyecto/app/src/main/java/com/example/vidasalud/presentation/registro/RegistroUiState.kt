package com.example.vidasalud.presentation.registro

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
data class RegistroUiState(
    val fechaSeleccionada: LocalDate = LocalDate.now(), // Fecha elegida para el registro
    val registroId: String? = null, // ID del documento en Firestore (si ya existe)

    // Valores ingresados en el formulario (String para validar fácilmente)
    val peso: String = "", // Peso en kg
    val calorias: String = "", // Calorías consumidas
    val sueno: String = "", // Horas de sueño
    val pasos: String = "", // Pasos diarios

    // Errores específicos de cada campo
    val errorPeso: String? = null,
    val errorCalorias: String? = null,
    val errorSueno: String? = null,
    val errorPasos: String? = null,

    // Estado general de la pantalla
    val isLoading: Boolean = false, // Indica proceso de guardado/carga
    val mensajeError: String? = null, // Mensaje general de error
    val mensajeExito: String? = null // Mensaje al guardar correctamente
)
