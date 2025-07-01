package com.example.blondiestest2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.blondiestest2.data.local.Drink

@Composable
fun DrinkListScreen(
    viewModel: DrinkViewModel = hiltViewModel()
) {
    val drinks by viewModel.drinks.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(
            onClick = { viewModel.addSample() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Sample Drink")
        }

        Spacer(Modifier.height(16.dp))

        if (drinks.isEmpty()) {
            Text(
                "No drinks yet",
                style = MaterialTheme.typography.bodyMedium
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(drinks) { drink ->
                    DrinkItem(drink)
                }
            }
        }
    }
}

@Composable
private fun DrinkItem(drink: Drink) {
    Text(
        text = "${drink.id}: ${drink.name}",
        style = MaterialTheme.typography.bodyLarge
    )
}
