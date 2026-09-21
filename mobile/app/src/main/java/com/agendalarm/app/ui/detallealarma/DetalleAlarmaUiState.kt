package com.agendalarm.app.ui.detallealarma

/** Una alarma tal como se ve en su detalle: el compromiso, cuándo salir de casa y con cuánto margen. */
data class DetalleAlarmaUi(
    val id: Long,
    val titulo: String,
    val hora: String,
    val lugar: String,
    val horaSalida: String,
    val margenMinutos: Int,
)

/** Estado completo de la pantalla; la vista sólo lo dibuja. */
sealed interface DetalleAlarmaUiState {

    data class Alarma(val detalle: DetalleAlarmaUi) : DetalleAlarmaUiState

    /** El identificador recibido no corresponde a ninguna alarma: no hay nada que mostrar y la pantalla se cierra. */
    data object NoEncontrada : DetalleAlarmaUiState
}
