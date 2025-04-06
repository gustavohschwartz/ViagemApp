package com.example.viagemapp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.example.viagemapp.entity.RegisterUser

@Dao
interface RegisterUserDao {
    @Insert
    suspend fun insert(registerUser: RegisterUser)
    @Update
    suspend fun update(registerUser: RegisterUser)
    @Upsert
    suspend fun upsert(registerUser: RegisterUser)
    @Delete
    suspend fun delete(registerUser: RegisterUser)
    @Query("select * from RegisterUser u where u.id =:id")
    suspend fun findById(id: Int): RegisterUser?
    @Query("select * from RegisterUser")
    suspend fun findAll(): List<RegisterUser>
    @Query("SELECT * FROM RegisterUser WHERE username = :username")
    suspend fun findByUsername(username: String): RegisterUser?

}
