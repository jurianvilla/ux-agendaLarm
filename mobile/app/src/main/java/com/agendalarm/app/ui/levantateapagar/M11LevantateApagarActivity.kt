package com.agendalarm.app.ui.levantateapagar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ActivityM11LevantateApagarBinding
import com.agendalarm.app.ui.alarmacumplida.M12AlarmaCumplidaActivity
import com.agendalarm.app.ui.base.BaseActivity

/**
 * M-11 · Levántate para apagar la alarma. Confirmación del despertar mediante un movimiento sostenido (estar de pie
 * unos segundos), que se representa con una barra de progreso y su porcentaje.
 * Demo: la barra se completa sola y, al llegar a 100 %, abre M-12.
 */
class M11LevantateApagarActivity : BaseActivity() {

    private val viewModel: LevantateApagarViewModel by viewModels()
    private val temporizador = Handler(Looper.getMainLooper())
    private val intervalo by lazy {
        resources.getInteger(R.integer.duracion_demo_levantate).toLong() /
            (LevantateApagarUiState.RANGO_PROGRESO.last - LevantateApagarViewModel.PROGRESO_MUESTRA)
    }

    private val avanzar = object : Runnable {
        override fun run() {
            viewModel.avanzar()
            if (viewModel.estado.value?.completo == true) abrirAlarmaCumplida()
            else temporizador.postDelayed(this, intervalo)
        }
    }

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

    override fun onStart() {
        super.onStart()
        temporizador.postDelayed(avanzar, intervalo)
    }

    override fun onStop() {
        temporizador.removeCallbacks(avanzar)
        super.onStop()
    }

    private fun observarProgreso(binding: ActivityM11LevantateApagarBinding) {
        viewModel.estado.observe(this) { estado -> binding.estado = estado }
    }

    private fun abrirAlarmaCumplida() {
        startActivity(Intent(this, M12AlarmaCumplidaActivity::class.java))
        finish()
    }
}
