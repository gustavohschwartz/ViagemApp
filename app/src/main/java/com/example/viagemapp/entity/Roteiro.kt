package com.example.viagemapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "roteiros")
data class Roteiro(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val destino: String,
    val sugestao: String,
    val aceito: Boolean = false,
    val username: String // <- Campo adicionado
)
