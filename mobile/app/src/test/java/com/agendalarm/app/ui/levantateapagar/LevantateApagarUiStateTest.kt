package com.agendalarm.app.ui.levantateapagar

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

/** El avance de M-11 es un porcentaje: la regla vive en el estado y se prueba en la JVM, sin emulador. */
class LevantateApagarUiStateTest {

    @Test
    fun `los dos extremos del avance son validos`() {
        assertEquals(0, LevantateApagarUiState(progreso = 0).progreso)
        assertEquals(100, LevantateApagarUiState(progreso = 100).progreso)
    }

    @Test
    fun `un avance fuera de 0 a 100 no es un estado valido`() {
        assertThrows(IllegalArgumentException::class.java) { LevantateApagarUiState(progreso = -1) }
        assertThrows(IllegalArgumentException::class.java) { LevantateApagarUiState(progreso = 101) }
    }

    @Test
    fun `el avance inicial del ViewModel es el que trae el mockup`() {
        val estado = LevantateApagarViewModel().estado.value

        assertEquals(LevantateApagarViewModel.PROGRESO_MUESTRA, estado?.progreso)
        assertEquals(50, estado?.progreso)
    }

    @Test
    fun `el avance de muestra cabe en el rango del estado`() {
        assertTrue(LevantateApagarViewModel.PROGRESO_MUESTRA in LevantateApagarUiState.RANGO_PROGRESO)
    }

    @Test
    fun `avanzar suma un punto`() {
        assertEquals(51, LevantateApagarUiState(progreso = 50).avanzado().progreso)
    }

    @Test
    fun `el avance no pasa de 100 y ahi queda completo`() {
        val lleno = LevantateApagarUiState(progreso = 100)

        assertTrue(lleno.completo)
        assertEquals(100, lleno.avanzado().progreso)
        assertFalse(LevantateApagarUiState(progreso = 99).completo)
    }
}
