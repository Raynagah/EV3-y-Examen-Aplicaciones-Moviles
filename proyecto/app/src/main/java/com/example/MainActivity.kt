package com.example.vidasalud

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.vidasalud.ui.navigation.NavegacionApp
import com.example.vidasalud.ui.theme.VidaSaludTheme

// Actividad principal de la app
// Punto de entrada donde se monta la interfaz con Jetpack Compose
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la UI ocupe toda la pantalla (borde a borde)
        enableEdgeToEdge()

        // Configura el contenido con Compose
        setContent {

            // Tema personalizado de la aplicación
            VidaSaludTheme {

                // Superficie principal de la app
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Llama al sistema de navegación de la app
                    NavegacionApp()
                }
            }
        }
    }
}
