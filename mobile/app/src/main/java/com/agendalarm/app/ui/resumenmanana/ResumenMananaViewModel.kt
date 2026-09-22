package com.agendalarm.app.ui.resumenmanana

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResumenMananaViewModel @JvmOverloads constructor(
    private val repositorio: ResumenMananaRepositorio = ResumenMananaRepositorioMuestra(),
) : ViewModel() {

    val estado: LiveData<ResumenMananaUiState> = MutableLiveData(
        ResumenMananaUiState(repositorio.obtenerCompromisosDeManana())
    )
}
