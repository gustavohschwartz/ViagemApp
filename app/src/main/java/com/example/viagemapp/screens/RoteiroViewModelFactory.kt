package com.example.viagemapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.viagemapp.api.GeminiService
import com.example.viagemapp.repository.RoteiroRepository


class RoteiroViewModelFactory(
    private val repository: RoteiroRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoteiroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoteiroViewModel(repository, GeminiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}