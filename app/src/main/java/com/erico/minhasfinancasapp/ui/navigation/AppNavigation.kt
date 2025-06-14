package com.erico.minhasfinancasapp.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.erico.minhasfinancasapp.data.UserPreferencesRepository
import com.erico.minhasfinancasapp.ui.home.HomeScreen
import com.erico.minhasfinancasapp.ui.login.LoginScreen
import com.erico.minhasfinancasapp.ui.transaction.TransactionScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    // ... (código existente para decidir a rota inicial) ...

    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        // ROTA INTELIGENTE para Adicionar/Editar
        composable(
            route = "transaction_screen/{transactionId}",
            arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: -1
            TransactionScreen(navController = navController, transactionId = transactionId)
        }
    }
}