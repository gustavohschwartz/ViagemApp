package com.example.viagemapp.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AddTripScreen(navController: NavController) {
    var destination by remember { mutableStateOf(TextFieldValue()) }
    var startDate by remember { mutableStateOf<Date?>(null) }
    var endDate by remember { mutableStateOf<Date?>(null) }
    var budget by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("Business") }

    val context = LocalContext.current

    // Função para formatar a data
    fun formatDate(date: Date?): String {
        return date?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        } ?: "Selecione a data"
    }

    // Função para exibir o DatePicker
    fun showDatePicker(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            context, // Usar o contexto correto
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
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

        // Tipo de viagem (Negócio ou Lazer)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Text("Tipo de Viagem: ")
            Spacer(modifier = Modifier.width(8.dp))
            RadioButton(
                selected = selectedType == "Business",
                onClick = { selectedType = "Business" }
            )
            Text("Negócio")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = selectedType == "Leisure",
                onClick = { selectedType = "Leisure" }
            )
            Text("Lazer")
        }

        // Data de Início
        Button(
            onClick = {
                showDatePicker { date ->
                    startDate = date
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(formatDate(startDate))
        }

        // Data de Fim
        Button(
            onClick = {
                showDatePicker { date ->
                    endDate = date
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(formatDate(endDate))
        }

        // Orçamento
        OutlinedTextField(
            value = budget,
            onValueChange = { newValue ->
                // Apenas permite números no orçamento
                if (newValue.all { it.isDigit() || it == '.' }) {
                    budget = newValue
                }
            },
            label = { Text("Orçamento") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions.Default,
            modifier = Modifier.fillMaxWidth()
        )

        // Botão Confirmar
        Button(
            onClick = {
                if (destination.text.isNotEmpty() && startDate != null && endDate != null && budget.isNotEmpty()) {
                    // Se todos os campos forem preenchidos corretamente
                    navController.navigate("menu") // Retorna para a tela de menu
                } else {
                    // Se algum campo estiver vazio, exibe o AlertDialog de falha
                    Toast.makeText(
                        context,
                        "Por favor, preencha todos os campos!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar")
        }
    }
}