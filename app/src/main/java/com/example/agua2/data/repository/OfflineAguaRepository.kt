package com.example.agua2.data.repository

import com.example.agua2.data.local.dao.ConsumptionDao
import com.example.agua2.data.local.entities.asEntity
import com.example.agua2.data.local.entities.asExternalModel
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * Implementación real del repositorio que utiliza Room como fuente de verdad.
 * Cumple con SSOT, Main-Safety y Flujos Reactivos.
 */
class OfflineAguaRepository(
    private val consumptionDao: ConsumptionDao,
    private val authRepository: AuthRepository
) : AguaRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getCurrentUserRecords(): Flow<List<com.example.agua2.data.local.entities.ConsumptionEntity>> {
        return authRepository.getCurrentUserCedula().flatMapLatest { cedula ->
            if (cedula != null) {
                consumptionDao.getRecordsByUser(cedula)
            } else {
                emptyFlow()
            }
        }
    }

    override fun getConsumptionHistory(): Flow<List<ConsumptionRecord>> {
        return getCurrentUserRecords()
            .map { entities ->
                entities.map { it.asExternalModel() }
            }
            .flowOn(Dispatchers.IO)
    }

    override fun getConsumptionSummary(): Flow<ConsumptionSummary> = getCurrentUserRecords().map { records ->
        val totalLiters = records.sumOf { it.amountLiters.toDouble() }
        val pricePerLiters = 0.78
        val currentCost = totalLiters * pricePerLiters
        
        ConsumptionSummary(
            currentConsumption = "${String.format(Locale.US, "%.1f", if(records.isNotEmpty()) records.last().amountLiters else 0f)} m³",
            monthlyConsumption = "${String.format(Locale.US, "%.1f", totalLiters)} m³",
            totalAccumulated = "${String.format(Locale.US, "%.1f", totalLiters)} m³",
            pricePerUnit = "$$pricePerLiters",
            currentCost = "$${String.format(Locale.US, "%.2f", currentCost)}",
            pendingDebt = if (currentCost > 50) "$15.00" else "$0.00",
            hasDebt = currentCost > 50
        )
    }.flowOn(Dispatchers.IO)

    override suspend fun insertConsumptionRecord(record: ConsumptionRecord) {
        val cedula = authRepository.getCurrentUserCedula().first()
        if (cedula != null) {
            consumptionDao.insertRecord(record.asEntity(cedula))
        }
    }

    override suspend fun deleteConsumptionRecord(id: Int) {
        consumptionDao.deleteRecordById(id)
    }
}
