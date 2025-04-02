package com.example.viagemapp.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.clickable // Mantenha se usar clickable em outro lugar
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
    var selectedType by remember { mutableStateOf("Business") } // Ou "Leisure" como padrão inicial, se preferir

    val context = LocalContext.current

    // --- MODIFICAÇÃO 1: Adicionar parâmetro 'defaultText' ---
    // Função para formatar a data ou retornar um texto padrão
    fun formatDate(date: Date?, defaultText: String): String {
        return date?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
        } ?: defaultText // Retorna o texto padrão fornecido se a data for nula
    }

    // Função para exibir o DatePicker
    fun showDatePicker(onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance()
        // Se uma data já foi selecionada, inicializa o DatePicker com essa data
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(calendar.time)
            },
            initialYear,
            initialMonth,
            initialDay
        )

        // Opcional: Definir data mínima para o seletor de data final
        // Se for o seletor de data final e a data inicial já foi definida,
        // você pode querer definir a data mínima para ser a data inicial.
        // Isso exigiria passar qual data está sendo selecionada para `showDatePicker`.
        // Por simplicidade, vamos omitir isso por enquanto.

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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically // Alinha verticalmente os RadioButtons e Textos
        ) {
            Text("Tipo de Viagem: ")
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) { // Agrupa RadioButton e Texto
                RadioButton(
                    selected = selectedType == "Business",
                    onClick = { selectedType = "Business" }
                )
                Text("Negócio", Modifier.clickable { selectedType = "Business" }) // Torna o texto clicável também
            }
            Spacer(modifier = Modifier.width(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) { // Agrupa RadioButton e Texto
                RadioButton(
                    selected = selectedType == "Leisure",
                    onClick = { selectedType = "Leisure" }
                )
                Text("Lazer", Modifier.clickable { selectedType = "Leisure" }) // Torna o texto clicável também
            }
        }

        // --- MODIFICAÇÃO 2: Usar o texto padrão específico para Data de Ida ---
        // Data de Início
        Button(
            onClick = {
                showDatePicker { date ->
                    // Opcional: Validação para garantir que a data de início não seja posterior à data de fim
                    if (endDate != null && date.after(endDate)) {
                        Toast.makeText(context, "Data de ida não pode ser depois da data de volta.", Toast.LENGTH_SHORT).show()
                    } else {
                        startDate = date
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            // Passa o texto padrão desejado para a função formatDate
            Text(formatDate(startDate, "Selecione a data de ida"))
        }

        // --- MODIFICAÇÃO 3: Usar o texto padrão específico para Data de Volta ---
        // Data de Fim
        Button(
            onClick = {
                showDatePicker { date ->
                    // Opcional: Validação para garantir que a data de fim não seja anterior à data de início
                    if (startDate != null && date.before(startDate)) {
                        Toast.makeText(context, "Data de volta não pode ser antes da data de ida.", Toast.LENGTH_SHORT).show()
                    } else {
                        endDate = date
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            // Opcional: Desabilitar o botão de data de volta se a data de ida não foi selecionada
            // enabled = startDate != null
        ) {
            // Passa o texto padrão desejado para a função formatDate
            Text(formatDate(endDate, "Selecione a data de volta"))
        }

        // Orçamento
        OutlinedTextField(
            value = budget,
            onValueChange = { newValue ->
                // Permite números e um único ponto decimal
                if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                    budget = newValue
                }
            },
            label = { Text("Orçamento") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal // Mudar para Decimal para permitir ponto
            ),
            keyboardActions = KeyboardActions.Default,
            modifier = Modifier.fillMaxWidth(),
            prefix = { Text("R$ ") } // Adiciona um prefixo para clareza
        )

        // Botão Confirmar
        Button(
            onClick = {
                // Validação mais robusta
                val budgetValue = budget.toDoubleOrNull() // Tenta converter para Double
                val isStartDateBeforeEndDate = startDate != null && endDate != null && !startDate!!.after(endDate!!)

                if (destination.text.isBlank()) {
                    Toast.makeText(context, "Por favor, informe o destino.", Toast.LENGTH_SHORT).show()
                } else if (startDate == null) {
                    Toast.makeText(context, "Por favor, selecione a data de ida.", Toast.LENGTH_SHORT).show()
                } else if (endDate == null) {
                    Toast.makeText(context, "Por favor, selecione a data de volta.", Toast.LENGTH_SHORT).show()
                } else if (!isStartDateBeforeEndDate) {
                    Toast.makeText(context, "Data de ida deve ser anterior ou igual à data de volta.", Toast.LENGTH_SHORT).show()
                } else if (budget.isBlank() || budgetValue == null || budgetValue <= 0) {
                    Toast.makeText(context, "Por favor, informe um orçamento válido.", Toast.LENGTH_SHORT).show()
                } else {
                    // Se todos os campos forem preenchidos corretamente
                    // Aqui você normalmente salvaria os dados (por exemplo, em um ViewModel, banco de dados)
                    Toast.makeText(context, "Viagem adicionada!", Toast.LENGTH_SHORT).show() // Feedback
                    navController.navigate("menu") {
                        // Limpa a pilha de backstack até 'menu' se 'menu' já estiver lá,
                        // ou simplesmente navega se não estiver. Evita múltiplas instâncias de 'menu'.
                        popUpTo("menu") { inclusive = false }
                        // Evita adicionar esta tela (AddTripScreen) à pilha de backstack
                        launchSingleTop = true
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar")
        }
    }
}