package com.agendalarm.app.ui.levantateapagar

/**
 * Estado completo de M-11; la vista sólo lo dibuja. La barra y el texto «50%» salen del mismo [progreso]: el
 * porcentaje, de 0 a 100, del tiempo de pie que ya se cumplió para confirmar que se despertó.
 */
data class LevantateApagarUiState(
    val progreso: Int,
) {
    init {
        require(progreso in RANGO_PROGRESO) { "El progreso debe estar entre 0 y 100 y es $progreso" }
    }

    companion object {
        val RANGO_PROGRESO = 0..100
    }
}
