package com.example.viagemapp.screens


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.viagemapp.dao.TripDao

class TripViewModelFactory(private val tripDao: TripDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TripViewModel(tripDao) as T
    }
}
