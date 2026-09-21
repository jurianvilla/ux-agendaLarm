package com.agendalarm.app.ui.alarmasdia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ItemAlarmaBinding
import com.agendalarm.app.databinding.ItemNotaBinding
import com.agendalarm.app.databinding.ItemSeccionBinding

private const val TIPO_SECCION = 0
private const val TIPO_ALARMA = 1
private const val TIPO_NOTA = 2

/** Lista de M-04: una sola lista con tres tipos de fila, para que título, tarjetas y nota se desplacen juntos. */
class AlarmasAdapter(
    private val alAlternar: (id: Long, activa: Boolean) -> Unit,
    private val alPulsar: (id: Long) -> Unit,
) : ListAdapter<ElementoLista, RecyclerView.ViewHolder>(Comparador) {

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is ElementoLista.Seccion -> TIPO_SECCION
        is ElementoLista.Alarma -> TIPO_ALARMA
        ElementoLista.Nota -> TIPO_NOTA
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflador = LayoutInflater.from(parent.context)
        return when (viewType) {
            TIPO_SECCION -> SeccionHolder(ItemSeccionBinding.inflate(inflador, parent, false))
            TIPO_ALARMA -> AlarmaHolder(ItemAlarmaBinding.inflate(inflador, parent, false), alAlternar, alPulsar)
            else -> NotaHolder(ItemNotaBinding.inflate(inflador, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val elemento = getItem(position)) {
            is ElementoLista.Seccion -> (holder as SeccionHolder).enlazar(elemento)
            is ElementoLista.Alarma -> (holder as AlarmaHolder).enlazar(elemento.alarma)
            ElementoLista.Nota -> Unit
        }
    }

    private object Comparador : DiffUtil.ItemCallback<ElementoLista>() {
        override fun areItemsTheSame(anterior: ElementoLista, nuevo: ElementoLista) = anterior.id == nuevo.id
        override fun areContentsTheSame(anterior: ElementoLista, nuevo: ElementoLista) = anterior == nuevo
    }

    private class SeccionHolder(private val binding: ItemSeccionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun enlazar(seccion: ElementoLista.Seccion) {
            val recursos = itemView.resources
            val compromisos = recursos.getQuantityString(R.plurals.compromisos, seccion.cantidad, seccion.cantidad)
            binding.texto = recursos.getString(R.string.alarmas_dia_seccion, seccion.fecha, compromisos)
            binding.executePendingBindings()
        }
    }

    private class AlarmaHolder(
        private val binding: ItemAlarmaBinding,
        private val alAlternar: (id: Long, activa: Boolean) -> Unit,
        private val alPulsar: (id: Long) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            // TalkBack anuncia «doble toque para ver el detalle de la alarma» en vez del genérico «para activar».
            ViewCompat.replaceAccessibilityAction(
                binding.root,
                AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK,
                itemView.resources.getString(R.string.alarma_ver_detalle),
                null,
            )
        }

        fun enlazar(alarma: AlarmaUi) {
            binding.alarma = alarma
            // La tarjeta abre el detalle; el interruptor sigue siendo un control aparte dentro de ella.
            binding.root.setOnClickListener { alPulsar(alarma.id) }
            // Sin oyente mientras se fija el estado: reciclar la fila no debe contar como un toque del usuario.
            binding.interruptor.setOnCheckedChangeListener(null)
            binding.interruptor.isChecked = alarma.activa
            binding.interruptor.setOnCheckedChangeListener { _, activa -> alAlternar(alarma.id, activa) }
            binding.executePendingBindings()
        }
    }

    private class NotaHolder(binding: ItemNotaBinding) : RecyclerView.ViewHolder(binding.root)
}
