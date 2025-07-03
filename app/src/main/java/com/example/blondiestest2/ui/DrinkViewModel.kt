package com.example.blondiestest2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blondiestest2.data.DrinkRepository
import com.example.blondiestest2.data.local.Drink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinkViewModel @Inject constructor(
    private val repository: DrinkRepository
) : ViewModel() {

    val allDrinks: StateFlow<List<Drink>> = repository.getAllDrinks()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _drinks = MutableStateFlow<List<Drink>>(emptyList())
    val drinks: StateFlow<List<Drink>> = _drinks.asStateFlow()

    private val _recentlyDeleted = MutableStateFlow<Drink?>(null)
    val recentlyDeleted: StateFlow<Drink?> = _recentlyDeleted.asStateFlow()

    init {
        viewModelScope.launch {
            allDrinks.collect { list ->
                _drinks.value = list
            }
        }
    }

    fun addDrink(name: String, ingredients: String, instructions: String) {
        viewModelScope.launch {
            repository.insert(Drink(name = name, ingredients = ingredients, instructions = instructions))
        }
    }

    fun deleteDrink(drink: Drink) {
        viewModelScope.launch {
            repository.delete(drink)
        }
    }

    fun updateDrink(drink: Drink) {
        viewModelScope.launch {
            repository.update(drink)
        }
    }

    fun setRecentlyDeleted(drink: Drink?) {
        _recentlyDeleted.value = drink
    }

    fun undoDelete() {
        viewModelScope.launch {
            _recentlyDeleted.value?.let {
                repository.insert(it)
                _recentlyDeleted.value = null
            }
        }
    }

    fun deleteAllDrinks() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun observeDrinkByName(name: String) = repository.observeByName(name)
}
