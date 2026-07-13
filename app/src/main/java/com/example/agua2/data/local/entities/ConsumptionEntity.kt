package com.example.agua2.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.agua2.data.repository.ConsumptionRecord

@Entity(tableName = "consumption_records")
data class ConsumptionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userCedula: String, // Vincular consumo con un usuario
    val amountLiters: Float,
    val date: String
)

fun ConsumptionEntity.asExternalModel() = ConsumptionRecord(
    id = id,
    amountLiters = amountLiters,
    date = date
)

fun ConsumptionRecord.asEntity(userCedula: String) = ConsumptionEntity(
    id = id,
    userCedula = userCedula,
    amountLiters = amountLiters,
    date = date
)
