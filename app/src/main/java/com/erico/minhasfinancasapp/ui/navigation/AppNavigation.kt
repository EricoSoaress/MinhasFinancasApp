// ui/navigation/AppNavigation.kt
package com.erico.minhasfinancasapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.erico.minhasfinancasapp.ui.login.LoginScreen
import com.erico.minhasfinancasapp.ui.home.HomeScreen // Teremos um erro aqui, já vamos criar

@Composable
fun AppNavigation() {
    val navController = rememberNavController() // O controlador de navegação

    NavHost(navController = navController, startDestination = "login") {
        // Define a rota "login" que mostra a nossa LoginScreen
        composable("login") {
            LoginScreen(navController = navController)
        }
        // Define a rota "home" para a tela principal
        composable("home") {
            HomeScreen()
        }
    }
}