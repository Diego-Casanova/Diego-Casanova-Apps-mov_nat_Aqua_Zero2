package com.example.agua2.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.agua2.data.repository.AguaRepository
import com.example.agua2.data.repository.AuthRepository
import com.example.agua2.data.repository.ConsumptionRecord
import com.example.agua2.data.repository.ConsumptionSummary
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class DashboardUiState {
    data object Loading : DashboardUiState()

    data class Success(
        val history: List<ConsumptionRecord>,
        val summary: ConsumptionSummary
    ) : DashboardUiState()

    data class Error(
        val message: String
    ) : DashboardUiState()
}

class DashboardViewModel(
    private val waterRepository: AguaRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            combine(
                waterRepository.getConsumptionHistory(),
                waterRepository.getConsumptionSummary()
            ) { history, summary ->
                DashboardUiState.Success(history, summary)
            }
            .catch {
                _uiState.value = DashboardUiState.Error(
                    message = "No se pudo cargar los datos de consumo"
                )
            }
            .collect { state ->
                _uiState.value = state
            }
        }
    }

    /**
     * Función para simular la inserción de un nuevo consumo y ver la reactividad.
     */
    fun addRandomConsumption() {
        viewModelScope.launch {
            val randomAmount = (10..50).random().toFloat() / 10f
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.format(Date())
            
            // El ID es 0 porque Room lo autogenera
            waterRepository.insertConsumptionRecord(
                ConsumptionRecord(id = 0, amountLiters = randomAmount, date = date)
            )
        }
    }

    /**
     * Elimina un registro por su ID.
     */
    fun deleteConsumption(id: Int) {
        viewModelScope.launch {
            waterRepository.deleteConsumptionRecord(id)
        }
    }

    /**
     * Cierra la sesión del usuario.
     */
    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
