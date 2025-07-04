package com.example.blondiestest2.data

import com.example.blondiestest2.data.local.Drink
import com.example.blondiestest2.data.local.DrinkDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DrinkRepository @Inject constructor(
    private val dao: DrinkDao
) {

    suspend fun insert(drink: Drink) {
        dao.insert(drink)
    }

    suspend fun insertIfNotExists(drink: Drink) {
        val existing = dao.getByName(drink.name)
        if (existing == null) {
            dao.insert(drink)
        }
    }

    suspend fun delete(drink: Drink) {
        dao.delete(drink)
    }

    suspend fun update(drink: Drink) {
        dao.update(drink)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
    }

    fun getAllDrinks(): Flow<List<Drink>> = dao.getAll()

    suspend fun getByName(name: String): Drink? = dao.getByName(name)

    fun observeByName(name: String): Flow<Drink?> = dao.observeByName(name)

    /**
     * Seeds the database with the provided drinks only if the database is empty.
     * It checks for existing drinks by name to avoid duplicates.
     */
    suspend fun seedIfEmpty(drinks: List<Drink>) {
        val drinks = dao.getAll().first()
        for (drink in drinks) {
            val exists = dao.getByName(drink.name)
            if (exists == null) {
                dao.insert(drink)
            }
        }
    }
}