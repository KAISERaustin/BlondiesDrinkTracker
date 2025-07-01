package com.example.blondiestest2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Drink::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun drinkDao(): DrinkDao
}