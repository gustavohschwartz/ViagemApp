package com.example.viagemapp.components

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import com.example.viagemapp.api.GeminiService

@Composable
fun RoteiroSuggestionButton(destino: String) {
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var suggestion by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            isLoading = true
            showDialog = true
            errorMessage = null
            suggestion = ""

            GeminiService.sugerirRoteiro(
                destino = destino,
                onResult = {
                    suggestion = it
                    isLoading = false
                },
                onError = {
                    errorMessage = it
                    isLoading = false
                }
            )
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
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (errorMessage != null) {
                    Text("Erro: $errorMessage")
                } else {
                    Text(suggestion)
                }
            }
        )
    }
}
