// Local: app/src/main/java/com/erico/minhasfinancasapp/ui/splash/SplashScreen.kt

package com.erico.minhasfinancasapp.ui.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.erico.minhasfinancasapp.data.UserPreferencesRepository
import kotlinx.coroutines.flow.first

@Composable
fun SplashScreen(navController: NavController) {
    val context = LocalContext.current
    val userPreferencesRepository = UserPreferencesRepository(context)

    // LaunchedEffect executa o bloco de código de forma segura e assíncrona
    // quando o Composable é exibido pela primeira vez.
    LaunchedEffect(key1 = Unit) {
        // Lê o token do DataStore.
        val authToken = userPreferencesRepository.authToken.first()

        // Decide para qual tela navegar.
        val destination = if (authToken.isNullOrBlank()) {
            "login"
        } else {
            "home"
        }

        // Navega para o destino correto e remove a SplashScreen da pilha de navegação,
        // para que o usuário não possa voltar para ela.
        navController.navigate(destination) {
            popUpTo("splash") {
                inclusive = true
            }
        }
    }

    // Enquanto a lógica acima executa, a UI mostra apenas um indicador de progresso.
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}