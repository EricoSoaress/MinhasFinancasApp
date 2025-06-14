// LoginViewModel.kt
package com.erico.minhasfinancasapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erico.minhasfinancasapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // 'StateFlow' é a forma moderna de guardar o estado que a UI pode observar.
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun login() {
        // 'viewModelScope.launch' inicia uma coroutine que é segura para a UI.
        viewModelScope.launch {
            try {
                // Certifique-se de que o backend está rodando!
                val response = RetrofitInstance.api.login(
                    email = email.value,
                    senha = password.value
                )

                if (response.isSuccessful) {
                    val token = response.body()?.get("access_token")
                    Log.d("LoginViewModel", "Login com sucesso! Token: $token")
                    // Próximo passo seria salvar o token e navegar para outra tela.
                } else {
                    Log.e("LoginViewModel", "Falha no login: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Erro de rede ou exceção: ${e.message}")
            }
        }
    }
}