package com.example.vidasalud.presentation.perfil

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vidasalud.data.repository.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PerfilViewModel : ViewModel() {

    // Instancias de Firebase
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("usuarios")
    private val userId = auth.currentUser?.uid

    // Estado observable para la UI
    private val _uiState = MutableStateFlow(PerfilUiState(isLoading = true))
    val uiState: StateFlow<PerfilUiState> = _uiState

    init {
        // Al crear el ViewModel, verifica si hay usuario autenticado y carga su perfil
        if (userId != null) {
            cargarPerfil(userId)
        } else {
            _uiState.update { it.copy(isLoading = false, mensajeError = "Usuario no autenticado.") }
        }
    }

    /**
     * Obtiene los datos del usuario desde Firestore usando su UID
     */
    private fun cargarPerfil(uid: String) {
        viewModelScope.launch {
            try {
                // Obtener documento desde Firestore
                val document = usersCollection.document(uid).get().await()

                // Convertir Firestore document a objeto Usuario
                val firestoreUser = document.toObject(Usuario::class.java)

                // Obtener correo actual directamente desde Firebase Auth
                val authEmail = auth.currentUser?.email

                // Combinar datos: si existe en Firestore, actualiza campos; si no, crea usuario nuevo básico
                val finalUser = firestoreUser?.copy(
                    id = document.id, // ID del documento en Firestore
                    uid = uid,        // UID del usuario autenticado
                    correo = authEmail
                ) ?: Usuario(
                    uid = uid,
                    correo = authEmail
                )

                // Actualizar UI State con datos cargados
                _uiState.update { it.copy(currentUser = finalUser, isLoading = false) }

            } catch (e: Exception) {
                // Error al obtener datos
                _uiState.update { it.copy(mensajeError = "Error al cargar perfil: ${e.message}", isLoading = false) }
                Log.e("PerfilViewModel", "Error al cargar perfil", e)
            }
        }
    }

    /**
     * Actualiza solo la URL de la foto en el estado y luego guarda el perfil completo
     */
    fun actualizarFotoPerfil(uriString: String?) {
        _uiState.update { currentState ->
            val updatedUser = currentState.currentUser?.copy(fotoPerfilUrl = uriString)
            currentState.copy(currentUser = updatedUser)
        }
        guardarPerfil() // Guardar cambios en Firestore
    }

    /**
     * Guarda o actualiza el perfil completo del usuario en Firestore
     */
    fun guardarPerfil() {
        val userToSave = _uiState.value.currentUser ?: return

        viewModelScope.launch {
            try {
                // Guardar estructura completa del usuario
                usersCollection.document(userToSave.uid).set(userToSave).await()
                Log.d("PerfilViewModel", "Perfil guardado/actualizado exitosamente.")
            } catch (e: Exception) {
                Log.e("PerfilViewModel", "Error al guardar perfil", e)
            }
        }
    }
}
