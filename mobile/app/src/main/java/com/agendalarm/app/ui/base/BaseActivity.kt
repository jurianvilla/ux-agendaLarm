package com.agendalarm.app.ui.base

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

/** Comportamiento común de toda pantalla: edge-to-edge y márgenes por barras del sistema. */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
    }

    /**
     * Pantalla estándar: todo el contenido queda entre las barras del sistema. Llamar tras `setContentView`.
     * Los insets se suman al relleno que ya traiga el layout (no lo reemplazan).
     */
    protected fun aplicarInsets(raiz: View) {
        raiz.sumarInsets(arriba = true, abajo = true)
    }

    /**
     * Pantalla con encabezado blanco a sangre (los 15 mockups S6): la franja llega hasta el borde superior,
     * así que el [encabezado] absorbe la barra de estado y el [contenido] absorbe la barra de navegación
     * (por eso puede desplazarse por debajo de ella). Los dos suman el inset a su relleno original.
     */
    protected fun aplicarInsets(encabezado: View, contenido: View) {
        encabezado.sumarInsets(arriba = true, abajo = false)
        contenido.sumarInsets(arriba = false, abajo = true)
    }

    private fun View.sumarInsets(arriba: Boolean, abajo: Boolean) {
        val inicial = Rect(paddingLeft, paddingTop, paddingRight, paddingBottom)
        ViewCompat.setOnApplyWindowInsetsListener(this) { vista, insets ->
            val barras = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            vista.updatePadding(
                left = inicial.left + barras.left,
                top = inicial.top + if (arriba) barras.top else 0,
                right = inicial.right + barras.right,
                bottom = inicial.bottom + if (abajo) barras.bottom else 0,
            )
            insets
        }
    }
}
