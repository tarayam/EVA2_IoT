package com.example.aplicacionmovil.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionmovil.data.SensorRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SensorViewModel(
    private val repository: SensorRepository = SensorRepository()
) : ViewModel() {

    var uiState = androidx.compose.runtime.mutableStateOf(SensorUiState())
        private set

    init {
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (isActive) {        // Mientras el ViewModel exista
                loadSensorData()
                delay(2000L)          // Cada 2 segundos
            }
        }
    }

    private suspend fun loadSensorData() {
        // Estado: cargando (opcional: si quieres que parpadee cada vez que carga)
        // Si prefieres que no parpadee, quita isLoading = true aquí 
        // o úsalo solo la primera vez.
        /*
        uiState.value = uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        */

        val result = repository.getSensorData()

        uiState.value = result.fold(
            onSuccess = { dto ->
                uiState.value.copy(
                    isLoading = false,
                    temperature = dto.temperature,
                    humidity = dto.humidity,
                    lastUpdate = dto.timestamp,
                    errorMessage = null
                )
            },
            onFailure = { e ->
                uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error al obtener datos: ${e.message}"
                )
            }
        )
    }
}
