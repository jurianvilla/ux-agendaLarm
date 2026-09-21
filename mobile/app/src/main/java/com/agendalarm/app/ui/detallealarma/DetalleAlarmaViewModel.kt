package com.agendalarm.app.ui.detallealarma

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

/**
 * Estado de M-06. La alarma que se muestra llega como argumento del Intent (`SavedStateHandle` recibe sus extras),
 * así que sobrevive a rotaciones y también a que el sistema cierre el proceso.
 */
class DetalleAlarmaViewModel(estadoGuardado: SavedStateHandle) : ViewModel() {

    val estado: LiveData<DetalleAlarmaUiState> = MutableLiveData(
        estadoGuardado.get<Long>(ARGUMENTO_ALARMA_ID)
            ?.let(DetalleAlarmaMuestra::porId)
            ?.let { DetalleAlarmaUiState.Alarma(it) }
            ?: DetalleAlarmaUiState.NoEncontrada
    )

    companion object {
        /** Clave del extra con el id de la alarma; la escribe `M06DetalleAlarmaActivity.intent`. */
        const val ARGUMENTO_ALARMA_ID = "alarma_id"
    }
}
