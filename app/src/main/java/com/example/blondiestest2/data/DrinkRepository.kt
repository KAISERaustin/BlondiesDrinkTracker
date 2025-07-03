package com.example.blondiestest2.data

import android.util.Log
import com.example.blondiestest2.data.local.Drink
import com.example.blondiestest2.data.local.DrinkDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DrinkRepository @Inject constructor(
    private val dao: DrinkDao
) {

    suspend fun insert(drink: Drink) {
        dao.insert(drink)
    }

    suspend fun delete(drink: Drink) {
        dao.delete(drink)
    }

    suspend fun update(drink: Drink) {
        dao.update(drink)
    }

    suspend fun deleteAll() {
        dao.deleteAll()
        Log.d("DrinkRepo", "🗑 Deleted all drinks")
    }

    fun getAllDrinks(): Flow<List<Drink>> = dao.getAll()

    suspend fun getByName(name: String): Drink? = dao.getByName(name)

    fun observeByName(name: String): Flow<Drink?> = dao.observeByName(name)
}
