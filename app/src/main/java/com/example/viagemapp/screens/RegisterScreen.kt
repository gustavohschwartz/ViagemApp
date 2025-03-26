package com.example.viagemapp.screens


import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.viagemapp.components.ErrorDialog
import com.example.viagemapp.components.TextField
import com.example.viagemapp.components.PasswordField
import com.example.viagemapp.ui.theme.ViagemAppTheme

@Composable
fun RegisterScreen(navController: NavController) {
    val registerUserViewModel: RegisterUserViewModel = viewModel()

    Scaffold {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegisterUserFields(registerUserViewModel, navController) // Adicionado navController
        }
    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun RegisterUserFields(registerUserViewModel: RegisterUserViewModel, navController: NavController) {
        var registerUser = registerUserViewModel.uiState.collectAsState()
        val ctx = LocalContext.current

        TextField(
            label = "User",
            value = registerUser.value.user,
            onValueChange = {
                registerUserViewModel.onUserChange(it)
            },
        )

        TextField(
            label = "Name",
            value = registerUser.value.name ,
            onValueChange = {
                registerUserViewModel.onNameChange(it)
            } )

        TextField(
            label = "E-mail",
            value = registerUser.value.email,
            onValueChange = {
                registerUserViewModel.onEmailChange(it)
            }
        )
        PasswordField(
            label = "Password",
            value = registerUser.value.password,
            errorMessage = registerUser.value.validatePassord(),
            onValueChange = {
                registerUserViewModel.onPasswordChange(it)
            })
        PasswordField(
            label = "Confirm password",
            value = registerUser.value.confirmPassword,
            errorMessage = registerUser.value.validateConfirmPassword() ,
            onValueChange = {
                registerUserViewModel.onConfirmPassword(it)
            })

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                if (registerUserViewModel.register()) {
                    Toast.makeText(ctx, "User registered",
                        Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text(text = "Cadastrar Usuário")
        }

        Button(
            modifier = Modifier.padding(top = 16.dp),
            onClick = {
                navController.navigate("login")
            }
        ) {
            Text(text = "Voltar")
        }





        if (registerUser.value.errorMessage.isNotBlank()) {
            ErrorDialog(
                error = registerUser.value.errorMessage,
                onDismissRequest =  {
                    registerUserViewModel.cleanErrorMessage()
                }
            )
        }
    }


