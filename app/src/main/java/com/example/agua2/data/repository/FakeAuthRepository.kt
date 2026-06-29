package com.example.agua2.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * Implementación simulada del repositorio de autenticación.
 * Cumple con el lineamiento de Fuente Única de Verdad (SSOT) usando StateFlow.
 */
class FakeAuthRepository : AuthRepository {

    // Fuente única de verdad para el estado de autenticación en memoria
    private val _isLoggedIn = MutableStateFlow(false)
    
    override fun isUserLoggedIn(): Flow<Boolean> = _isLoggedIn.asStateFlow()

    /**
     * Simula un proceso de login con retardo asíncrono.
     * Se delega explícitamente a Dispatchers.IO para no bloquear la UI.
     */
    override suspend fun login(cedula: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            // Simulamos latencia de red (2 segundos)
            delay(2000)
            
            // Lógica simple de validación simulada
            val success = cedula == "1234567890" && password == "1234"
            
            if (success) {
                _isLoggedIn.value = true
            }
            
            success
        }
    }

    /**
     * Simula un proceso de registro con retardo asíncrono.
     * Se delega explícitamente a Dispatchers.IO.
     */
    override suspend fun register(cedula: String, password: String): Boolean {
        return withContext(Dispatchers.IO) {
            // Simulamos latencia de red
            delay(2000)
            
            // Lógica simple: registro exitoso si la cédula tiene 10 dígitos
            val success = cedula.length == 10
            
            if (success) {
                _isLoggedIn.value = true
            }
            
            success
        }
    }
}
