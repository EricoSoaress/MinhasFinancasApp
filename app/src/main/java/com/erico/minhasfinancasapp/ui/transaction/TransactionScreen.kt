package com.erico.minhasfinancasapp.ui.transaction

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun TransactionScreen(
    navController: NavController,
    transactionId: Int, // Recebe o ID da transação
    transactionViewModel: TransactionViewModel = viewModel()
) {
    val descricao by transactionViewModel.descricao.collectAsState()
    val valor by transactionViewModel.valor.collectAsState()
    val tipo by transactionViewModel.tipo.collectAsState()
    val saveSuccess by transactionViewModel.saveSuccess.collectAsState()

    // Carrega os dados da transação se estiver em modo de edição
    LaunchedEffect(key1 = Unit) {
        transactionViewModel.carregarTransacao(transactionId)
    }

    if (saveSuccess) {
        LaunchedEffect(key1 = Unit) {
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("transacao_modificada", true)
            navController.popBackStack()
            transactionViewModel.onSaveFinished()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        val title = if (transactionId == -1) "Adicionar Nova Transação" else "Editar Transação"
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        // ... (Resto do formulário como estava antes)
    }
}