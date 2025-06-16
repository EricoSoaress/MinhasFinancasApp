
package com.erico.minhasfinancasapp.ui.transaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionScreen(
    navController: NavController,
    transactionId: Int,
    transactionViewModel: TransactionViewModel = viewModel()
) {
    val descricao by transactionViewModel.descricao.collectAsState()
    val valor by transactionViewModel.valor.collectAsState()
    val tipo by transactionViewModel.tipo.collectAsState()
    val saveSuccess by transactionViewModel.saveSuccess.collectAsState()
    val isLoading by transactionViewModel.isLoading.collectAsState() // Novo estado

    LaunchedEffect(key1 = transactionId) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = if (transactionId == -1) "Adicionar Transação" else "Editar Transação"
                    Text(text = title)
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = descricao,
                        onValueChange = { transactionViewModel.descricao.value = it },
                        label = { Text("Descrição") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = valor,
                        onValueChange = { transactionViewModel.valor.value = it },
                        label = { Text("Valor (Ex: 99,90)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Tipo de Transação", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = tipo == "despesa",
                            onClick = { transactionViewModel.tipo.value = "despesa" }
                        )
                        Text(
                            text = "Despesa",
                            modifier = Modifier.clickable(onClick = { transactionViewModel.tipo.value = "despesa" })
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(
                            selected = tipo == "receita",
                            onClick = { transactionViewModel.tipo.value = "receita" }
                        )
                        Text(
                            text = "Receita",
                            modifier = Modifier.clickable(onClick = { transactionViewModel.tipo.value = "receita" })
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { transactionViewModel.salvarTransacao() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text("Salvar")
                    }
                }
            }
        }
    }
}