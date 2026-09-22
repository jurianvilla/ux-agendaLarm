package com.agendalarm.app.ui.levantateapagar

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ActivityM11LevantateApagarBinding
import com.agendalarm.app.ui.base.BaseActivity

/**
 * M-11 · Levántate para apagar la alarma. Confirmación del despertar mediante un movimiento sostenido (estar de pie
 * unos segundos), que se representa con una barra de progreso y su porcentaje.
 */
class M11LevantateApagarActivity : BaseActivity() {

    private val viewModel: LevantateApagarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityM11LevantateApagarBinding>(
            this,
            R.layout.activity_m11_levantate_apagar,
        )
        binding.lifecycleOwner = this
        setTitle(R.string.m11_titulo)
        aplicarInsets(encabezado = binding.encabezado.root, contenido = binding.contenido)
        observarProgreso(binding)
    }

    private fun observarProgreso(binding: ActivityM11LevantateApagarBinding) {
        viewModel.estado.observe(this) { estado -> binding.estado = estado }
    }
}
