package com.example.blondiestest2.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.blondiestest2.data.DrinkRepository
import com.example.blondiestest2.data.local.Drink
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrinkViewModel @Inject constructor(
    private val repository: DrinkRepository
) : ViewModel() {
    val drinks: StateFlow<List<Drink>> =
        repository.allDrinks()
            .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun addSample() {
        viewModelScope.launch {
            repository.add(Drink(name = "Espresso", ingredients = "Coffee, Water", instructions = "Brew."))
        }
    }
}
