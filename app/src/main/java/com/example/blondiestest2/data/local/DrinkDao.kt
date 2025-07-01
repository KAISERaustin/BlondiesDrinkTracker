package com.example.blondiestest2.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkDao {
    @Query("SELECT * FROM drinks")
    fun getAll(): Flow<List<Drink>>

    @Query("SELECT * FROM drinks WHERE id = :id")
    suspend fun getById(id: Int): Drink?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drink: Drink): Long

    @Update
    suspend fun update(drink: Drink)

    @Delete
    suspend fun delete(drink: Drink)
}