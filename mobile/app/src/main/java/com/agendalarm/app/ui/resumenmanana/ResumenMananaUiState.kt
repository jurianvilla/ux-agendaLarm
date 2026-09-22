package com.agendalarm.app.ui.resumenmanana

/** Un compromiso tal como se ve en su tarjeta dentro de M-07. */
data class CompromisoUi(
    val id: Long,
    val titulo: String,
    val detalle: String,
)

data class ResumenMananaUiState(val compromisos: List<CompromisoUi>)
