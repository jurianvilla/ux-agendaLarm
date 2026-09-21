package com.agendalarm.app.ui.metodoconfirmacion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Estado de M-09. Sobrevive a rotaciones; el método elegido cambia aquí, no en la vista. Nada se guarda: al cerrar la
 * app vuelve a «Ponerte de pie y sostener el teléfono», que es el que trae activo el mockup.
 */
class MetodoConfirmacionViewModel : ViewModel() {

    private val _estado = MutableLiveData(MetodoConfirmacionUiState(activo = MetodoConfirmacion.DE_PIE))
    val estado: LiveData<MetodoConfirmacionUiState> = _estado

    fun seleccionar(metodo: MetodoConfirmacion) {
        val actual = _estado.value ?: return
        if (actual.activo != metodo) _estado.value = actual.conMetodoActivo(metodo)
    }
}
