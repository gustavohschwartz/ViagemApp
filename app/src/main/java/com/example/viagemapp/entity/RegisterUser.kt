package com.example.viagemapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RegisterUser (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val name: String,
    val email: String,
    val password: String,

)