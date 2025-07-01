package com.example.blondiestest2.data

import com.example.blondiestest2.data.local.Drink
import com.example.blondiestest2.data.local.DrinkDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrinkRepository @Inject constructor(
    private val dao: DrinkDao
) {
    fun allDrinks(): Flow<List<Drink>> = dao.getAll()
    suspend fun add(drink: Drink) = dao.insert(drink)
    suspend fun delete(drink: Drink) = dao.delete(drink)
}
