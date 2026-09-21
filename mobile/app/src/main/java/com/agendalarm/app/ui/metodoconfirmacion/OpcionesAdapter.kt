package com.agendalarm.app.ui.metodoconfirmacion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ItemIntroduccionBinding
import com.agendalarm.app.databinding.ItemOpcionMetodoBinding
import com.agendalarm.app.ui.animacion.conTransicion

private const val TIPO_INTRODUCCION = 0
private const val TIPO_OPCION = 1

/** Avisa de que sólo cambió si la tarjeta está activa, para animar ese cambio en vez de redibujarla de golpe. */
private const val CAMBIO_ESTADO = "estado"

/** Lista de M-09: una sola lista (introducción y tarjetas) para que se desplacen juntas. */
class OpcionesAdapter(
    private val alSeleccionar: (MetodoConfirmacion) -> Unit,
) : ListAdapter<ElementoLista, RecyclerView.ViewHolder>(Comparador) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        ElementoLista.Introduccion -> TIPO_INTRODUCCION
        is ElementoLista.Opcion -> TIPO_OPCION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflador = LayoutInflater.from(parent.context)
        return when (viewType) {
            TIPO_INTRODUCCION -> IntroduccionHolder(ItemIntroduccionBinding.inflate(inflador, parent, false))
            else -> OpcionHolder(ItemOpcionMetodoBinding.inflate(inflador, parent, false), alSeleccionar)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        enlazar(holder, position, animar = false)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        enlazar(holder, position, animar = CAMBIO_ESTADO in payloads)
    }

    private fun enlazar(holder: RecyclerView.ViewHolder, position: Int, animar: Boolean) {
        val elemento = getItem(position)
        if (holder is OpcionHolder && elemento is ElementoLista.Opcion) holder.enlazar(elemento.opcion, animar)
    }

    private object Comparador : DiffUtil.ItemCallback<ElementoLista>() {
        override fun areItemsTheSame(anterior: ElementoLista, nuevo: ElementoLista) = anterior.id == nuevo.id
        override fun areContentsTheSame(anterior: ElementoLista, nuevo: ElementoLista) = anterior == nuevo
        override fun getChangePayload(anterior: ElementoLista, nuevo: ElementoLista): Any = CAMBIO_ESTADO
    }

    private class IntroduccionHolder(binding: ItemIntroduccionBinding) : RecyclerView.ViewHolder(binding.root)

    private class OpcionHolder(
        private val binding: ItemOpcionMetodoBinding,
        private val alSeleccionar: (MetodoConfirmacion) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            // TalkBack anuncia «doble toque para elegir este método» en vez del genérico «para activar».
            ViewCompat.replaceAccessibilityAction(
                binding.root,
                AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK,
                itemView.resources.getString(R.string.metodo_elegir),
                null,
            )
        }

        fun enlazar(opcion: OpcionUi, animar: Boolean) {
            // La transición se arma antes de cambiar la fila: anima el texto «Activo» y el desplazamiento del contenido.
            if (animar) binding.tarjeta.conTransicion { aplicar(opcion) } else aplicar(opcion)
        }

        private fun aplicar(opcion: OpcionUi) {
            binding.opcion = opcion
            // La tarjeta entera elige el método; el interruptor sigue siendo un control aparte dentro de ella.
            binding.tarjeta.setOnClickListener { alSeleccionar(opcion.metodo) }
            // Sin oyente mientras se fija el estado: reciclar la fila no debe contar como un toque del usuario.
            binding.interruptor.setOnCheckedChangeListener(null)
            binding.interruptor.isChecked = opcion.activa
            binding.interruptor.setOnCheckedChangeListener { interruptor, marcado ->
                // Siempre hay un método activo: apagar el activo no hace nada, el interruptor vuelve a quedar encendido.
                if (marcado) alSeleccionar(opcion.metodo) else interruptor.isChecked = true
            }
            binding.executePendingBindings()
        }
    }
}
