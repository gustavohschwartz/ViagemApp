package com.example.viagemapp.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.viagemapp.R
import com.example.viagemapp.database.AppDatabase
import com.example.viagemapp.entity.Trip
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripScreen(navController: NavController, username: String, existingTrip: Trip? = null) {
    val ctx = LocalContext.current
    val tripDao = AppDatabase.getDatabase(ctx).tripDao()
    val tripViewModel: TripViewModel = viewModel(factory = TripViewModelFactory(tripDao, username))

    var destination by remember { mutableStateOf(TextFieldValue(existingTrip?.destination ?: "")) }
    var startDate by remember { mutableStateOf(existingTrip?.startDate?.let { Date(it) }) }
    var endDate by remember { mutableStateOf(existingTrip?.endDate?.let { Date(it) }) }
    var budget by remember { mutableStateOf(existingTrip?.budget?.toString() ?: "") }
    var selectedType by remember { mutableStateOf(existingTrip?.type ?: "Business") }

    fun formatDate(date: Date?, defaultText: String): String {
        return date?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        } ?: defaultText
    }

    fun showDatePicker(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            ctx,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (existingTrip == null) "Nova Viagem" else "Editar Viagem",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("menu/$username") {
                            popUpTo("menu/$username") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destino") },
                modifier = Modifier.fillMaxWidth()
            )

            Text("Tipo de Viagem:")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val iconBgColor = MaterialTheme.colorScheme.surface

                IconButton(
                    onClick = { selectedType = "Business" },
                    modifier = Modifier
                        .size(72.dp)
                        .background(iconBgColor, RoundedCornerShape(12.dp))
                        .border(
                            width = if (selectedType == "Business") 2.dp else 0.dp,
                            color = if (selectedType == "Business") MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_business),
                        contentDescription = "Negócio",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                IconButton(
                    onClick = { selectedType = "Leisure" },
                    modifier = Modifier
                        .size(72.dp)
                        .background(iconBgColor, RoundedCornerShape(12.dp))
                        .border(
                            width = if (selectedType == "Leisure") 2.dp else 0.dp,
                            color = if (selectedType == "Leisure") MaterialTheme.colorScheme.primary else Color.Transparent,
                            shape = RoundedCornerShape(12.dp)
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_leisure),
                        contentDescription = "Lazer",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Button(
                onClick = {
                    showDatePicker { date ->
                        if (endDate != null && date.after(endDate)) {
                            Toast.makeText(ctx, "Data de ida não pode ser depois da volta.", Toast.LENGTH_SHORT).show()
                        } else {
                            startDate = date
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(formatDate(startDate, "Selecione a data de ida"))
            }

            Button(
                onClick = {
                    showDatePicker { date ->
                        if (startDate != null && date.before(startDate)) {
                            Toast.makeText(ctx, "Data de volta não pode ser antes da ida.", Toast.LENGTH_SHORT).show()
                        } else {
                            endDate = date
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(formatDate(endDate, "Selecione a data de volta"))
            }

            OutlinedTextField(
                value = budget,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*"))) {
                        budget = newValue
                    }
                },
                label = { Text("Orçamento") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                ),
                modifier = Modifier.fillMaxWidth(),
                prefix = { Text("R$ ") }
            )

            Button(
                onClick = {
                    val budgetValue = budget.toDoubleOrNull()
                    val validDates = startDate != null && endDate != null && !startDate!!.after(endDate!!)

                    when {
                        destination.text.isBlank() -> Toast.makeText(ctx, "Informe o destino.", Toast.LENGTH_SHORT).show()
                        startDate == null -> Toast.makeText(ctx, "Selecione a data de ida.", Toast.LENGTH_SHORT).show()
                        endDate == null -> Toast.makeText(ctx, "Selecione a data de volta.", Toast.LENGTH_SHORT).show()
                        !validDates -> Toast.makeText(ctx, "Datas inválidas.", Toast.LENGTH_SHORT).show()
                        budgetValue == null || budgetValue <= 0 -> Toast.makeText(ctx, "Informe um orçamento válido.", Toast.LENGTH_SHORT).show()
                        else -> {
                            val trip = Trip(
                                id = existingTrip?.id ?: 0,
                                destination = destination.text,
                                startDate = startDate!!.time,
                                endDate = endDate!!.time,
                                budget = budgetValue,
                                type = selectedType,
                                username = username
                            )

                            if (existingTrip != null) {
                                tripViewModel.updateTrip(trip)
                                Toast.makeText(ctx, "Viagem atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                            } else {
                                tripViewModel.addTrip(trip)
                                Toast.makeText(ctx, "Viagem salva com sucesso!", Toast.LENGTH_SHORT).show()
                            }

                            navController.navigate("menu/$username") {
                                popUpTo("menu/$username") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (existingTrip == null) "Confirmar" else "Atualizar")
            }
        }
    }
}
