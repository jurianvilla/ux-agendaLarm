package com.agendalarm.app.ui.alarmasdia

/**
 * Datos de muestra de M-04 (textos reales del mockup). Fase de sólo interfaz: cuando exista la fuente de datos
 * real, el ViewModel deja de leer de aquí y pasa a pedirlos a un repositorio.
 */
object AlarmasMuestra {
    const val FECHA = "martes 12"

    val alarmas = listOf(
        AlarmaUi(id = 1, titulo = "Despertar", detalle = "07:00 · Hora de inicio", activa = true),
        AlarmaUi(id = 2, titulo = "Clase Bases de Datos", detalle = "08:30 · Universidad", activa = true),
        AlarmaUi(id = 3, titulo = "Reunión equipo", detalle = "11:00 · Trabajo", activa = true),
        AlarmaUi(id = 4, titulo = "Tutoría", detalle = "15:30 · Universidad", activa = false),
    )
}
