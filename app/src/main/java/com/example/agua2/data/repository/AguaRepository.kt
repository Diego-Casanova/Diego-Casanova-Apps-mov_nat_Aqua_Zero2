package com.example.agua2.data.repository

import kotlinx.coroutines.flow.Flow

/**
 * Interfaz para la gestión de datos de consumo de agua.
 * Define flujos reactivos para cumplir con los lineamientos MAD.
 */
interface AguaRepository {
    fun getConsumptionHistory(): Flow<List<ConsumptionRecord>>
    fun getConsumptionSummary(): Flow<ConsumptionSummary>
}

data class ConsumptionSummary(
    val currentConsumption: String,
    val monthlyConsumption: String,
    val totalAccumulated: String,
    val pricePerUnit: String,
    val currentCost: String,
    val pendingDebt: String,
    val hasDebt: Boolean = false
)

data class ConsumptionRecord(
    val id: Int,
    val amountLiters: Float,
    val date: String
)
