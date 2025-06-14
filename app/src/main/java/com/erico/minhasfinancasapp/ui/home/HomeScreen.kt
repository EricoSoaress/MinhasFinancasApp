package com.erico.minhasfinancasapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.erico.minhasfinancasapp.data.model.Transacao
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Delete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val transacaoParaDeletar by homeViewModel.transacaoParaDeletar.collectAsState()
    // ... (código existente)

    transacaoParaDeletar?.let {
        AlertDialog(
            onDismissRequest = { homeViewModel.onDismissDelecao() },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Você tem certeza que deseja deletar a transação '${it.descricao}'?") },
            confirmButton = { Button(onClick = { homeViewModel.onConfirmarDelecao() }) { Text("Deletar") } },
            dismissButton = { TextButton(onClick = { homeViewModel.onDismissDelecao() }) { Text("Cancelar") } }
        )
    }

    Scaffold(
        topBar = { },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Navega para a tela de transação em modo de CRIAÇÃO
                navController.navigate("transaction_screen/-1")
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Transação")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            items(transacoes) { transacao ->
                TransacaoItem(
                    transacao = transacao,
                    onDeleteClick = { homeViewModel.onDeletarClicked(transacao) },
                    onItemClick = {
                        // Navega para a tela de transação em modo de EDIÇÃO
                        navController.navigate("transaction_screen/${transacao.id}")
                    }
                )
                Divider()
            }
        }
    }
}

@Composable
fun TransacaoItem(
    transacao: Transacao,
    onDeleteClick: () -> Unit,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onItemClick)
            .padding(vertical = 8.dp),
        // ... (resto do Row)
    ) {
        // ... (código dos Texts)
        IconButton(onClick = onDeleteClick) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Deletar Transação")
        }
    }
}
// ...