package com.example.viagemapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viagemapp.dao.RegisterUserDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LoginState(
    val username: String = "",
    val password: String = "",
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false
)

class LoginViewModel(private val registerUserDao: RegisterUserDao) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun login() {
        viewModelScope.launch {
            val user = registerUserDao.findByUsername(_uiState.value.username)
            when {
                user == null -> _uiState.value = _uiState.value.copy(errorMessage = "Usuário não encontrado")
                user.password != _uiState.value.password -> _uiState.value = _uiState.value.copy(errorMessage = "Senha incorreta")
                else -> _uiState.value = _uiState.value.copy(isLoggedIn = true)
            }
        }
    }

    fun cleanErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }
}
