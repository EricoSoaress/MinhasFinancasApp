package com.erico.minhasfinancasapp.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.erico.minhasfinancasapp.data.UserPreferencesRepository
import com.erico.minhasfinancasapp.ui.home.HomeScreen
import com.erico.minhasfinancasapp.ui.login.LoginScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreferencesRepository = UserPreferencesRepository(context)

    // Observa o token salvo no DataStore. O valor inicial é 'null' enquanto carrega.
    val authToken by userPreferencesRepository.authToken.collectAsState(initial = null)

    // A tela de carregamento só será exibida enquanto authToken for nulo
    if (authToken == null) {
        // Por enquanto, usamos um simples indicador de progresso centralizado.
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // QUANDO o authToken for carregado (mesmo que seja uma string vazia),
        // decidimos a rota inicial e mostramos o NavHost.
        val startDestination = if (authToken!!.isNotBlank()) {
            "home" // Se tem token, começa na home
        } else {
            "login" // Se não tem, começa no login
        }

        NavHost(navController = navController, startDestination = startDestination) {
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("home") {
                HomeScreen()
            }
        }
    }
}