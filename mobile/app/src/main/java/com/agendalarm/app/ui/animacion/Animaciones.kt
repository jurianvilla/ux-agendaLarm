package com.agendalarm.app.ui.animacion

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Transition
import androidx.transition.TransitionInflater
import androidx.transition.TransitionManager
import com.agendalarm.app.R

/*
 * Movimiento compartido de la app (equivale al bloque de transición de web/css/styles.css y a
 * conTransicion() de web/js/main.js). Duración, curva y tipo de transición viven en res/, no aquí:
 *   - cambio de pantalla → tema: android:windowAnimationStyle (Animacion.AgendaLarm.Ventana), sin código;
 *   - cambio dentro de una pantalla → conTransicion() (res/transition/transicion_estandar.xml);
 *   - filas de una lista → usarAnimacionesAgendaLarm().
 */

/** Transición estándar de la app (fundido + cambio de límites, 220 ms, ease-in-out). */
fun transicionEstandar(contexto: Context): Transition =
    requireNotNull(TransitionInflater.from(contexto).inflateTransition(R.transition.transicion_estandar)) {
        "res/transition/transicion_estandar.xml no se pudo inflar"
    }

/**
 * Anima todo cambio de vistas que ocurra dentro de [cambio] (mostrar u ocultar bloques, agregar o quitar filas)
 * con la misma transición que se usa entre pantallas. Se llama sobre el contenedor que cambia:
 *
 *     binding.raiz.conTransicion { binding.bloqueImportado.isVisible = true }
 */
fun ViewGroup.conTransicion(cambio: () -> Unit) {
    TransitionManager.beginDelayedTransition(this, transicionEstandar(context))
    cambio()
}

/**
 * Animaciones de altas, bajas y movimientos de filas a la duración estándar. Los cambios de contenido de una
 * fila no se animan como «fila nueva»: así el interruptor conserva su propia animación al cambiar de estado.
 */
fun RecyclerView.usarAnimacionesAgendaLarm() {
    val duracion = resources.getInteger(R.integer.duracion_transicion).toLong()
    itemAnimator = DefaultItemAnimator().apply {
        addDuration = duracion
        removeDuration = duracion
        moveDuration = duracion
        changeDuration = duracion
        supportsChangeAnimations = false
    }
}
