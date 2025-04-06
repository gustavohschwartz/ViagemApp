package com.example.viagemapp.dao

import androidx.room.*
import com.example.viagemapp.entity.Trip
import kotlinx.coroutines.flow.Flow

@Dao
interface TripDao {
    @Insert
    suspend fun insert(trip: Trip)

    @Query("SELECT * FROM Trip WHERE username = :username ORDER BY startDate DESC")
    fun getTripsByUsername(username: String): Flow<List<Trip>>


    @Delete
    suspend fun delete(trip: Trip)
}
