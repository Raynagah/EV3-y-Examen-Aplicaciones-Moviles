package com.example.vidasalud.data.repository

import com.google.firebase.firestore.DocumentId

// Modelo que representa un usuario almacenado en Firestore
data class Usuario(
    @DocumentId val id: String? = null, // ID del documento en Firestore
    val uid: String = "", // UID del usuario en Firebase Auth
    val nombre: String? = null, // Nombre del usuario
    val correo: String? = null, // Correo electrónico del usuario
    val fotoPerfilUrl: String? = null, // URL de la foto de perfil (si tiene)
) {
    // Constructor vacío necesario para que Firestore pueda crear la instancia
    constructor() : this(
        id = null,
        uid = "",
        nombre = null,
        correo = null,
        fotoPerfilUrl = null
    )
}
