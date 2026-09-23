package com.agendalarm.app.ui.alarmacumplida

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ActivityM12AlarmaCumplidaBinding
import com.agendalarm.app.ui.base.BaseActivity

/** M-12 · Alarma cumplida. */
class M12AlarmaCumplidaActivity : BaseActivity() {

    private val viewModel: AlarmaCumplidaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityM12AlarmaCumplidaBinding>(
            this,
            R.layout.activity_m12_alarma_cumplida,
        )
        binding.lifecycleOwner = this
        setTitle(R.string.m12_titulo)
        aplicarInsets(encabezado = binding.encabezado.root, contenido = binding.contenido)
        viewModel.estado.observe(this) { estado -> binding.estado = estado }
    }
}
