package com.agendalarm.app.ui.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.agendalarm.app.R
import com.agendalarm.app.databinding.ActivityMainBinding
import com.agendalarm.app.ui.base.BaseActivity

/** Pantalla vacía de andamio: se reemplaza por la primera pantalla real. */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        aplicarInsets(binding.raiz)
    }
}
