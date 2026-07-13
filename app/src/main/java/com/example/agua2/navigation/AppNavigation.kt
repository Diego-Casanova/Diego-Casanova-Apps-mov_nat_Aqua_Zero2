package com.example.agua2.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agua2.di.AppViewModelProvider
import com.example.agua2.ui.screens.*
import com.example.agua2.ui.viewmodels.DashboardViewModel

/**
 * Orquestador de navegación de la aplicación.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        // Pantalla de Bienvenida
        composable("splash") {
            SplashScreen(
                onNavigateToAccess = {
                    navController.navigate("access") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Selección (Inicio o Registro)
        composable("access") {
            AccessScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onNavigateBack = { 
                    // Intentamos volver atrás, si no hay historial vamos a access
                    if (!navController.popBackStack()) {
                        navController.navigate("access")
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("access") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Registro
        composable("register") {
            RegisterScreen(
                onNavigateBack = { 
                    if (!navController.popBackStack()) {
                        navController.navigate("access")
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("access") { inclusive = true }
                    }
                }
            )
        }

        // Pantalla Principal - Dashboard
        composable("dashboard") {
            val dashboardViewModel: DashboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
            DashboardScreen(
                onLogout = {
                    dashboardViewModel.logout()
                    // Al cerrar sesión, limpiamos el historial y vamos a la pantalla de selección
                    navController.navigate("access") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onNavigateToConsumption = { navController.navigate("consumption") },
                viewModel = dashboardViewModel
            )
        }

        // Pantalla de Consumo Detallado
        composable("consumption") {
            ConsumptionScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToStats = { navController.navigate("stats") }
            )
        }

        // Pantalla de Estadísticas
        composable("stats") {
            StatsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
