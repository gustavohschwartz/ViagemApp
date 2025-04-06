package com.example.viagemapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viagemapp.dao.RegisterUserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// Importa a entidade do banco (com @Entity e @PrimaryKey)
import com.example.viagemapp.entity.RegisterUser as EntityRegisterUser

data class RegisterUser(
    val user: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errorMessage: String = "",
    val isSaved: Boolean = false
) {
    fun validatePassord(): String {
        if (password.isBlank()) {
            return "Campo senha é obrigatório."
        }
        return ""
    }

    fun validateConfirmPassword(): String {
        if (confirmPassword != password) {
            return "As senhas não conferem."
        }
        return ""
    }

    fun validateAllField() {
        if (user.isBlank()) {
            throw Exception("Campo obrigatório")
        }
        if (name.isBlank()) {
            throw Exception("Campo obrigatório")
        }
        if (email.isBlank()) {
            throw Exception("Campo obrigatório")
        }
        if (validatePassord().isNotBlank()) {
            throw Exception(validatePassord())
        }
        if (validateConfirmPassword().isNotBlank()) {
            throw Exception(validateConfirmPassword())
        }
    }

    // Função para converter a classe da UI para a entidade do banco
    fun toEntity(): EntityRegisterUser {
        return EntityRegisterUser(
            username = this.user,
            name = this.name,
            email = this.email,
            password = this.password
        )
    }
}

class RegisterUserViewModel(private val registerUserDao: RegisterUserDao) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUser())
    val uiState: StateFlow<RegisterUser> = _uiState.asStateFlow()

    fun onUserChange(user: String) {
        _uiState.value = _uiState.value.copy(user = user)
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onConfirmPassword(confirm: String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirm)
    }

    fun register() {
        try {
            _uiState.value.validateAllField()

            viewModelScope.launch {
                registerUserDao.insert(_uiState.value.toEntity())
                _uiState.value = _uiState.value.copy(isSaved = true)
            }
        } catch (e: Exception) {
            _uiState.value =
                _uiState.value.copy(errorMessage = e.message ?: "Erro desconhecido")
        }
    }

    fun cleanErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }
}
