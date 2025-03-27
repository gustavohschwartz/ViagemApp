package com.example.viagemapp.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class RegisterUser(
    val user: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val errorMessage: String = "",
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
        if (name.isBlank()){
            throw Exception ("Campo obrigatório")
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

}

class RegisterUserViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUser())
    val uiState : StateFlow<RegisterUser> = _uiState.asStateFlow()

    fun onUserChange(user: String) {
        _uiState.value = _uiState.value.copy(user = user)
    }

    fun onNameChange(name: String){
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onEmailChange(email : String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }

    fun onConfirmPassword(confirm : String) {
        _uiState.value = _uiState.value.copy(confirmPassword = confirm)
    }

    fun register(): Boolean  {
        try {
            _uiState.value.validateAllField()
            return true
            // register in database or invoke api
        }
        catch (e: Exception) {
            _uiState.value = _uiState.value.copy(errorMessage = e.message ?: "Unknow error")
            return false
        }
    }

    fun cleanErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }


}