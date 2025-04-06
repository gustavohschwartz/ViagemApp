package com.example.viagemapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.viagemapp.components.ErrorDialog
import com.example.viagemapp.components.PasswordField
import com.example.viagemapp.components.TextField
import com.example.viagemapp.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun RegisterScreen(navController: NavController) {
    val ctx = LocalContext.current
    val registerUserDao = AppDatabase.getDatabase(ctx).registerUserDao()
    val registerUserViewModel: RegisterUserViewModel =
        viewModel(factory = RegisterUserViewFactory(registerUserDao))

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterUserFields(
                registerUserViewModel = registerUserViewModel,
                navController = navController,
                snackbarHostState = snackbarHostState,
                coroutineScope = coroutineScope
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterUserFields(
    registerUserViewModel: RegisterUserViewModel,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope
) {
    val registerUser = registerUserViewModel.uiState.collectAsState()

    TextField(
        label = "Usuário",
        value = registerUser.value.user,
        onValueChange = { registerUserViewModel.onUserChange(it) }
    )

    TextField(
        label = "Nome Completo",
        value = registerUser.value.name,
        onValueChange = { registerUserViewModel.onNameChange(it) }
    )

    TextField(
        label = "E-mail",
        value = registerUser.value.email,
        onValueChange = { registerUserViewModel.onEmailChange(it) }
    )

    PasswordField(
        label = "Senha",
        value = registerUser.value.password,
        errorMessage = registerUser.value.validatePassord(),
        onValueChange = { registerUserViewModel.onPasswordChange(it) }
    )

    PasswordField(
        label = "Confirmar Senha",
        value = registerUser.value.confirmPassword,
        errorMessage = registerUser.value.validateConfirmPassword(),
        onValueChange = { registerUserViewModel.onConfirmPassword(it) }
    )

    Button(
        modifier = Modifier.padding(top = 16.dp),
        onClick = { registerUserViewModel.register() }
    ) {
        Text(text = "Cadastrar Usuário")
    }

    Button(
        modifier = Modifier.padding(top = 16.dp),
        onClick = { navController.navigate("login") }
    ) {
        Text(text = "Voltar")
    }

    // Mostrar snackbar e redirecionar após salvar
    if (registerUser.value.isSaved) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Usuário cadastrado com sucesso!",
                    actionLabel = "OK"
                )

                if (result == androidx.compose.material3.SnackbarResult.ActionPerformed) {
                    registerUserViewModel.clearFields()
                    navController.navigate("login")
                }
            }
        }

    }

    // Dialog de erro
    if (registerUser.value.errorMessage.isNotBlank()) {
        ErrorDialog(
            error = registerUser.value.errorMessage,
            onDismissRequest = { registerUserViewModel.cleanErrorMessage() }
        )
    }
}
