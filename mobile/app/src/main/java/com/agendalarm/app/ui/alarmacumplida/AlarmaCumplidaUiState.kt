package com.agendalarm.app.ui.alarmacumplida

/** Estado de M-12. [horaRegistrada] va en formato de 24 horas «HH:mm». */
data class AlarmaCumplidaUiState(
    val horaRegistrada: String,
) {
    init {
        require(FORMATO_HORA.matches(horaRegistrada)) { "La hora registrada debe ser HH:mm y es $horaRegistrada" }
    }

    companion object {
        val FORMATO_HORA = Regex("([01]\\d|2[0-3]):[0-5]\\d")
    }
}
