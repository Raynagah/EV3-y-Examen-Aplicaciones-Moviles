package com.example.vidasalud.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

// Representa el resultado de una operación de autenticación
sealed class ResultadoAuth {
    object Exito : ResultadoAuth() // Operación exitosa
    data class Error(val mensaje: String) : ResultadoAuth() // Error con mensaje
}

class AuthRepository {

    private val auth = FirebaseAuth.getInstance() // Instancia de Firebase Auth
    private val firestore = FirebaseFirestore.getInstance() // Instancia de Firestore

    // Iniciar sesión con email y contraseña
    suspend fun iniciarSesion(email: String, contrasena: String): ResultadoAuth {
        return try {
            auth.signInWithEmailAndPassword(email, contrasena).await() // Espera respuesta de Firebase
            ResultadoAuth.Exito
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en iniciarSesion: ${e.message}") // Log del error
            ResultadoAuth.Error(e.message ?: "Error desconocido al iniciar sesión")
        }
    }

    // Crear un nuevo usuario y guardarlo en Firestore
    suspend fun crearUsuario(nombre: String, email: String, contrasena: String): ResultadoAuth {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, contrasena).await() // Crea usuario en Auth
            val uid = authResult.user?.uid // Obtiene UID del usuario

            if (uid != null) {
                // Datos que se guardarán en Firestore
                val datosUsuario = hashMapOf(
                    "uid" to uid,
                    "nombre" to nombre,
                    "email" to email
                )

                // Guarda datos del usuario en colección "usuarios"
                firestore.collection("usuarios").document(uid)
                    .set(datosUsuario)
                    .await()

                ResultadoAuth.Exito
            } else {
                ResultadoAuth.Error("No se pudo crear el usuario (UID nulo)")
            }

        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en crearUsuario: ${e.message}") // Log del error

            // Manejo específico de error de correo existente
            val mensajeError = when {
                e.message?.contains("EMAIL_EXISTS") == true || e.message?.contains("email-already-in-use") == true ->
                    "El email ya está en uso."
                else -> e.message ?: "Error desconocido al registrar"
            }
            ResultadoAuth.Error(mensajeError)
        }
    }

    // Obtener el nombre del usuario desde Firestore
    suspend fun obtenerNombreUsuario(): String? {
        val uid = auth.currentUser?.uid ?: return null // UID del usuario actual

        return try {
            val documento = firestore.collection("usuarios").document(uid).get().await() // Obtiene documento
            documento.getString("nombre") // Devuelve nombre
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al obtener nombre de usuario: ${e.message}") // Log del error
            null
        }
    }

    // --- AGREGADO PARA QUE FUNCIONE COMUNIDAD ---
    fun obtenerUid(): String? {
        return auth.currentUser?.uid
    }
}