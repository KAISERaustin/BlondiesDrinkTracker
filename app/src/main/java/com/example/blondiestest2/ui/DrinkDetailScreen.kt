package com.example.blondiestest2.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.blondiestest2.data.local.Drink
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DrinkDetailScreen(
    drinkName: String,
    navController: NavController,
    viewModel: DrinkViewModel = hiltViewModel()
) {
    val drink by viewModel.observeDrinkByName(drinkName).collectAsState(initial = null)

    if (drink == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val drinkValue = drink!!

    var editing       by remember { mutableStateOf<Drink?>(null) }
    var confirmDelete by remember { mutableStateOf(false) }
    var name         by remember { mutableStateOf(drinkValue.name) }
    var ingredients  by remember { mutableStateOf(drinkValue.ingredients) }
    var instructions by remember { mutableStateOf(drinkValue.instructions) }

    LaunchedEffect(editing) {
        editing?.let {
            name         = it.name
            ingredients  = it.ingredients
            instructions = it.instructions
        }
    }

    // --- Edit Dialog (themed) ---
    if (editing != null) {
        AlertDialog(
            onDismissRequest = { editing = null },
            containerColor   = MaterialTheme.colorScheme.surface,
            tonalElevation   = 4.dp,
            shape            = MaterialTheme.shapes.medium,
            title            = { Text("Edit Drink") },
            text             = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = ingredients,
                        onValueChange = { ingredients = it },
                        label = { Text("Ingredients") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = instructions,
                        onValueChange = { instructions = it },
                        label = { Text("Instructions") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.updateDrink(
                        editing!!.copy(
                            name = name,
                            ingredients = ingredients,
                            instructions = instructions
                        )
                    )
                    editing = null
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { editing = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // --- Delete Confirmation Dialog ---
    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            containerColor   = MaterialTheme.colorScheme.surface,
            tonalElevation   = 4.dp,
            shape            = MaterialTheme.shapes.medium,
            title            = { Text("Delete '${drinkValue.name}'?") },
            text             = { Text("This action cannot be undone.") },
            confirmButton   = {
                TextButton(onClick = {
                    viewModel.setRecentlyDeleted(drinkValue)
                    viewModel.deleteDrink(drinkValue)
                    confirmDelete = false
                    navController.popBackStack()
                }) {
                    Text("Delete")
                }
            },
            dismissButton   = {
                TextButton(onClick = { confirmDelete = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Ingredients", style = MaterialTheme.typography.titleMedium)
                Card(
                    colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape     = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        drinkValue.ingredients
                            .split(",", "\n", ";")
                            .map(String::trim)
                            .filter(String::isNotEmpty)
                            .forEach { ing ->
                                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                                    Text("•", style = MaterialTheme.typography.bodyMedium)
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = ing,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                    }
                }

                Text("Instructions", style = MaterialTheme.typography.titleMedium)
                Card(
                    colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape     = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        drinkValue.instructions
                            .split("\n", ";")
                            .map(String::trim)
                            .filter(String::isNotEmpty)
                            .forEach { step ->
                                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                                    Text("•", style = MaterialTheme.typography.bodyMedium)
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = step,
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                    }
                }
            }

            // --- Outlined Card for icon buttons (Edit, Delete, Back) ---
            Card(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .border(
                        width = 2.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape     = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { editing = drinkValue }) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit drink")
                    }
                    IconButton(onClick = { confirmDelete = true }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete drink")
                    }
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Go back")
                    }
                }
            }
        }
    }
}
