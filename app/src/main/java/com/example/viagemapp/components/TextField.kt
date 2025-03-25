package com.example.viagemapp.components

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent

@Composable
fun TextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    var isTouched = remember {
        mutableStateOf(false)
    }
    var focusRequester = remember {
        FocusRequester()
    }
    OutlinedTextField(
        value = value ,
        onValueChange = {
            isTouched.value = true
            onValueChange(it)
        },
        singleLine = true,
        label = {
            Text(text = label)
        },
        modifier = Modifier
            .focusRequester(focusRequester)
            .onFocusEvent {
                if (it.hasFocus)
                    isTouched.value = true
            },
        isError = isTouched.value && value.isBlank(),
        supportingText = {
            if (isTouched.value && value.isBlank()) {
                Text(text = "Field ${label} is required")
            }
        }
    )

}