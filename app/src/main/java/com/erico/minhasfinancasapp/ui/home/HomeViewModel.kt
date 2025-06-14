package com.erico.minhasfinancasapp.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.erico.minhasfinancasapp.data.UserPreferencesRepository
import com.erico.minhasfinancasapp.data.model.Transacao
import com.erico.minhasfinancasapp.data.remote.ApiService
import com.erico.minhasfinancasapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: ApiService = RetrofitInstance.getRetrofitInstance(application).create(ApiService::class.java)
    private val userPreferencesRepository = UserPreferencesRepository(application)

    private val _transacoes = MutableStateFlow<List<Transacao>>(emptyList())
    val transacoes = _transacoes.asStateFlow()

    private val _logoutComplete = MutableStateFlow(false)
    val logoutComplete = _logoutComplete.asStateFlow()

    private val _transacaoParaDeletar = MutableStateFlow<Transacao?>(null)
    val transacaoParaDeletar = _transacaoParaDeletar.asStateFlow()

    init {
        userPreferencesRepository.authToken.onEach { token ->
            if (!token.isNullOrBlank()) {
                carregarTransacoes()
            } else {
                _transacoes.value = emptyList()
            }
        }.launchIn(viewModelScope)
    }

    fun carregarTransacoes() {
        viewModelScope.launch {
            try {
                val response = apiService.getTransacoes()
                if (response.isSuccessful) {
                    _transacoes.value = response.body() ?: emptyList()
                } else {
                    Log.e("HomeViewModel", "Erro ao buscar transações: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Falha na conexão: ${e.message}")
            }
        }
    }

    fun onDeletarClicked(transacao: Transacao) {
        _transacaoParaDeletar.value = transacao
    }

    fun onConfirmarDelecao() {
        viewModelScope.launch {
            _transacaoParaDeletar.value?.let { transacao ->
                try {
                    val response = apiService.deletarTransacao(transacao.id)
                    if (response.isSuccessful) {
                        carregarTransacoes()
                    }
                } catch (e: Exception) {
                    Log.e("HomeVM", "Falha na conexão ao deletar: ${e.message}")
                }
            }
            onDismissDelecao()
        }
    }

    fun onDismissDelecao() {
        _transacaoParaDeletar.value = null
    }

    fun logout() {
        viewModelScope.launch {
            userPreferencesRepository.clearAuthToken()
            _logoutComplete.value = true
        }
    }

    fun onLogoutComplete() {
        _logoutComplete.value = false
    }
}