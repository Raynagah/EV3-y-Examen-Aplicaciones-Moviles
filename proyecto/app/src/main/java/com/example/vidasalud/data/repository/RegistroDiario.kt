package com.example.vidasalud.data.repository

import com.google.firebase.firestore.DocumentId

// Modelo de datos para un registro diario del usuario
data class RegistroDiario(
    @DocumentId val id: String? = null, // ID automático del documento en Firestore
    val userId: String = "", // ID del usuario propietario del registro
    val fecha: String = "", // Fecha del registro (string)
    val peso_kg: Double? = null, // Peso del usuario en kg
    val calorias_consumidas: Int? = null, // Calorías consumidas en el día
    val horas_sueno: Double? = null, // Horas dormidas
    val pasos: Int? = null, // Cantidad de pasos del día
) {
    // Constructor vacío requerido por Firestore para deserializar datos
    constructor() : this(
        id = null,
        userId = "",
        fecha = "",
        peso_kg = null,
        calorias_consumidas = null,
        horas_sueno = null,
        pasos = null
    )
}
