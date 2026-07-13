package com.example.agua2.data.repository

import com.example.agua2.data.local.dao.UserDao
import com.example.agua2.data.local.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * Implementación real de AuthRepository usando Room.
 */
class OfflineAuthRepository(private val userDao: UserDao) : AuthRepository {

    private val _isLoggedIn = MutableStateFlow(false)
    private val _currentUserCedula = MutableStateFlow<String?>(null)

    override fun isUserLoggedIn(): Flow<Boolean> = _isLoggedIn.asStateFlow()

    override fun getCurrentUserCedula(): Flow<String?> = _currentUserCedula.asStateFlow()

    override suspend fun login(cedula: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val user = userDao.getUserByCedula(cedula)
            val success = user != null && user.password == password
            if (success) {
                _currentUserCedula.value = cedula
                _isLoggedIn.value = true
            }
            success
        }
    }

    override suspend fun register(cedula: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            val existingUser = userDao.getUserByCedula(cedula)
            if (existingUser == null) {
                userDao.insertUser(UserEntity(cedula, password))
                _currentUserCedula.value = cedula
                _isLoggedIn.value = true
                true
            } else {
                false
            }
        }
    }

    override suspend fun logout() {
        _currentUserCedula.value = null
        _isLoggedIn.value = false
    }
}
