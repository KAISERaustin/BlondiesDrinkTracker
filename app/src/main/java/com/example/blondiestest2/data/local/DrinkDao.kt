package com.example.blondiestest2.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DrinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(drink: Drink)

    @Delete
    suspend fun delete(drink: Drink)

    @Update
    suspend fun update(drink: Drink)

    @Query("DELETE FROM drinks")
    suspend fun deleteAll()

    @Query("SELECT * FROM drinks")
    fun getAll(): Flow<List<Drink>>

    @Query("SELECT * FROM drinks WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): Drink?

    @Query("SELECT * FROM drinks WHERE name = :name LIMIT 1")
    fun observeByName(name: String): Flow<Drink?>
}
