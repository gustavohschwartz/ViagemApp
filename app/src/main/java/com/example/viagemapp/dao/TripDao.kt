package com.example.viagemapp.dao

import androidx.room.*
import com.example.viagemapp.entity.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Insert
    suspend fun insert(trip: Trip)

    @Query("SELECT * FROM Trip ORDER BY startDate DESC")
    fun getAllTrips(): Flow<List<Trip>>

    @Delete
    suspend fun delete(trip: Trip)
}
