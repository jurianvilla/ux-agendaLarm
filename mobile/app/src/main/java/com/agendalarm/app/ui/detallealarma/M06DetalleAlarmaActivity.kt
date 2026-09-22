package com.agendalarm.app.ui.detallealarma

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ActivityM06DetalleAlarmaBinding
import com.agendalarm.app.ui.base.BaseActivity
import com.agendalarm.app.ui.metodoconfirmacion.M09MetodoConfirmacionActivity

/**
 * M-06 · Detalle de la alarma. Información completa del compromiso que origina la alarma (lugar, hora de salida
 * estimada y margen calculado) con las acciones de editar y eliminar. Se abre desde las tarjetas de M-04;
 * Editar Alarma abre M-09 (elegir método de confirmación).
 */
class M06DetalleAlarmaActivity : BaseActivity() {

    private val viewModel: DetalleAlarmaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityM06DetalleAlarmaBinding>(
            this,
            R.layout.activity_m06_detalle_alarma,
        )
        binding.lifecycleOwner = this
        setTitle(R.string.m06_titulo)
        aplicarInsets(encabezado = binding.encabezado.root, contenido = binding.contenido)
        observarDetalle(binding)
        binding.botonEditar.setOnClickListener {
            startActivity(Intent(this, M09MetodoConfirmacionActivity::class.java))
        }
    }

    private fun observarDetalle(binding: ActivityM06DetalleAlarmaBinding) {
        viewModel.estado.observe(this) { estado ->
            when (estado) {
                is DetalleAlarmaUiState.Alarma -> binding.detalle = estado.detalle
                DetalleAlarmaUiState.NoEncontrada -> finish()
            }
        }
    }

    companion object {
        /** Intent que abre el detalle de la alarma [alarmaId]; el resto de pantallas no conoce la clave del extra. */
        fun intent(contexto: Context, alarmaId: Long): Intent =
            Intent(contexto, M06DetalleAlarmaActivity::class.java)
                .putExtra(DetalleAlarmaViewModel.ARGUMENTO_ALARMA_ID, alarmaId)
    }
}
