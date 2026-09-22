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

    val estado: LiveData<LevantateApagarUiState> =
        MutableLiveData(LevantateApagarUiState(progreso = PROGRESO_MUESTRA))

    companion object {
        /** Avance que muestra el mockup. */
        const val PROGRESO_MUESTRA = 50
    }
}
