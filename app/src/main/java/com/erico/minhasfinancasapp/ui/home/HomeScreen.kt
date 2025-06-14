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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    val transacoes by homeViewModel.transacoes.collectAsState()
    val logoutComplete by homeViewModel.logoutComplete.collectAsState()

    if (logoutComplete) {
        LaunchedEffect(key1 = Unit) {
            navController.navigate("login") {
                popUpTo(0)
            }
            homeViewModel.onLogoutComplete()
        }
    }

    // NOVO CÓDIGO: Ouve por resultados da tela anterior
    val novaTransacaoAdicionada = navController.currentBackStackEntry
        ?.savedStateHandle?.getLiveData<Boolean>("nova_transacao_adicionada")
        ?.observeAsState()

    LaunchedEffect(key1 = novaTransacaoAdicionada?.value) {
        if (novaTransacaoAdicionada?.value == true) {
            homeViewModel.carregarTransacoes() // Recarrega a lista
            // Limpa o estado para não recarregar de novo sem necessidade
            navController.currentBackStackEntry?.savedStateHandle?.set("nova_transacao_adicionada", false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Finanças") },
                actions = {
                    IconButton(onClick = { homeViewModel.logout() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Sair"
                        )
                    }
                }
            )
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navega para a nova rota que criamos
                    navController.navigate("add_transaction")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Transação"
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            items(transacoes) { transacao ->
                TransacaoItem(transacao = transacao)
                Divider()
            }
        }
    }
}

// O resto do arquivo continua igual...
@Composable
fun TransacaoItem(transacao: Transacao) {
    val corValor = if (transacao.tipo == "receita") Color(0xFF2E7D32) else Color.Red
    val valorFormatado = formatarMoeda(transacao.valor)

    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = transacao.descricao, style = MaterialTheme.typography.bodyLarge)
        Text(text = valorFormatado, color = corValor, style = MaterialTheme.typography.bodyLarge)
    }
}

fun formatarMoeda(valor: BigDecimal): String {
    return NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(valor)
}