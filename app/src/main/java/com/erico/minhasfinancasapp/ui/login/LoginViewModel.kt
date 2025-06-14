package com.erico.minhasfinancasapp.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.erico.minhasfinancasapp.data.UserPreferencesRepository
import com.erico.minhasfinancasapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Altere ViewModel para AndroidViewModel e adicione 'application' ao construtor
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    // Cria uma instância do nosso gerenciador de preferências
    private val userPreferencesRepository = UserPreferencesRepository(application)

    fun login() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(
                    email = email.value,
                    senha = password.value
                )
                if (response.isSuccessful) {
                    val token = response.body()?.get("access_token")
                    if (token != null) {
                        // SALVA O TOKEN AQUI!
                        userPreferencesRepository.saveAuthToken(token)
                        Log.d("LoginViewModel", "Login e token salvo com sucesso!")
                        _loginSuccess.value = true
                    }
                } else {
                    Log.e("LoginViewModel", "Falha no login: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Erro de rede ou exceção: ${e.message}")
            }
        }
    }

    fun resetLoginStatus() {
        _loginSuccess.value = false
    }
}