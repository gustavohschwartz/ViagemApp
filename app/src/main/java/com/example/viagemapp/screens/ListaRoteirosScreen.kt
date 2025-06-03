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
import com.example.viagemapp.entity.Roteiro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRoteirosScreen(
    roteiroViewModel: RoteiroViewModel,
    navController: NavController,
    username: String
) {
    val context = LocalContext.current

    // Carrega apenas os roteiros aceitos do usuário logado
    val roteiros by roteiroViewModel.roteirosDoUsuario(username).collectAsState(initial = emptyList())

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
            items(roteiros.filter { it.aceito }) { roteiro ->
                RoteiroCard(roteiro = roteiro, onDelete = {
                    roteiroViewModel.excluirRoteiro(roteiro)
                })
            }
        }
    }
}

@Composable
fun RoteiroCard(roteiro: Roteiro, onDelete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Destino: ${roteiro.destino}", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (expanded) roteiro.sugestao else roteiro.sugestao.take(200) + "...",
                style = MaterialTheme.typography.bodyMedium
            )

            if (roteiro.sugestao.length > 200) {
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Ver menos" else "Ver mais")
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text("Aceito: ${if (roteiro.aceito) "Sim" else "Não"}")

            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = onDelete) {
                Text("Excluir")
            }
        }
    }
}
