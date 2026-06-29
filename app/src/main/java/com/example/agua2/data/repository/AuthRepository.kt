package com.example.agua2.data.repository

import kotlinx.coroutines.flow.Flow

/**
 * Interfaz que define las operaciones de autenticación.
 * Sigue el principio de abstracción para facilitar las pruebas y el desacoplamiento.
 */
interface AuthRepository {
    // Ejemplo de flujo reactivo para el estado de autenticación
    fun isUserLoggedIn(): Flow<Boolean>
    
    // Función suspendida para login (simulada)
    suspend fun login(cedula: String, password: String): Boolean

    // Función suspendida para registro (simulada)
    suspend fun register(cedula: String, password: String): Boolean
}
