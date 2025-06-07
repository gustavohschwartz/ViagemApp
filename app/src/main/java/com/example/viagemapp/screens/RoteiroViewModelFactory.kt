package com.example.viagemapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.viagemapp.repository.RoteiroRepository

class RoteiroViewModelFactory(
    private val repository: RoteiroRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RoteiroViewModel(repository) as T
    }
}
