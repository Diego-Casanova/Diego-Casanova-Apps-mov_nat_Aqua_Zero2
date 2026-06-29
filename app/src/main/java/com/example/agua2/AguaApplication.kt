package com.example.agua2

import android.app.Application
import com.example.agua2.di.AppContainer
import com.example.agua2.di.AppDataContainer

/**
 * Clase Application personalizada para el proyecto.
 * Aquí inicializamos el contenedor de dependencias (Manual DI) que estará disponible
 * durante todo el ciclo de vida de la aplicación.
 */
class AguaApplication : Application() {
    
    /**
     * Instancia del contenedor de dependencias que será usada por los ViewModels.
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        // Inicializamos el contenedor de datos/dependencias
        container = AppDataContainer()
    }
}
