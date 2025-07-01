package com.example.blondiestest2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.blondiestest2.ui.theme.BlondiesTest2Theme

import com.example.blondiestest2.ui.DrinkListScreen
import androidx.compose.foundation.layout.Box


import dagger.hilt.android.AndroidEntryPoint         // ①
import javax.inject.Inject                          // ②
import com.example.blondiestest2.data.local.DrinkDao // ③

@AndroidEntryPoint                                 // ④
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var drinkDao: DrinkDao                // ⑤

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // You can now use drinkDao inside your Activity
        // For example, launch a coroutine to read all drinks:
        // lifecycleScope.launch { drinkDao.getAll().collect { /* … */ } }

        setContent {
            BlondiesTest2Theme {
                Scaffold { innerPadding ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ) {
                        DrinkListScreen()
                    }
                }
            }
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BlondiesTest2Theme {
        Greeting("Android")
    }
}
