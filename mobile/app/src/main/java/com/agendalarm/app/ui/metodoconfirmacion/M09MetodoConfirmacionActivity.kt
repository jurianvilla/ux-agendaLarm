package com.agendalarm.app.ui.metodoconfirmacion

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ActivityM09MetodoConfirmacionBinding
import com.agendalarm.app.ui.animacion.usarAnimacionesAgendaLarm
import com.agendalarm.app.ui.base.BaseActivity

/**
 * M-09 · ¿Cómo quieres confirmar que despertaste? Selección del método de confirmación entre ponerse de pie
 * sosteniendo el teléfono o registrar una fotografía: una tarjeta por método, con un solo método activo.
 */
class M09MetodoConfirmacionActivity : BaseActivity() {

    private val viewModel: MetodoConfirmacionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityM09MetodoConfirmacionBinding>(
            this,
            R.layout.activity_m09_metodo_confirmacion,
        )
        binding.lifecycleOwner = this
        setTitle(R.string.m09_titulo)
        aplicarInsets(encabezado = binding.encabezado.root, contenido = binding.listaOpciones)
        activarLista(binding)
    }

    private fun activarLista(binding: ActivityM09MetodoConfirmacionBinding) {
        val adaptador = OpcionesAdapter(alSeleccionar = viewModel::seleccionar)
        binding.listaOpciones.adapter = adaptador
        binding.listaOpciones.usarAnimacionesAgendaLarm()
        viewModel.estado.observe(this) { estado -> adaptador.submitList(estado.elementos()) }
    }
}
