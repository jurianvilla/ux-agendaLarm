package com.agendalarm.app.ui.detallealarma

import androidx.lifecycle.SavedStateHandle
import com.agendalarm.app.ui.alarmasdia.AlarmasMuestra
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** El ViewModel de M-06 sólo depende del id que recibe: se prueba en la JVM, sin emulador. */
class DetalleAlarmaViewModelTest {

    private fun viewModelCon(vararg argumentos: Pair<String, Any?>) =
        DetalleAlarmaViewModel(SavedStateHandle(mapOf(*argumentos)))

    @Test
    fun `una alarma conocida expone su detalle`() {
        val estado = viewModelCon(DetalleAlarmaViewModel.ARGUMENTO_ALARMA_ID to 2L).estado.value

        val detalle = (estado as DetalleAlarmaUiState.Alarma).detalle
        assertEquals("Clase Bases de Datos", detalle.titulo)
        assertEquals("08:30", detalle.hora)
        assertEquals("Universidad : Aula 302 - Edificio 3", detalle.lugar)
        assertEquals("07:52", detalle.horaSalida)
        assertEquals(22, detalle.margenMinutos)
    }

    @Test
    fun `un id desconocido no tiene nada que mostrar`() {
        val estado = viewModelCon(DetalleAlarmaViewModel.ARGUMENTO_ALARMA_ID to 99L).estado.value

        assertEquals(DetalleAlarmaUiState.NoEncontrada, estado)
    }

    @Test
    fun `sin id no tiene nada que mostrar`() {
        assertEquals(DetalleAlarmaUiState.NoEncontrada, viewModelCon().estado.value)
    }

    @Test
    fun `cada tarjeta de M-04 tiene un detalle con su mismo titulo y hora`() {
        AlarmasMuestra.alarmas.forEach { alarma ->
            val detalle = requireNotNull(DetalleAlarmaMuestra.porId(alarma.id)) {
                "La tarjeta «${alarma.titulo}» de M-04 no tiene detalle en DetalleAlarmaMuestra"
            }
            assertEquals(alarma.titulo, detalle.titulo)
            assertTrue(
                "«${alarma.detalle}» debería empezar por la hora ${detalle.hora}",
                alarma.detalle.startsWith(detalle.hora),
            )
        }
    }
}
