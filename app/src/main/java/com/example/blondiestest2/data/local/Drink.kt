package com.example.blondiestest2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drinks")
data class Drink(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val ingredients: String,
    val instructions: String
)
