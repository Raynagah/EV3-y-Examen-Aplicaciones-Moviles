package com.example.vidasalud

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.vidasalud.ui.navigation.NavegacionApp
import com.example.vidasalud.ui.navigation.RutasApp
import com.example.vidasalud.ui.theme.VidaSaludTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // LÓGICA MODO AVIÓN / PERSISTENCIA LOCAL
        // 1. Verificamos caché de Firebase
        val currentUser = FirebaseAuth.getInstance().currentUser

        // 2. Verificamos nuestra bandera local en SharedPreferences
        val sharedPreferences = getSharedPreferences("VidaSaludPrefs", Context.MODE_PRIVATE)
        val isLoggedInLocal = sharedPreferences.getBoolean("is_logged_in", false)

        // Si cualquiera de los dos dice que sí, entramos directo
        val startDestination = if (currentUser != null || isLoggedInLocal) {
            RutasApp.PantallaPrincipal.ruta
        } else {
            RutasApp.PantallaBienvenida.ruta
        }

        setContent {
            VidaSaludTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pasamos la ruta inicial calculada
                    NavegacionApp(startDestination = startDestination)
                }
            }
        }
    }
}