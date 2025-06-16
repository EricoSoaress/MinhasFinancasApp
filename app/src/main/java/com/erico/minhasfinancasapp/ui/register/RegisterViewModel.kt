// Local: app/src/main/java/com/erico/minhasfinancasapp/ui/register/RegisterViewModel.kt

package com.erico.minhasfinancasapp.ui.register

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.erico.minhasfinancasapp.data.model.NovoUsuario
import com.erico.minhasfinancasapp.data.remote.ApiService
import com.erico.minhasfinancasapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: ApiService = RetrofitInstance.getRetrofitInstance(application).create(ApiService::class.java)

    // --- NOVO CAMPO ADICIONADO ---
    val nome = MutableStateFlow("")
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val confirmPassword = MutableStateFlow("")

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess = _registrationSuccess.asStateFlow()

    fun register() {
        if (isLoading.value) return

        // Validações
        if (nome.value.isBlank()) {
            _errorMessage.value = "O campo Nome é obrigatório."
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            _errorMessage.value = "Por favor, insira um e-mail válido."
            return
        }
        if (password.value.length < 6) {
            _errorMessage.value = "A senha deve ter pelo menos 6 caracteres."
            return
        }
        if (password.value != confirmPassword.value) {
            _errorMessage.value = "As senhas não coincidem."
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // --- OBJETO ATUALIZADO COM O NOME ---
                val novoUsuario = NovoUsuario(
                    nome = nome.value,
                    email = email.value,
                    senha = password.value
                )
                val response = apiService.criarUsuario(novoUsuario)

                if (response.isSuccessful) {
                    _registrationSuccess.value = true
                } else {
                    _errorMessage.value = "Este e-mail já está cadastrado."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Falha na conexão. Tente novamente."
            } finally {
                _isLoading.value = false
            }
        }
    }
}