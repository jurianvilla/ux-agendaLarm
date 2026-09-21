package com.agendalarm.app.ui.metodoconfirmacion

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** La regla de M-09 (un solo método activo, siempre uno) vive en el estado: se prueba en la JVM, sin emulador. */
class MetodoConfirmacionUiStateTest {

    private fun opciones(estado: MetodoConfirmacionUiState) =
        estado.elementos().filterIsInstance<ElementoLista.Opcion>().map { it.opcion }

    @Test
    fun `la lista empieza por la introduccion y sigue con una tarjeta por metodo`() {
        val elementos = MetodoConfirmacionUiState(activo = MetodoConfirmacion.DE_PIE).elementos()

        assertEquals(ElementoLista.Introduccion, elementos.first())
        assertEquals(MetodoConfirmacion.entries, opciones(MetodoConfirmacionUiState(MetodoConfirmacion.DE_PIE)).map { it.metodo })
        assertEquals(1 + MetodoConfirmacion.entries.size, elementos.size)
    }

    @Test
    fun `solo el metodo activo esta marcado`() {
        MetodoConfirmacion.entries.forEach { activo ->
            val marcadas = opciones(MetodoConfirmacionUiState(activo)).filter { it.activa }

            assertEquals(listOf(activo), marcadas.map { it.metodo })
        }
    }

    @Test
    fun `elegir otro metodo desactiva el anterior`() {
        val inicial = MetodoConfirmacionUiState(activo = MetodoConfirmacion.DE_PIE)

        val nuevo = inicial.conMetodoActivo(MetodoConfirmacion.FOTO)

        assertEquals(MetodoConfirmacion.FOTO, nuevo.activo)
        assertTrue(opciones(nuevo).single { it.metodo == MetodoConfirmacion.FOTO }.activa)
        assertTrue(opciones(nuevo).none { it.metodo == MetodoConfirmacion.DE_PIE && it.activa })
    }

    @Test
    fun `cada fila tiene un id distinto y estable`() {
        val antes = MetodoConfirmacionUiState(MetodoConfirmacion.DE_PIE).elementos().map { it.id }
        val despues = MetodoConfirmacionUiState(MetodoConfirmacion.FOTO).elementos().map { it.id }

        assertEquals(antes.size, antes.toSet().size)
        assertEquals("elegir otro método no cambia la identidad de las filas", antes, despues)
    }

    @Test
    fun `el metodo inicial del ViewModel es el que trae activo el mockup`() {
        val estado = MetodoConfirmacionViewModel().estado.value

        assertEquals(MetodoConfirmacion.DE_PIE, estado?.activo)
    }
}
