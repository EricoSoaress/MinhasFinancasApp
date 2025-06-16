
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

    LaunchedEffect(key1 = Unit) {

        val authToken = userPreferencesRepository.authToken.first()


        val destination = if (authToken.isNullOrBlank()) {
            "login"
        } else {
            "home"
        }

        navController.navigate(destination) {
            popUpTo("splash") {
                inclusive = true
            }
        }
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}