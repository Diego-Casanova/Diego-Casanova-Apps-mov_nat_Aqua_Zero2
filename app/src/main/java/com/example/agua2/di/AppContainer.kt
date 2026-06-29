package com.example.agua2.di

import com.example.agua2.data.repository.AuthRepository
import com.example.agua2.data.repository.FakeAuthRepository
import com.example.agua2.data.repository.FakeWaterRepository
import com.example.agua2.data.repository.WaterRepository

/**
 * Contenedor de dependencias para la Inyección de Dependencias Manual.
 * Este contenedor vive a nivel de Aplicación y provee las instancias únicas (Singletons)
 * de los repositorios a los ViewModels a través de las fábricas.
 */
interface AppContainer {
    val authRepository: AuthRepository
    val waterRepository: WaterRepository
}

/**
 * Implementación real del contenedor que instancia los repositorios.
 * Aquí es donde se decide qué implementación concreta usar (Fake o Real).
 */
class AppDataContainer : AppContainer {
    /**
     * Implementación única del repositorio de autenticación.
     */
    override val authRepository: AuthRepository by lazy {
        FakeAuthRepository()
    }

    /**
     * Implementación única del repositorio de agua.
     * Mantiene la consistencia de Fuente Única de Verdad (SSOT).
     */
    override val waterRepository: WaterRepository by lazy {
        FakeWaterRepository()
    }
}
