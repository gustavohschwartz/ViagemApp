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

@Composable
fun MenuScreen(navController: NavController) {
    val context = LocalContext.current
    val tripDao = AppDatabase.getDatabase(context).tripDao()
    val tripViewModel: TripViewModel = viewModel(factory = TripViewModelFactory(tripDao))
    val tripList by tripViewModel.trips.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Minhas Viagens",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

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
fun BottomNavBar(navController: NavController) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primary
    ) {
        Spacer(Modifier.weight(1f))

        FloatingActionButton(
            onClick = {
                navController.navigate("add_trip")
            },
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Adicionar viagem")
        }

        Spacer(Modifier.weight(1f))
    }
}
