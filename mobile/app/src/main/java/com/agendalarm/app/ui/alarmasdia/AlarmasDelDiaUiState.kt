package com.agendalarm.app.ui.alarmasdia

private const val ID_SECCION = -1L
private const val ID_NOTA = -2L

/** Una alarma tal como se ve en su tarjeta: el compromiso que la origina y si está activa. */
data class AlarmaUi(
    val id: Long,
    val titulo: String,
    val detalle: String,
    val activa: Boolean,
)

/** Filas de la lista de M-04: el título de sección, una tarjeta por alarma y la nota final. */
sealed interface ElementoLista {

    /** Identidad estable para que DiffUtil distinga «misma fila con otro contenido» de «fila distinta». */
    val id: Long

    data class Seccion(val fecha: String, val cantidad: Int) : ElementoLista {
        override val id get() = ID_SECCION
    }

    data class Alarma(val alarma: AlarmaUi) : ElementoLista {
        override val id get() = alarma.id
    }

    data object Nota : ElementoLista {
        override val id get() = ID_NOTA
    }
}

/** Estado completo de la pantalla; la vista sólo lo dibuja. */
data class AlarmasDelDiaUiState(
    val fecha: String,
    val alarmas: List<AlarmaUi>,
) {
    fun elementos(): List<ElementoLista> = buildList {
        add(ElementoLista.Seccion(fecha, alarmas.size))
        alarmas.forEach { add(ElementoLista.Alarma(it)) }
        add(ElementoLista.Nota)
    }
}
