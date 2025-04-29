package com.example.viagemapp.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.viagemapp.R
import com.example.viagemapp.api.GeminiService
import com.example.viagemapp.database.AppDatabase
import com.example.viagemapp.entity.Trip
import java.text.SimpleDateFormat
import java.util.*

fun calcularDiasViagem(startDate: Long, endDate: Long): Int {
    val diffMillis = endDate - startDate
    val dias = (diffMillis / (1000 * 60 * 60 * 24)).toInt()
    return dias.coerceAtLeast(1)
}

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
                title = { Text("Minhas Viagens", color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    TextButton(onClick = {
                        navController.navigate("login") {
                            popUpTo("menu") { inclusive = true }
                        }
                    }) {
                        Text("Sair", color = MaterialTheme.colorScheme.onPrimary)
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
            Text(
                text = "Dica: pressione e segure para editar uma viagem ou deslize para a esquerda para excluir.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                        TripItem(
                            trip = trip,
                            onDelete = { tripViewModel.deleteTrip(trip) },
                            onEdit = { navController.navigate("edit_trip/${trip.id}/$username") }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TripItem(
    trip: Trip,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val dismissState = rememberDismissState(
        confirmValueChange = {
            if (it == DismissValue.DismissedToStart) {
                onDelete()
                true
            } else false
        }
    )

    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val iconRes = if (trip.type == "Business") R.drawable.ic_business else R.drawable.ic_leisure

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color = if (direction == DismissDirection.EndToStart)
                MaterialTheme.colorScheme.errorContainer else Color.Transparent

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, contentDescription = null)
            }
        },
        dismissContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .combinedClickable(
                        onClick = {},
                        onLongClick = onEdit
                    ),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Destino: ${trip.destination}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text("Ida: ${sdf.format(Date(trip.startDate))}")
                            Text("Volta: ${sdf.format(Date(trip.endDate))}")
                            Text("Orçamento: R$ %.2f".format(trip.budget))
                        }

                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = iconRes),
                                contentDescription = null,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    SuggestionButtonWithDays(trip)
                }
            }
        }
    )
}

@Composable
fun SuggestionButtonWithDays(trip: Trip) {
    var showDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var suggestion by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val days = calcularDiasViagem(trip.startDate, trip.endDate)

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = {
            isLoading = true
            showDialog = true
            errorMessage = null
            suggestion = ""

            val prompt = "Sugira um roteiro de ${days} ${if (days == 1) "dia" else "dias"} para ${trip.destination}, incluindo dicas locais, culinária e pontos turísticos."

            GeminiService.sugerirRoteiro(
                destino = prompt,
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
            Text("Sugestões para ${trip.destination}")
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
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else if (errorMessage != null) {
                        Text("Erro: $errorMessage")
                    } else {
                        Text(suggestion)
                    }
                }
            }
        )
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
