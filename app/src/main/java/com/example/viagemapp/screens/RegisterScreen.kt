package com.example.viagemapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {
    val ctx = LocalContext.current
    val registerUserDao = AppDatabase.getDatabase(ctx).registerUserDao()
    val registerUserViewModel: RegisterUserViewModel =
        viewModel(factory = RegisterUserViewFactory(registerUserDao))

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Cadastro", color = MaterialTheme.colorScheme.onPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("login") {
                            popUpTo("login") { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
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

    // Mostrar snackbar e redirecionar após salvar
    if (registerUser.value.isSaved) {
        LaunchedEffect(Unit) {
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = "Usuário cadastrado com sucesso!",
                    actionLabel = "OK"
                )
                if (result == SnackbarResult.ActionPerformed) {
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
