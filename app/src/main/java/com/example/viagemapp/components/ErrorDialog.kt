package com.example.viagemapp.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
@ExperimentalMaterial3Api
fun ErrorDialog(
    error : String,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        title = {
            Text("Error message")
        },
        text = {
            Text(text = error)
        },
        confirmButton = {
            Button(onClick = onDismissRequest) {
                Text(text = "Ok")
            }
        },
        onDismissRequest = onDismissRequest,
    )



}