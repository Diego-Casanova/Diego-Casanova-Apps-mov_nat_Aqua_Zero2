package com.example.agua2.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agua2.ui.screens.ConsumptionScreen
import com.example.agua2.ui.screens.AccessScreen
import com.example.agua2.ui.screens.ConsumptionScreen
import com.example.agua2.ui.screens.DashboardScreen
import com.example.agua2.ui.screens.LoginScreen
import com.example.agua2.ui.screens.RegisterScreen
import com.example.agua2.ui.screens.SplashScreen
import com.example.agua2.ui.screens.StatsScreen

/**
 * Orquestador de navegación de la aplicación.
 * Define las rutas y asegura que los ViewModels se instancien correctamente
 * en el nivel de pantalla (Screen level).
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

        // Pantalla de Selección (Login o Register)
        composable("access") {
            AccessScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        // Pantalla de Login
        composable("login") {
            LoginScreen(
                onNavigateBack = { navController.popBackStack() },
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
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("access") { inclusive = true }
                    }
                }
            )
        }
// ... rest of the composables

        // Pantalla Principal - Dashboard
        composable("dashboard") {
            DashboardScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                },
                onNavigateToConsumption = { navController.navigate("consumption") }
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
