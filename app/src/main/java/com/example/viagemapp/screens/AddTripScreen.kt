package com.example.viagemapp.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.navigation.NavController
import com.example.viagemapp.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTripScreen(navController: NavController) {
    var destination by remember { mutableStateOf(TextFieldValue()) }
    var startDate by remember { mutableStateOf<Date?>(null) }
    var endDate by remember { mutableStateOf<Date?>(null) }
    var budget by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Business") }

    val context = LocalContext.current

    fun formatDate(date: Date?, defaultText: String): String {
        return date?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        } ?: defaultText
    }

    fun showDatePicker(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nova Viagem") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("menu") {
                            popUpTo("menu") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
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
            // Destino
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destino") },
                modifier = Modifier.fillMaxWidth()
            )

            // Tipo de viagem com imagem
            Text("Tipo de Viagem:")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(
                    onClick = { selectedType = "Business" },
                    modifier = Modifier
                        .size(72.dp)
                        .border(
                            width = if (selectedType == "Business") 2.dp else 0.dp,
                            color = if (selectedType == "Business") Color.Blue else Color.Transparent,
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
                        .border(
                            width = if (selectedType == "Leisure") 2.dp else 0.dp,
                            color = if (selectedType == "Leisure") Color.Blue else Color.Transparent,
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

            // Datas
            Button(
                onClick = {
                    showDatePicker { date ->
                        if (endDate != null && date.after(endDate)) {
                            Toast.makeText(context, "Data de ida não pode ser depois da volta.", Toast.LENGTH_SHORT).show()
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
                            Toast.makeText(context, "Data de volta não pode ser antes da ida.", Toast.LENGTH_SHORT).show()
                        } else {
                            endDate = date
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(formatDate(endDate, "Selecione a data de volta"))
            }

            // Orçamento
            OutlinedTextField(
                value = budget,
                onValueChange = { newValue ->
                    if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
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

            // Confirmar
            Button(
                onClick = {
                    val budgetValue = budget.toDoubleOrNull()
                    val validDates = startDate != null && endDate != null && !startDate!!.after(endDate!!)

                    when {
                        destination.text.isBlank() -> Toast.makeText(context, "Informe o destino.", Toast.LENGTH_SHORT).show()
                        startDate == null -> Toast.makeText(context, "Selecione a data de ida.", Toast.LENGTH_SHORT).show()
                        endDate == null -> Toast.makeText(context, "Selecione a data de volta.", Toast.LENGTH_SHORT).show()
                        !validDates -> Toast.makeText(context, "Datas inválidas.", Toast.LENGTH_SHORT).show()
                        budgetValue == null || budgetValue <= 0 -> Toast.makeText(context, "Informe um orçamento válido.", Toast.LENGTH_SHORT).show()
                        else -> {
                            Toast.makeText(context, "Viagem adicionada!", Toast.LENGTH_SHORT).show()
                            navController.navigate("menu") {
                                popUpTo("menu") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar")
            }
        }
    }
}
