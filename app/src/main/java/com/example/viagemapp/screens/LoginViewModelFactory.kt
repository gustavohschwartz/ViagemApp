package com.example.viagemapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.viagemapp.dao.RegisterUserDao

class LoginViewModelFactory(private val dao: RegisterUserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(dao) as T
    }
}
