package com.agendalarm.app.ui.detallealarma

/**
 * Datos de muestra de M-06, uno por cada tarjeta de M-04 (mismos id, título y hora que en `AlarmasMuestra`).
 * Sólo «Clase Bases de Datos» son los textos reales del mockup; el resto se inventó para poder abrir el detalle
 * desde cualquier tarjeta. Fase de sólo interfaz: cuando exista la fuente de datos real, el ViewModel deja de
 * leer de aquí y pasa a pedirlos a un repositorio.
 */
object DetalleAlarmaMuestra {

    private val detalles = listOf(
        DetalleAlarmaUi(
            id = 1,
            titulo = "Despertar",
            hora = "07:00",
            lugar = "Casa : Habitación principal",
            horaSalida = "07:52",
            margenMinutos = 22,
        ),
        DetalleAlarmaUi(
            id = 2,
            titulo = "Clase Bases de Datos",
            hora = "08:30",
            lugar = "Universidad : Aula 302 - Edificio 3",
            horaSalida = "07:52",
            margenMinutos = 22,
        ),
        DetalleAlarmaUi(
            id = 3,
            titulo = "Reunión equipo",
            hora = "11:00",
            lugar = "Trabajo : Sala de juntas - Piso 2",
            horaSalida = "10:35",
            margenMinutos = 25,
        ),
        DetalleAlarmaUi(
            id = 4,
            titulo = "Tutoría",
            hora = "15:30",
            lugar = "Universidad : Oficina 204 - Edificio 5",
            horaSalida = "15:05",
            margenMinutos = 25,
        ),
    )

    fun porId(id: Long): DetalleAlarmaUi? = detalles.firstOrNull { it.id == id }
}
