package com.agendalarm.app.ui.resumenmanana

/** Implementación en memoria de [ResumenMananaRepositorio]; sustituible por una fuente real sin tocar el ViewModel. */
class ResumenMananaRepositorioMuestra : ResumenMananaRepositorio {

    override fun obtenerCompromisosDeManana(): List<CompromisoUi> = listOf(
        CompromisoUi(id = 1, titulo = "Clase Bases de Datos", detalle = "08:30 · Universidad"),
        CompromisoUi(id = 2, titulo = "Reunión equipo", detalle = "11:00 · Trabajo"),
        CompromisoUi(id = 3, titulo = "Tutoría", detalle = "15:30 · Universidad"),
    )
}
