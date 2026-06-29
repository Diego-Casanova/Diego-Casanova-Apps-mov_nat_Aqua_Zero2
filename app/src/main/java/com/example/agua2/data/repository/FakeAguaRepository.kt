package com.example.agua2.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Implementación simulada para el consumo de agua.
 * Aplica el lineamiento de usar el constructor 'flow' y Dispatchers.IO.
 */
class FakeAguaRepository : AguaRepository {

    override fun getConsumptionHistory(): Flow<List<ConsumptionRecord>> = flow {
        // Simulamos una carga inicial de datos
        delay(2000)
        
        val mockData = listOf(
            ConsumptionRecord(1, 2.5f, "2023-10-20"),
            ConsumptionRecord(2, 1.8f, "2023-10-21"),
            ConsumptionRecord(3, 3.0f, "2023-10-22")
        )
        
        // Emitimos los datos al flujo reactivo
        emit(mockData)
        
    }.flowOn(Dispatchers.IO)

    override fun getConsumptionSummary(): Flow<ConsumptionSummary> = flow {
        delay(1500)
        emit(
            ConsumptionSummary(
                currentConsumption = "18 m³",
                monthlyConsumption = "24 m³",
                totalAccumulated = "540 m³",
                pricePerUnit = "$0.78",
                currentCost = "$14.04",
                pendingDebt = "$8.50",
                hasDebt = true
            )
        )
    }.flowOn(Dispatchers.IO)
}
