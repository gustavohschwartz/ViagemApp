package com.example.viagemapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.viagemapp.database.AppDatabase
import com.example.viagemapp.entity.Trip
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(navController: NavController, username: String) {
    val context = LocalContext.current
    val tripDao = AppDatabase.getDatabase(context).tripDao()
    val tripViewModel: TripViewModel = viewModel(factory = TripViewModelFactory(tripDao, username))
    val tripList by tripViewModel.trips.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minhas Viagens") },
                actions = {
                    TextButton(onClick = {
                        navController.navigate("login") {
                            popUpTo("menu") { inclusive = true }
                        }
                    }) {
                        Text("Sair", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(navController = navController, username = username)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (tripList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhuma viagem cadastrada.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(tripList) { trip ->
                        TripItem(trip)
                    }
                }
            }
        }
    }
}

@Composable
fun TripItem(trip: Trip) {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Destino: ${trip.destination}", style = MaterialTheme.typography.titleMedium)
            Text("Tipo: ${if (trip.type == "Business") "Negócio" else "Lazer"}")
            Text("Ida: ${sdf.format(Date(trip.startDate))}")
            Text("Volta: ${sdf.format(Date(trip.endDate))}")
            Text("Orçamento: R$ %.2f".format(trip.budget))
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController, username: String) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Spacer(Modifier.weight(1f))

        FloatingActionButton(
            onClick = {
                navController.navigate("add_trip/$username")
            },
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Adicionar viagem")
        }

        Spacer(Modifier.weight(1f))
    }
}
