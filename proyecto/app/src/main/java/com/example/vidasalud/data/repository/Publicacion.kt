package com.example.vidasalud.data.repository

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

// Modelo de datos para una publicación en la comunidad
data class Publicacion(
    @DocumentId val id: String? = null, // ID automático de Firestore
    val userId: String = "",
    val nombreUsuario: String = "Anónimo",
    val contenido: String = "",
    @ServerTimestamp val fecha: Date? = null // Firestore pondrá la fecha del servidor automáticamente
) {
    // Constructor vacío necesario para Firestore
    constructor() : this(null, "", "Anónimo", "", null)
}