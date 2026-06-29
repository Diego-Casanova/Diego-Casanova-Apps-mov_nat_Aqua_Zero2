package com.example.agua2.di

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.agua2.AguaApplication
import com.example.agua2.ui.viewmodels.ConsumptionViewModel
import com.example.agua2.ui.viewmodels.DashboardViewModel
import com.example.agua2.ui.viewmodels.LoginViewModel
import com.example.agua2.ui.viewmodels.RegisterViewModel
import com.example.agua2.ui.viewmodels.StatsViewModel

/**
 * Proveedor de fábricas para los ViewModels de la aplicación.
 * Centraliza la creación de los ViewModels inyectando las dependencias desde el AppContainer.
 * Cumple con el lineamiento D de Inyección de Dependencias Manual estructurada.
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Inicializador para el LoginViewModel
        initializer {
            LoginViewModel(
                authRepository = aguaApplication().container.authRepository
            )
        }
        
        // Inicializador para el RegisterViewModel
        initializer {
            RegisterViewModel(
                authRepository = aguaApplication().container.authRepository
            )
        }

        // Inicializador para el DashboardViewModel
        initializer {
            DashboardViewModel(
                waterRepository = aguaApplication().container.waterRepository
            )
        }

        // Inicializador para el ConsumptionViewModel
        initializer {
            ConsumptionViewModel(
                waterRepository = aguaApplication().container.waterRepository
            )
        }

        // Inicializador para el StatsViewModel
        initializer {
            StatsViewModel(
                waterRepository = aguaApplication().container.waterRepository
            )
        }
    }
}

/**
 * Función de extensión para obtener la instancia de AguaApplication a partir de CreationExtras.
 */
fun CreationExtras.aguaApplication(): AguaApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as AguaApplication)
