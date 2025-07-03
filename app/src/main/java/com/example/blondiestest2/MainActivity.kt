package com.example.blondiestest2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.blondiestest2.ui.DrinkDetailScreen
import com.example.blondiestest2.ui.DrinkListScreen
import com.example.blondiestest2.ui.DrinkViewModel
import com.example.blondiestest2.ui.theme.BlondiesTest2Theme
import com.example.blondiestest2.ui.theme.LocalUiScale
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 👉 FIX: Get your view model instance
        val viewModel: DrinkViewModel by viewModels()

        setContent {
            BoxWithConstraints {
                val screenDp = maxWidth.value
                val baseDp   = 412f
                val scale    = (screenDp / baseDp).coerceIn(1f, 2f)

                CompositionLocalProvider(LocalUiScale provides scale) {
                    BlondiesTest2Theme {
                        val navController = rememberNavController()

                        Scaffold(
                            modifier = Modifier
                                .statusBarsPadding()
                                .navigationBarsPadding(),
                            topBar = {
                                Surface(
                                    color = MaterialTheme.colorScheme.primary,
                                    shadowElevation = 0.dp
                                ) {
                                    TopAppBar(
                                        title = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.DateRange,
                                                    contentDescription = "App icon",
                                                    tint = MaterialTheme.colorScheme.onPrimary
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "Drink Tracker",
                                                    color = MaterialTheme.colorScheme.onPrimary
                                                )
                                            }
                                        },
                                        colors = TopAppBarDefaults.topAppBarColors(
                                            containerColor    = MaterialTheme.colorScheme.primary,
                                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }
                        ) { innerPadding ->
                            NavHost(
                                navController   = navController,
                                startDestination = "list",
                                modifier         = Modifier.padding(innerPadding)
                            ) {
                                composable("list") {
                                    DrinkListScreen(
                                        onDrinkSelected = { selectedName ->
                                            navController.navigate("detail/$selectedName")
                                        },
                                        viewModel = viewModel  // <-- FIXED!
                                    )
                                }
                                composable("detail/{drinkName}") { backStackEntry ->
                                    val name = backStackEntry
                                        .arguments
                                        ?.getString("drinkName")
                                        .orEmpty()
                                    DrinkDetailScreen(
                                        drinkName = name,
                                        navController = navController
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
