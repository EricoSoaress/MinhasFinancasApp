// Local: app/src/main/java/com/erico/minhasfinancasapp/ui/login/LoginViewModel.kt

package com.erico.minhasfinancasapp.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.erico.minhasfinancasapp.data.UserPreferencesRepository
import com.erico.minhasfinancasapp.data.remote.ApiService
import com.erico.minhasfinancasapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: ApiService = RetrofitInstance.getRetrofitInstance(application).create(ApiService::class.java)
    private val userPreferencesRepository = UserPreferencesRepository(application)

    // Estados para os campos do formulário
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    // Novos estados para controlar a UI
    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun login() {
        // Ignora cliques múltiplos se já estiver carregando
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null // Limpa erros antigos
            try {
                val response = apiService.login(
                    email = email.value,
                    senha = password.value
                )
                if (response.isSuccessful) {
                    val token = response.body()?.get("access_token")
                    if (token != null) {
                        userPreferencesRepository.saveAuthToken(token)
                        _loginSuccess.value = true
                    }
                } else {
                    // Define a mensagem de erro para ser exibida na tela
                    _errorMessage.value = "E-mail ou senha incorretos."
                    Log.e("LoginViewModel", "Falha no login: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _errorMessage.value = "Falha na conexão. Verifique a rede."
                Log.e("LoginViewModel", "Erro de rede ou exceção: ${e.message}")
            } finally {
                // Garante que o estado de carregamento seja desativado
                _isLoading.value = false
            }
        }
    }

    fun resetLoginStatus() {
        _loginSuccess.value = false
    }
}