package com.agendalarm.app.ui.metodoconfirmacion

import androidx.annotation.StringRes
import com.agendalarm.app.R

private const val ID_INTRODUCCION = -1L

/** Formas de confirmar que se despertó; cada una tiene su tarjeta en M-09. */
enum class MetodoConfirmacion(@param:StringRes val titulo: Int) {
    DE_PIE(R.string.metodo_de_pie),
    FOTO(R.string.metodo_foto),
}

/** Una tarjeta de M-09: el método y si es el que está activo. */
data class OpcionUi(
    val metodo: MetodoConfirmacion,
    val activa: Boolean,
)

/** Filas de la lista de M-09: el texto introductorio y una tarjeta por método. */
sealed interface ElementoLista {

    /** Identidad estable para que DiffUtil distinga «misma fila con otro contenido» de «fila distinta». */
    val id: Long

    data object Introduccion : ElementoLista {
        override val id get() = ID_INTRODUCCION
    }

    data class Opcion(val opcion: OpcionUi) : ElementoLista {
        override val id get() = opcion.metodo.ordinal.toLong()
    }
}

/**
 * Estado completo de la pantalla; la vista sólo lo dibuja. Un solo método puede estar activo a la vez (siempre hay
 * uno): elegir otro desactiva el anterior.
 */
data class MetodoConfirmacionUiState(
    val activo: MetodoConfirmacion,
) {
    fun conMetodoActivo(metodo: MetodoConfirmacion) = copy(activo = metodo)

    fun elementos(): List<ElementoLista> = buildList {
        add(ElementoLista.Introduccion)
        MetodoConfirmacion.entries.forEach { add(ElementoLista.Opcion(OpcionUi(it, activa = it == activo))) }
    }
}
