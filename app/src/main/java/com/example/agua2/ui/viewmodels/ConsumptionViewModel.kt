package com.example.agua2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agua2.data.repository.AguaRepository
import com.example.agua2.data.repository.ConsumptionSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class ConsumptionUiState {
    data object Loading : ConsumptionUiState()

    data class Success(
        val summary: ConsumptionSummary
    ) : ConsumptionUiState()

    data class Error(
        val message: String
    ) : ConsumptionUiState()
}

class ConsumptionViewModel(
    private val waterRepository: AguaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ConsumptionUiState>(ConsumptionUiState.Loading)
    val uiState: StateFlow<ConsumptionUiState> = _uiState.asStateFlow()

    init {
        loadConsumptionSummary()
    }

    private fun loadConsumptionSummary() {
        viewModelScope.launch {
            waterRepository.getConsumptionSummary()
                .onStart { _uiState.value = ConsumptionUiState.Loading }
                .catch {
                    _uiState.value = ConsumptionUiState.Error(
                        message = "No se pudo cargar el resumen de consumo"
                    )
                }
                .collect { summary ->
                    _uiState.value = ConsumptionUiState.Success(summary = summary)
                }
        }
    }
}