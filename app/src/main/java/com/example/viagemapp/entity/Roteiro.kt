package com.example.viagemapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Roteiro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val destino: String,
    val sugestao: String
)
