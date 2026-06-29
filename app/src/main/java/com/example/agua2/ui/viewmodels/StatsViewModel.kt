package com.example.agua2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agua2.data.repository.AguaRepository
import com.example.agua2.data.repository.ConsumptionRecord
import com.example.agua2.data.repository.ConsumptionSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

sealed class StatsUiState {
    data object Loading : StatsUiState()

    data class Success(
        val consumptionData: List<Float>,
        val costData: List<Float>,
        val labels: List<String>,
        val monthlyValue: Float,
        val totalValue: Float
    ) : StatsUiState()

    data class Error(
        val message: String
    ) : StatsUiState()
}

class StatsViewModel(
    private val waterRepository: AguaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<StatsUiState>(StatsUiState.Loading)
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            combine(
                waterRepository.getConsumptionHistory(),
                waterRepository.getConsumptionSummary()
            ) { history, summary ->
                buildSuccessState(history = history, summary = summary)
            }
                .onStart { _uiState.value = StatsUiState.Loading }
                .catch {
                    _uiState.value = StatsUiState.Error(
                        message = "No se pudieron cargar las estadísticas"
                    )
                }
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    private fun buildSuccessState(
        history: List<ConsumptionRecord>,
        summary: ConsumptionSummary
    ): StatsUiState.Success {
        val pricePerUnit = summary.pricePerUnit.extractNumericValue()
        val monthlyValue = summary.monthlyConsumption.extractNumericValue()
        val totalValue = summary.totalAccumulated.extractNumericValue().coerceAtLeast(1f)

        val consumptionData = history.map { it.amountLiters }
        val costData = history.map { record -> record.amountLiters * pricePerUnit }
        val labels = history.map { it.date }

        return StatsUiState.Success(
            consumptionData = consumptionData,
            costData = costData,
            labels = labels,
            monthlyValue = monthlyValue,
            totalValue = totalValue
        )
    }

    private fun String.extractNumericValue(): Float {
        val numericText = replace("[^0-9.,]".toRegex(), "").replace(',', '.')
        return numericText.toFloatOrNull() ?: 0f
    }
}