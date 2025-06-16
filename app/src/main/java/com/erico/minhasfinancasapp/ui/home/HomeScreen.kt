// Local: app/src/main/java/com/erico/minhasfinancasapp/ui/home/HomeScreen.kt

package com.erico.minhasfinancasapp.ui.home

import androidx.compose.foundation.background
import com.erico.minhasfinancasapp.ui.theme.GreenPrimary
import com.erico.minhasfinancasapp.ui.theme.GreenSecondary
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.erico.minhasfinancasapp.data.model.Transacao
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel()
) {
    // ... (toda a lógica de 'val transacoes', 'LaunchedEffect', etc., continua a mesma)
    val transacoes by homeViewModel.transacoes.collectAsState()
    val logoutComplete by homeViewModel.logoutComplete.collectAsState()
    val transacaoParaDeletar by homeViewModel.transacaoParaDeletar.collectAsState()

    LaunchedEffect(logoutComplete) {
        if (logoutComplete) {
            navController.navigate("login") {
                popUpTo("home") { inclusive = true }
            }
            homeViewModel.onLogoutComplete()
        }
    }

    val transacaoModificada = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<Boolean>("transacao_modificada") ?: false
    if (transacaoModificada) {
        LaunchedEffect(Unit) {
            homeViewModel.carregarTransacoes()
            navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>("transacao_modificada")
        }
    }

    transacaoParaDeletar?.let { transacao ->
        AlertDialog(
            onDismissRequest = { homeViewModel.onDismissDelecao() },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Você tem certeza que deseja deletar a transação '${transacao.descricao}'?") },
            confirmButton = { Button(onClick = { homeViewModel.onConfirmarDelecao() }) { Text("Deletar") } },
            dismissButton = { TextButton(onClick = { homeViewModel.onDismissDelecao() }) { Text("Cancelar") } }
        )
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Finanças") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent, // Deixamos transparente para o gradiente passar por baixo
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                actions = {
                    IconButton(onClick = { homeViewModel.logout() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = "Sair")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("transaction_screen/-1") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar Transação")
            }
        },
        // A cor do container do Scaffold também deve ser transparente
        containerColor = Color.Transparent
    ) { paddingValues ->
        // A MUDANÇA PRINCIPAL ESTÁ AQUI: O MODIFICADOR .background()
        Column(
            modifier = Modifier
                .fillMaxSize()
                // Aplicamos o gradiente a toda a coluna
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            SaldoGeralCard(transacoes)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Transações Recentes", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(transacoes, key = { it.id }) { transacao ->
                    TransacaoItem(
                        transacao = transacao,
                        onDeleteClick = { homeViewModel.onDeletarClicked(transacao) },
                        onItemClick = { navController.navigate("transaction_screen/${transacao.id}") }
                    )
                }
            }
        }
    }
}

// ... (O código para SaldoGeralCard e TransacaoItem pode continuar o mesmo da última versão)
@Composable
fun SaldoGeralCard(transacoes: List<Transacao>) {
    val saldo = transacoes.fold(BigDecimal.ZERO) { acc, transacao ->
        if (transacao.tipo.equals("receita", ignoreCase = true)) acc.add(transacao.valor) else acc.subtract(transacao.valor)
    }
    val formatadorDeMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Usando a cor de superfície do tema
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Saldo Geral", style = MaterialTheme.typography.titleMedium)
            Text(
                text = formatadorDeMoeda.format(saldo),
                style = MaterialTheme.typography.headlineLarge,
                color = if (saldo >= BigDecimal.ZERO) GreenPrimary else MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun TransacaoItem(
    transacao: Transacao,
    onDeleteClick: () -> Unit,
    onItemClick: () -> Unit
) {
    val formatadorDeMoeda = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    val valorFormatado = formatadorDeMoeda.format(transacao.valor)

    val isReceita = transacao.tipo.equals("receita", ignoreCase = true)
    val icon = if (isReceita) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
    val backgroundColor = if (isReceita) GreenSecondary else MaterialTheme.colorScheme.errorContainer
    val valorColor = if (isReceita) GreenPrimary else MaterialTheme.colorScheme.error

    val formatadorDeData = SimpleDateFormat("dd 'de' MMM", Locale("pt", "BR"))
    val dataFormatada = formatadorDeData.format(transacao.data)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onItemClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Removemos a elevação para um look mais "flat"
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = transacao.tipo, tint = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transacao.descricao,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = dataFormatada,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = valorFormatado,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = valorColor
                )
                IconButton(onClick = onDeleteClick, modifier = Modifier.size(24.dp).offset(x = 8.dp)) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Deletar Transação",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}