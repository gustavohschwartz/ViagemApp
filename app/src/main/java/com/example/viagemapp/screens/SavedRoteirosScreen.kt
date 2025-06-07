package com.example.viagemapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viagemapp.database.AppDatabase
import com.example.viagemapp.repository.RoteiroRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedRoteirosScreen(username: String, onBack: () -> Unit) {
    val context = LocalContext.current
    val roteiroDao = AppDatabase.getDatabase(context).roteiroDao()
    val roteiroRepository = RoteiroRepository(roteiroDao)
    val roteiroViewModel: RoteiroViewModel = viewModel(factory = RoteiroViewModelFactory(roteiroRepository))

    LaunchedEffect(Unit) {
        roteiroViewModel.carregarRoteiros(username)
    }

    val roteiros = roteiroViewModel.roteiros

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Roteiros salvos",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = paddingValues.calculateTopPadding(),
                bottom = 16.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(roteiros) { roteiro ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF1F8E9)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                        ) {
                            Text(
                                text = "Destino: ${roteiro.destino}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = roteiro.sugestao,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(
                            onClick = {
                                roteiroViewModel.deletarRoteiro(roteiro)
                                roteiroViewModel.carregarRoteiros(username)  // Recarregar lista após deletar
                            },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Deletar roteiro",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
        }
    }

