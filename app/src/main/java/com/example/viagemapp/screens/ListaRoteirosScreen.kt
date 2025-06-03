package com.example.viagemapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRoteirosScreen(
    roteiroViewModel: RoteiroViewModel,
    navController: NavController,
    username: String
) {
    val roteiros by roteiroViewModel.roteiros.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        roteiroViewModel.carregarTodosRoteiros()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sugestões Armazenadas") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(roteiros) { roteiro ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Destino: ${roteiro.destino}", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Sugestão: ${roteiro.sugestao}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Aceito: ${if (roteiro.aceito) "Sim" else "Não"}")
                    }
                }
            }
        }
    }
}