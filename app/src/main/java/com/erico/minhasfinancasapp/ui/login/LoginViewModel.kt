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

    // AQUI ESTÁ A MUDANÇA: Instanciamos o ApiService usando a nova função
    private val apiService: ApiService = RetrofitInstance.getRetrofitInstance(application).create(ApiService::class.java)

    private val userPreferencesRepository = UserPreferencesRepository(application)

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()

    fun login() {
        viewModelScope.launch {
            try {
                // E AQUI USAMOS A NOSSA VARIÁVEL local 'apiService'
                val response = apiService.login(
                    email = email.value,
                    senha = password.value
                )
                if (response.isSuccessful) {
                    val token = response.body()?.get("access_token")
                    if (token != null) {
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