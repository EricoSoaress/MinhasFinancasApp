package com.erico.minhasfinancasapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.erico.minhasfinancasapp.data.model.Transacao
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, // Adicione o NavController como parâmetro
    homeViewModel: HomeViewModel = viewModel()
) {
    val transacoes by homeViewModel.transacoes.collectAsState()
    val logoutComplete by homeViewModel.logoutComplete.collectAsState()

    // Observa o estado de logout. Quando for 'true', navega para o login.
    if (logoutComplete) {
        LaunchedEffect(key1 = Unit) {
            navController.navigate("login") {
                popUpTo(0) // Limpa toda a pilha de navegação
            }
            homeViewModel.onLogoutComplete() // Reseta o estado
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Finanças") },
                // NOVO BLOCO DE AÇÕES
                actions = {
                    IconButton(onClick = { homeViewModel.logout() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Sair"
                        )
                    }
                }
            )


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