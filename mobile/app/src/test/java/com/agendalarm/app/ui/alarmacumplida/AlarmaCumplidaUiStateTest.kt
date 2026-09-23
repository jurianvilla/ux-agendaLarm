package com.agendalarm.app.ui.alarmacumplida

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

/** Pruebas del estado de M-12. */
class AlarmaCumplidaUiStateTest {

    @Test
    fun `los extremos del dia son horas validas`() {
        assertEquals("00:00", AlarmaCumplidaUiState(horaRegistrada = "00:00").horaRegistrada)
        assertEquals("23:59", AlarmaCumplidaUiState(horaRegistrada = "23:59").horaRegistrada)
    }

    @Test
    fun `una hora fuera del formato HH mm no es un estado valido`() {
        listOf("7:03", "24:00", "07:60", "07.03", "").forEach { hora ->
            assertThrows(IllegalArgumentException::class.java) { AlarmaCumplidaUiState(horaRegistrada = hora) }
        }
    }

    @Test
    fun `la hora registrada es la del reloj al abrir la pantalla`() {
        val reloj = Clock.fixed(Instant.parse("2026-09-22T07:03:41Z"), ZoneOffset.UTC)

        assertEquals("07:03", AlarmaCumplidaViewModel(reloj).estado.value?.horaRegistrada)
    }

    @Test
    fun `la hora registrada usa la zona horaria del reloj`() {
        val reloj = Clock.fixed(Instant.parse("2026-09-22T12:03:00Z"), ZoneOffset.ofHours(-5))

        assertEquals("07:03", AlarmaCumplidaViewModel(reloj).estado.value?.horaRegistrada)
    }

    @Test
    fun `las horas de la tarde se registran en formato de 24 horas`() {
        val reloj = Clock.fixed(Instant.parse("2026-09-22T19:05:00Z"), ZoneOffset.UTC)

        assertEquals("19:05", AlarmaCumplidaViewModel(reloj).estado.value?.horaRegistrada)
    }
}
