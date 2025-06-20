
package com.erico.minhasfinancasapp.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.erico.minhasfinancasapp.ui.home.HomeScreen
import com.erico.minhasfinancasapp.ui.login.LoginScreen
import com.erico.minhasfinancasapp.ui.register.RegisterScreen
import com.erico.minhasfinancasapp.ui.splash.SplashScreen // Importe a nova tela
import com.erico.minhasfinancasapp.ui.transaction.TransactionScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = "splash") {


        composable("splash") {
            SplashScreen(navController = navController)
        }

        composable("login") {
            LoginScreen(navController = navController)
        }
        composable("register") {
            RegisterScreen(navController = navController)
        }
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable(
            route = "transaction_screen/{transactionId}",
            arguments = listOf(navArgument("transactionId") { type = NavType.IntType })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getInt("transactionId") ?: -1
            TransactionScreen(navController = navController, transactionId = transactionId)
        }
    }
}