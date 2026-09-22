package com.agendalarm.app.ui.alarmasdia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ActivityM04AlarmasDelDiaBinding
import com.agendalarm.app.ui.animacion.usarAnimacionesAgendaLarm
import com.agendalarm.app.ui.base.BaseActivity
import com.agendalarm.app.ui.detallealarma.M06DetalleAlarmaActivity

/**
 * M-04 · Alarmas del día. Pantalla principal de consulta diaria: fecha y número de compromisos, y una tarjeta
 * por alarma con su interruptor; tocar la tarjeta abre su detalle (M-06). M-05 (el estado vacío) es una pantalla
 * propia, no un estado de esta Activity.
 */
class M04AlarmasDelDiaActivity : BaseActivity() {

    private val viewModel: AlarmasDelDiaViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityM04AlarmasDelDiaBinding>(
            this,
            R.layout.activity_m04_alarmas_del_dia,
        )
        binding.lifecycleOwner = this
        setTitle(R.string.m04_titulo)
        aplicarInsets(encabezado = binding.encabezado.root, contenido = binding.listaAlarmas)
        activarLista(binding)
    }

    private fun activarLista(binding: ActivityM04AlarmasDelDiaBinding) {
        val adaptador = AlarmasAdapter(alAlternar = viewModel::alternarAlarma, alPulsar = ::abrirDetalle)
        binding.listaAlarmas.adapter = adaptador
        binding.listaAlarmas.usarAnimacionesAgendaLarm()
        viewModel.estado.observe(this) { estado -> adaptador.submitList(estado.elementos()) }
    }

    private fun abrirDetalle(alarmaId: Long) {
        startActivity(M06DetalleAlarmaActivity.intent(this, alarmaId))
    }
}
