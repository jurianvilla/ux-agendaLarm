package com.agendalarm.app.ui.levantateapagar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Estado de M-11. Sobrevive a rotaciones. Fase de sólo interfaz: el avance es el dato de muestra del mockup (50 %) y
 * no cambia, porque aún no se lee el acelerómetro; cuando exista, el ViewModel dejará de fijar este valor y pasará
 * a recibir el avance de la fuente que mida el movimiento.
 */
class LevantateApagarViewModel : ViewModel() {

    private val _estado = MutableLiveData(LevantateApagarUiState(progreso = PROGRESO_MUESTRA))
    val estado: LiveData<LevantateApagarUiState> = _estado

    /** Demo: la Activity lo llama a intervalos fijos hasta completar la barra. */
    fun avanzar() {
        val actual = _estado.value ?: return
        if (!actual.completo) _estado.value = actual.avanzado()
    }

    companion object {
        /** Avance que muestra el mockup. */
        const val PROGRESO_MUESTRA = 50
    }
}
