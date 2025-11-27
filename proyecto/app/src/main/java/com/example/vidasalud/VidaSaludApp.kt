package com.example.vidasalud

import android.app.Application
import com.google.firebase.FirebaseApp

// Clase Application de la app VidaSalud
// Se ejecuta antes que cualquier Activity o Composable al iniciar la app.
// Se usa para inicializar configuraciones globales, como Firebase.
class VidaSaludApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Inicializa Firebase cuando la aplicación arranca
        // Esto asegura que Firebase esté listo antes de usar Auth, Firestore, etc.
        FirebaseApp.initializeApp(this)
    }
}
