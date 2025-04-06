package com.example.viagemapp.screens

import androidx.lifecycle.ViewModel
import com.example.viagemapp.dao.RegisterUserDao

class RegisterUserViewFactory (
    private val registerUserDao: RegisterUserDao) : androidx.lifecycle.ViewModelProvider.Factory

    {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RegisterUserViewModel(registerUserDao) as T
        }
}