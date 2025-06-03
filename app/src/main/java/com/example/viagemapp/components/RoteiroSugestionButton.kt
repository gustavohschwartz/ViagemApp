package com.example.viagemapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viagemapp.entity.Roteiro
import com.example.viagemapp.screens.RoteiroViewModel
import com.example.viagemapp.screens.RoteiroViewModelFactory
import com.example.viagemapp.database.AppDatabase
import com.example.viagemapp.api.GeminiService
import com.example.viagemapp.repository.RoteiroRepository

@Composable
fun RoteiroSuggestionButton(destino: String) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val viewModel: RoteiroViewModel = viewModel(
        factory = RoteiroViewModelFactory(
            RoteiroRepository(db.roteiroDao(), GeminiService),
            GeminiService
        )
    )

    var showDialog by remember { mutableStateOf(false) }

    val roteiro by viewModel.roteiro.collectAsState()

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            viewModel.carregarRoteiro(destino)
            showDialog = true
        }) {
            Text("Sugestões para $destino")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Fechar")
                }
            },
            title = { Text("Sugestão de Roteiro") },
            text = {
                Box(
                    modifier = Modifier
                        .heightIn(max = 400.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (roteiro == null) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    } else {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(roteiro!!.sugestao)
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                Button(onClick = { viewModel.aceitarRoteiro(roteiro!!); showDialog = false }) {
                                    Text("Aceitar")
                                }
                                OutlinedButton(onClick = { viewModel.recusarERetornarOutro(destino) }) {
                                    Text("Nova Sugestão")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
