package com.example.viagemapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun MenuScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) {
        // Conteúdo principal da tela de Menu
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), // Para garantir que o conteúdo não sobreponha a BottomNavigation
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Bem-vindo ao Menu!", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary // Usando containerColor em vez de backgroundColor
    ) {
        Spacer(Modifier.weight(1f))

        // Ícone "+" para adicionar nova viagem
        FloatingActionButton(
            onClick = {
                // Ação para adicionar nova viagem
                navController.navigate("add_trip") // Navegação para a tela de adicionar viagem
            },
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Adicionar viagem")
        }

        Spacer(Modifier.weight(1f))
    }
}

