package com.example.agua2.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Access : Screen("access")
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Consumption : Screen("consumption")
    object Stats : Screen("stats")
}
