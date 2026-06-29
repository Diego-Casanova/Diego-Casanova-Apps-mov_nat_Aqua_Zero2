package com.example.agua2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agua2.data.repository.AguaRepository
import com.example.agua2.data.repository.ConsumptionRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class DashboardUiState {
    data object Loading : DashboardUiState()

    data class Success(
        val history: List<ConsumptionRecord>
    ) : DashboardUiState()

    data class Error(
        val message: String
    ) : DashboardUiState()
}

class DashboardViewModel(
    private val waterRepository: AguaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            waterRepository.getConsumptionHistory()
                .onStart { _uiState.value = DashboardUiState.Loading }
                .catch {
                    _uiState.value = DashboardUiState.Error(
                        message = "No se pudo cargar el historial de consumo"
                    )
                }
                .collect { history ->
                    _uiState.value = DashboardUiState.Success(history = history)
                }
        }
    }
}