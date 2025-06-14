package com.erico.minhasfinancasapp.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erico.minhasfinancasapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    // Estado privado para controlar o sucesso do login
    private val _loginSuccess = MutableStateFlow(false)
    // Estado público que a UI pode observar
    val loginSuccess = _loginSuccess.asStateFlow()

    fun login() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.login(
                    email = email.value,
                    senha = password.value
                )
                if (response.isSuccessful) {
                    val token = response.body()?.get("access_token")
                    Log.d("LoginViewModel", "Login com sucesso! Token: $token")
                    // AVISA A UI QUE O LOGIN DEU CERTO!
                    _loginSuccess.value = true
                } else {
                    Log.e("LoginViewModel", "Falha no login: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Erro de rede ou exceção: ${e.message}")
            }
        }
    }

    // Função para resetar o estado após a navegação ter ocorrido
    fun resetLoginStatus() {
        _loginSuccess.value = false
    }
}