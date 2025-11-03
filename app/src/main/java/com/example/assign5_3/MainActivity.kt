package com.example.assign5_3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.assign5_3.ui.theme.Assign5_3Theme
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assign5_3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun HomeScreen(recipes: List<Recipe>, modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Recipes", style = MaterialTheme.typography.headlineMedium)
        LazyColumn {
            items(recipes.size) {index ->
                Text(text = recipes[index].title)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val recipes = listOf(
        Recipe(
            "Recipe 1",
            listOf("Ingredient 1", "Ingredient 2"),
            listOf("Step 1", "Step 2")
        ),
        Recipe(
            "Recipe 2",
            listOf("Ingredient 3", "Ingredient 4"),
            listOf("Step 3", "Step 4")
        ),
        Recipe(
            "Recipe 3",
            listOf("Ingredient 5", "Ingredient 6"),
            listOf("Step 5", "Step 6")
        ),
        Recipe(
            "Recipe 4",
            listOf("Ingredient 7", "Ingredient 8"),
            listOf("Step 7", "Step 8")
        ),
        Recipe(
            "Recipe 5",
            listOf("Ingredient 9", "Ingredient 10"),
            listOf("Step 9", "Step 10")
        )
    )
    Assign5_3Theme {
        HomeScreen(
            recipes = recipes, modifier = Modifier.fillMaxSize()
        )
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
    Assign5_3Theme {
        Greeting("Android")
    }
}