package com.example.viagemapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.viagemapp.entity.Roteiro

@Dao
interface RoteiroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(roteiro: Roteiro)

    @Query("SELECT * FROM Roteiro WHERE username = :username")
    suspend fun getRoteirosByUsername(username: String): List<Roteiro>

    @Delete
    suspend fun delete(roteiro: Roteiro)

}
