package com.example.blondiestest2.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.blondiestest2.data.local.Drink
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DrinkListScreen(
    onDrinkSelected: (String) -> Unit,
    viewModel: DrinkViewModel
) {
    var query by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val drinks by viewModel.drinks.collectAsState()
    val filtered = drinks.filter { it.name.contains(query, ignoreCase = true) }
    val itemsToShow = if (query.isBlank()) drinks else filtered

    var editing by remember { mutableStateOf<Drink?>(null) }
    var deleteTarget by remember { mutableStateOf<Drink?>(null) }
    var adding by remember { mutableStateOf(false) }
    var isInEditMode by remember { mutableStateOf(false) }
    var isInDeleteMode by remember { mutableStateOf(false) }

    var name by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }

    LaunchedEffect(editing) {
        editing?.let {
            name = it.name
            ingredients = it.ingredients
            instructions = it.instructions
        }
    }
    LaunchedEffect(adding) {
        if (adding) {
            name = ""
            ingredients = ""
            instructions = ""
        }
    }

    val recentlyDeleted by viewModel.recentlyDeleted.collectAsState()

    // Snackbar setup
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Show snackbar on delete
    LaunchedEffect(recentlyDeleted) {
        recentlyDeleted?.let {
            val result = snackbarHostState.showSnackbar(
                message = "Drink deleted",
                actionLabel = "Undo",
                withDismissAction = true,
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.undoDelete()
            } else {
                viewModel.setRecentlyDeleted(null)
            }
        }
    }

    // Animated colors for mode card and icon buttons
    val modeCardColor by animateColorAsState(
        targetValue = when {
            isInEditMode -> MaterialTheme.colorScheme.primaryContainer
            isInDeleteMode -> MaterialTheme.colorScheme.errorContainer
            else -> MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(durationMillis = 400)
    )
    val editIconColor by animateColorAsState(
        if (isInEditMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(400)
    )
    val deleteIconColor by animateColorAsState(
        if (isInDeleteMode) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
        animationSpec = tween(400)
    )

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .widthIn(max = 600.dp)
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // Removed dev-only buttons here (Seed Sample & Delete All)

                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium),
                    placeholder = { Text("Search…") },
                    singleLine = true,
                    trailingIcon = {
                        if (query.isNotEmpty()) {
                            IconButton(onClick = { query = "" }) {
                                Icon(
                                    Icons.Outlined.Close,
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
                )

                Spacer(Modifier.height(8.dp))

                if (itemsToShow.isEmpty()) {
                    Text(
                        text = if (query.isBlank())
                            "No drinks yet. Tap “+” to add one."
                        else
                            "No matches for “$query”",
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(itemsToShow, key = { _, drink -> drink.name }) { _, drink ->
                            var visible by remember(drink) { mutableStateOf(true) }
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn(animationSpec = tween(400)),
                                exit = fadeOut(animationSpec = tween(400)),
                                modifier = Modifier.animateItem(
                                    fadeInSpec = null, fadeOutSpec = null, placementSpec = spring<IntOffset>(
                                        stiffness = Spring.StiffnessMediumLow,
                                        visibilityThreshold = IntOffset.VisibilityThreshold
                                    )
                                )
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            when {
                                                isInEditMode -> {
                                                    editing = drink
                                                    isInEditMode = false
                                                }
                                                isInDeleteMode -> {
                                                    visible = false
                                                    coroutineScope.launch {
                                                        kotlinx.coroutines.delay(400)
                                                        deleteTarget = drink
                                                        isInDeleteMode = false
                                                    }
                                                }
                                                else -> onDrinkSelected(drink.name)
                                            }
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isInEditMode || isInDeleteMode)
                                            MaterialTheme.colorScheme.surfaceVariant
                                        else
                                            MaterialTheme.colorScheme.surface
                                    ),
                                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text(
                                        text = "• ${drink.name}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                        item {
                            Spacer(Modifier.height(32.dp))
                        }
                    }
                }
            }

            // Animated Floating Card (mode icons) at bottom-end (RIGHT), STACKED
            Card(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = modeCardColor),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = {
                        isInEditMode = !isInEditMode
                        if (isInEditMode) isInDeleteMode = false
                    }) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "Toggle Edit Mode",
                            tint = editIconColor
                        )
                    }
                    IconButton(onClick = {
                        isInDeleteMode = !isInDeleteMode
                        if (isInDeleteMode) isInEditMode = false
                    }) {
                        Icon(
                            Icons.Outlined.Delete,
                            contentDescription = "Toggle Delete Mode",
                            tint = deleteIconColor
                        )
                    }
                    IconButton(onClick = { adding = true }) {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = "Add new drink"
                        )
                    }
                }
            }
        }
    }

    // Animated Add-new-drink dialog
    AnimatedVisibility(
        visible = adding,
        enter = fadeIn(animationSpec = tween(250)),
        exit = fadeOut(animationSpec = tween(250))
    ) {
        AlertDialog(
            onDismissRequest = { adding = false },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            shape = MaterialTheme.shapes.medium,
            title = { Text("Add New Drink") },
            text = {
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
                    viewModel.addDrink(name, ingredients, instructions)
                    adding = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { adding = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Animated Edit Dialog
    AnimatedVisibility(
        visible = editing != null,
        enter = fadeIn(animationSpec = tween(250)),
        exit = fadeOut(animationSpec = tween(250))
    ) {
        editing?.let {
            AlertDialog(
                onDismissRequest = { editing = null },
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp,
                shape = MaterialTheme.shapes.medium,
                title = { Text("Edit Drink") },
                text = {
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
    }

    // Delete Confirmation Dialog
    if (deleteTarget != null) {
        AlertDialog(
            onDismissRequest = { deleteTarget = null },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            shape = MaterialTheme.shapes.medium,
            title = { Text("Delete '${deleteTarget!!.name}'?") },
            text = { Text("This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setRecentlyDeleted(deleteTarget!!)
                    viewModel.deleteDrink(deleteTarget!!)
                    deleteTarget = null
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteTarget = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}
