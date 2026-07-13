package com.example.agua2.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.agua2.data.local.entities.ConsumptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumptionDao {
    @Query("SELECT * FROM consumption_records WHERE userCedula = :cedula ORDER BY date DESC")
    fun getRecordsByUser(cedula: String): Flow<List<ConsumptionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: ConsumptionEntity)

    @Query("DELETE FROM consumption_records WHERE id = :id")
    suspend fun deleteRecordById(id: Int)

    @Query("DELETE FROM consumption_records")
    suspend fun deleteAll()
}
