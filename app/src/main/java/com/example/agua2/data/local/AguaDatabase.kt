package com.example.agua2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.agua2.data.local.dao.ConsumptionDao
import com.example.agua2.data.local.dao.UserDao
import com.example.agua2.data.local.entities.ConsumptionEntity
import com.example.agua2.data.local.entities.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ConsumptionEntity::class, UserEntity::class], version = 3, exportSchema = false)
abstract class AguaDatabase : RoomDatabase() {
    abstract fun consumptionDao(): ConsumptionDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: AguaDatabase? = null

        fun getDatabase(context: Context): AguaDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AguaDatabase::class.java, "agua_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Insertar datos iniciales en un hilo secundario
                            Instance?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val dao = database.consumptionDao()
                                    dao.insertRecord(ConsumptionEntity(userCedula = "1234567890", amountLiters = 15.5f, date = "2024-03-01"))
                                    dao.insertRecord(ConsumptionEntity(userCedula = "1234567890", amountLiters = 22.0f, date = "2024-03-02"))
                                    dao.insertRecord(ConsumptionEntity(userCedula = "1234567890", amountLiters = 18.3f, date = "2024-03-03"))
                                    
                                    val userDao = database.userDao()
                                    userDao.insertUser(UserEntity("1234567890", "1234"))
                                }
                            }
                        }
                    })
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
