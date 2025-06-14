package com.erico.minhasfinancasapp.ui.transaction

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.erico.minhasfinancasapp.data.model.NovaTransacao
import com.erico.minhasfinancasapp.data.remote.ApiService
import com.erico.minhasfinancasapp.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService: ApiService = RetrofitInstance.getRetrofitInstance(application).create(ApiService::class.java)

    val descricao = MutableStateFlow("")
    val valor = MutableStateFlow("")
    val tipo = MutableStateFlow("despesa")
    private var transacaoId: Int? = null

    private val _saveSuccess = MutableStateFlow(false)
    val saveSuccess = _saveSuccess.asStateFlow()

    fun carregarTransacao(id: Int) {
        if (id == -1) return // Modo de criação, não faz nada
        transacaoId = id
        viewModelScope.launch {
            try {
                val response = apiService.getTransacaoPorId(id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        descricao.value = it.descricao
                        valor.value = it.valor.toPlainString()
                        tipo.value = it.tipo
                    }
                }
            } catch (e: Exception) {
                Log.e("TransactionVM", "Erro ao carregar transação: ${e.message}")
            }
        }
    }

    fun salvarTransacao() {
        viewModelScope.launch {
            try {
                if (descricao.value.isBlank() || valor.value.isBlank()) return@launch

                val novaTransacao = NovaTransacao(
                    descricao = descricao.value,
                    valor = BigDecimal(valor.value.replace(",", ".")),
                    tipo = tipo.value
                )

                val response = if (transacaoId == null) {
                    apiService.criarTransacao(novaTransacao)
                } else {
                    apiService.atualizarTransacao(transacaoId!!, novaTransacao)
                }

                if (response.isSuccessful) {
                    _saveSuccess.value = true
                } else {
                    Log.e("TransactionVM", "Erro na operação: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("TransactionVM", "Falha na operação: ${e.message}")
            }
        }
    }

    fun onSaveFinished() {
        _saveSuccess.value = false
    }
}