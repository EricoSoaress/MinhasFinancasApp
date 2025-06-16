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

    private var listaOriginal: List<Transacao> = emptyList()

    private val _filtroAtual = MutableStateFlow("Recente")
    val filtroAtual = _filtroAtual.asStateFlow()

    private val _transacoesFiltradas = MutableStateFlow<List<Transacao>>(emptyList())
    val transacoesFiltradas = _transacoesFiltradas.asStateFlow()

    private val _listaCompleta = MutableStateFlow<List<Transacao>>(emptyList())
    val listaCompleta = _listaCompleta.asStateFlow()

    private val _logoutComplete = MutableStateFlow(false)
    val logoutComplete = _logoutComplete.asStateFlow()

    private val _transacaoParaDeletar = MutableStateFlow<Transacao?>(null)
    val transacaoParaDeletar = _transacaoParaDeletar.asStateFlow()

    init {
        userPreferencesRepository.authToken.onEach { token ->
            if (!token.isNullOrBlank()) {
                carregarTransacoes()
            } else {
                listaOriginal = emptyList()
                _transacoesFiltradas.value = emptyList()
                _listaCompleta.value = emptyList()
            }
        }.launchIn(viewModelScope)
    }

    fun carregarTransacoes() {
        viewModelScope.launch {
            try {
                val response = apiService.getTransacoes()
                if (response.isSuccessful) {
                    listaOriginal = response.body() ?: emptyList()
                    _listaCompleta.value = listaOriginal
                    aplicarFiltro()
                } else {
                    Log.e("HomeViewModel", "Erro ao buscar transações: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Falha na conexão: ${e.message}")
            }
        }
    }

    fun selecionarFiltro(filtro: String) {
        _filtroAtual.value = filtro
        aplicarFiltro()
    }

    private fun aplicarFiltro() {
        val listaFiltrada = when (_filtroAtual.value) {
            "Despesa" -> listaOriginal.filter { it.tipo.equals("despesa", ignoreCase = true) }
            "Receita" -> listaOriginal.filter { it.tipo.equals("receita", ignoreCase = true) }
            else -> listaOriginal
        }
        _transacoesFiltradas.value = listaFiltrada.sortedByDescending { it.data }
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