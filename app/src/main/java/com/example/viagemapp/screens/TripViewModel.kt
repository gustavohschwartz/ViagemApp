package com.example.viagemapp.screens


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viagemapp.dao.TripDao
import com.example.viagemapp.entity.Trip
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TripViewModel(
    private val tripDao: TripDao,
    username: String
) : ViewModel() {

    val trips: StateFlow<List<Trip>> = tripDao.getTripsByUsername(username)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTrip(trip: Trip) {
        viewModelScope.launch {
            tripDao.insert(trip)
        }
    }

    fun deleteTrip(trip: Trip) {
        viewModelScope.launch {
            tripDao.delete(trip)
        }
    }
}

