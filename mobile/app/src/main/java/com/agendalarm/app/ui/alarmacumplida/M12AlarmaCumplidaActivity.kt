package com.agendalarm.app.ui.alarmacumplida

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ActivityM12AlarmaCumplidaBinding
import com.agendalarm.app.ui.base.BaseActivity
import com.agendalarm.app.ui.metodoconfirmacion.M09MetodoConfirmacionActivity

/** M-12 · Alarma cumplida. Demo: tras un segundo vuelve a M-09. */
class M12AlarmaCumplidaActivity : BaseActivity() {

    private val viewModel: AlarmaCumplidaViewModel by viewModels()
    private val temporizador = Handler(Looper.getMainLooper())
    private val volver = Runnable { volverAMetodoConfirmacion() }

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

    override fun onStart() {
        super.onStart()
        temporizador.postDelayed(volver, resources.getInteger(R.integer.duracion_demo_cumplida).toLong())
    }

    override fun onStop() {
        temporizador.removeCallbacks(volver)
        super.onStop()
    }

    private fun volverAMetodoConfirmacion() {
        startActivity(
            Intent(this, M09MetodoConfirmacionActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        )
        finish()
    }
}
