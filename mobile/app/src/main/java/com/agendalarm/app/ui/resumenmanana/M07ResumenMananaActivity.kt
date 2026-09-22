package com.agendalarm.app.ui.resumenmanana

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ActivityM07ResumenMananaBinding
import com.agendalarm.app.ui.alarmasdia.M04AlarmasDelDiaActivity
import com.agendalarm.app.ui.base.BaseActivity

class M07ResumenMananaActivity : BaseActivity() {

    private val viewModel: ResumenMananaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityM07ResumenMananaBinding>(
            this,
            R.layout.activity_m07_resumen_manana,
        )
        binding.lifecycleOwner = this
        setTitle(R.string.m07_titulo)
        aplicarInsets(encabezado = binding.encabezado.root, contenido = binding.contenido)
        activarLista(binding)
    }

    private fun activarLista(binding: ActivityM07ResumenMananaBinding) {
        val adaptador = CompromisosAdapter()
        binding.listaCompromisos.adapter = adaptador
        viewModel.estado.observe(this) { estado ->
            adaptador.submitList(estado.compromisos)
            val cantidad = estado.compromisos.size
            binding.botonVerAlarmas.text = resources.getQuantityString(
                R.plurals.compromisos_programados,
                cantidad,
                cantidad,
            )
        }
        binding.botonVerAlarmas.setOnClickListener {
            startActivity(Intent(this, M04AlarmasDelDiaActivity::class.java))
        }
    }
}
