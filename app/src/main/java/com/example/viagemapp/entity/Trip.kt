package com.example.viagemapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Trip(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val destination: String,
    val startDate: Long,
    val endDate: Long,
    val budget: Double,
    val type: String
)
