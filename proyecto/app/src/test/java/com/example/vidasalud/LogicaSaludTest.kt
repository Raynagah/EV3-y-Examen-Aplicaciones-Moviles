package com.example.vidasalud

import com.example.vidasalud.domain.LogicaSalud
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LogicaSaludTest {

    @Test
    fun `dado que pasos superan la meta, debe felicitar`() {
        // Preparación (Given)
        val pasos = 6000
        val meta = 5000
        val sueno = 7.0

        // Ejecución (When)
        val resultado = LogicaSalud.generarConsejo(pasos, sueno, meta)

        // Verificación (Then)
        assertTrue(resultado.contains("¡Increíble!"))
    }

    @Test
    fun `dado poco sueño, debe recomendar descansar`() {
        // Si duerme 4 horas, aunque camine mucho o poco, la prioridad es dormir
        val pasos = 2000
        val meta = 5000
        val sueno = 4.0

        val resultado = LogicaSalud.generarConsejo(pasos, sueno, meta)

        assertTrue(resultado.contains("Has dormido poco"))
    }

    @Test
    fun `dado sin datos, debe invitar a registrar`() {
        val pasos = 0
        val meta = 5000
        val sueno = 0.0

        val resultado = LogicaSalud.generarConsejo(pasos, sueno, meta)

        assertEquals("No hay datos recientes. ¡Ve a la pestaña 'Datos' y registra tu día!", resultado)
    }

    @Test
    fun `dado medio camino de la meta, debe motivar con porcentaje`() {
        val pasos = 2600
        val meta = 5000
        val sueno = 7.0

        val resultado = LogicaSalud.generarConsejo(pasos, sueno, meta)

        // 2600 de 5000 es 52%
        assertTrue(resultado.contains("52%"))
    }
}