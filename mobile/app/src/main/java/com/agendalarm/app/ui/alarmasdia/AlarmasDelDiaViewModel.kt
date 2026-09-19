package com.agendalarm.app.ui.alarmasdia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/** Estado de M-04. Sobrevive a rotaciones; el interruptor de cada tarjeta cambia aquí, no en la vista. */
class AlarmasDelDiaViewModel : ViewModel() {

    private val _estado = MutableLiveData(
        AlarmasDelDiaUiState(fecha = AlarmasMuestra.FECHA, alarmas = AlarmasMuestra.alarmas)
    )
    val estado: LiveData<AlarmasDelDiaUiState> = _estado

    fun alternarAlarma(id: Long, activa: Boolean) {
        val actual = _estado.value ?: return
        _estado.value = actual.copy(
            alarmas = actual.alarmas.map { if (it.id == id) it.copy(activa = activa) else it }
        )
    }
}
