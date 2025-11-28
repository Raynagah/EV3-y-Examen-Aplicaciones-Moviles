package com.example.vidasalud.domain

object LogicaSalud {

    fun generarConsejo(pasos: Int, sueno: Double, metaPasos: Int): String {
        return when {
            // Caso 1: Meta superada
            pasos >= metaPasos -> "¡Increíble! Has superado tu meta de $metaPasos pasos. 🎉"

            // Caso 2: Progreso mayor al 50%
            pasos > (metaPasos / 2) -> "¡Bien! Llevas el ${(pasos * 100) / metaPasos}% de tu meta diaria."

            // Caso 3: Poco sueño (Prioridad alta)
            sueno > 0 && sueno < 6 -> "Has dormido poco ($sueno hrs). Prioriza tu descanso hoy."

            // Caso 4: Sedentarismo
            pasos > 0 && pasos < 4000 -> "Llevas $pasos pasos. ¡Intenta salir a caminar un poco!"

            // Caso 5: Descanso ideal
            sueno >= 7 && sueno <= 9 -> "Tu descanso de $sueno horas es óptimo. ¡Sigue así!"

            // Caso 6: Sin datos
            pasos == 0 && sueno == 0.0 -> "No hay datos recientes. ¡Ve a la pestaña 'Datos' y registra tu día!"

            // Default
            else -> "Mantén tus hábitos saludables y registra tu progreso."
        }
    }
}