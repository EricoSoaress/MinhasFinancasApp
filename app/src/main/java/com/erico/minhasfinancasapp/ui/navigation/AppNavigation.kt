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

// Uma constante privada para representar nosso estado de carregamento.
private const val STATE_LOADING = "state_loading"

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val userPreferencesRepository = UserPreferencesRepository(context)

    // Usamos nossa constante como o valor inicial.
    // O 'authToken' agora será a nossa constante, o token real, ou nulo/vazio.
    val authToken by userPreferencesRepository.authToken.collectAsState(initial = STATE_LOADING)

    // A lógica agora verifica explicitamente pelo estado de carregamento.
    if (authToken == STATE_LOADING) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Quando o carregamento termina, o authToken é diferente da nossa constante.
        // Agora, podemos verificar se ele é válido (não nulo e não em branco).
        val startDestination = if (authToken?.isNotBlank() == true) {
            "home"
        } else {
            "login"
        }

        NavHost(navController = navController, startDestination = startDestination) {
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("home") {
                HomeScreen(navController = navController)
            }
        }
    }
}