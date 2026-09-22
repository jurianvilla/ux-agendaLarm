package com.agendalarm.app.ui.alarmasdiavacio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlarmasDiaVaciaViewModel : ViewModel() {
    val estado: LiveData<AlarmasDiaVaciaUiState> = MutableLiveData(AlarmasDiaVaciaUiState)
}
