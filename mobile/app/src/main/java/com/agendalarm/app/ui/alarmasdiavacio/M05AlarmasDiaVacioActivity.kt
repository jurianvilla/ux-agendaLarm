package com.agendalarm.app.ui.alarmasdiavacio

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ActivityM05AlarmasDiaVacioBinding
import com.agendalarm.app.ui.base.BaseActivity
import com.agendalarm.app.ui.resumenmanana.M07ResumenMananaActivity

/** M-05 · Alarmas del día · estado vacío. Pantalla de arranque de la app; sin compromisos para el día consultado. */
class M05AlarmasDiaVacioActivity : BaseActivity() {

    private val viewModel: AlarmasDiaVaciaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityM05AlarmasDiaVacioBinding>(
            this,
            R.layout.activity_m05_alarmas_dia_vacio,
        )
        binding.lifecycleOwner = this
        setTitle(R.string.m05_titulo)
        aplicarInsets(encabezado = binding.encabezado.root, contenido = binding.contenido)
        viewModel.estado.observe(this) { /* M-05 no tiene contenido variable que dibujar */ }
        configurarCambioDeDia(binding)
    }

    private fun configurarCambioDeDia(binding: ActivityM05AlarmasDiaVacioBinding) {
        // Retroceder no tiene pantalla en el mockup: se consume el toque sin navegar.
        binding.zonaDiaAnterior.setOnClickListener { }
        binding.zonaDiaSiguiente.setOnClickListener {
            startActivity(Intent(this, M07ResumenMananaActivity::class.java))
        }
    }
}
