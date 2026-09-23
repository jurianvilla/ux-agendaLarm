package com.agendalarm.app.ui.alarmacumplida

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.Clock
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/** Estado de M-12. La hora registrada es la del dispositivo al abrir la pantalla. */
class AlarmaCumplidaViewModel(reloj: Clock = Clock.systemDefaultZone()) : ViewModel() {

    val estado: LiveData<AlarmaCumplidaUiState> =
        MutableLiveData(AlarmaCumplidaUiState(horaRegistrada = LocalTime.now(reloj).format(FORMATO_HORA)))

    companion object {
        private val FORMATO_HORA: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}
