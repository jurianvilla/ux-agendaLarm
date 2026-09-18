package com.agendalarm.app.ui.base

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/** Comportamiento común de toda pantalla: edge-to-edge y márgenes por barras del sistema. */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

    /** Llamar tras `setContentView` con la vista raíz de la pantalla. */
    protected fun aplicarInsets(raiz: View) {
        ViewCompat.setOnApplyWindowInsetsListener(raiz) { vista, insets ->
            val barras = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            insets
        }
    }
}
