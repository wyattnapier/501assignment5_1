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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.assign5_3.ui.theme.Assign5_3Theme


class MainActivity : ComponentActivity() {
    private val vm: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assign5_3Theme {
                MainScreen(vm)
            }
        }
    }
}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object ViewRecipe : Screen("viewRecipe", "View Recipe", Icons.Default.Person)
    data object AddRecipe : Screen("addRecipe", "Add Recipe", Icons.Default.Favorite)
}

val screens = listOf(
    Screen.Home,
    Screen.ViewRecipe,
    Screen.AddRecipe
)

@Composable
fun MainScreen(viewModel: MainViewModel) {
    val navController = rememberNavController()
    val recipes = viewModel.recipes.collectAsState().value

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(recipes = recipes, modifier = Modifier.fillMaxSize()) }
            composable(Screen.ViewRecipe.route) { GenericScreen(screenName = Screen.ViewRecipe.title) }
            composable(Screen.AddRecipe.route) { GenericScreen(screenName = Screen.AddRecipe.title) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: androidx.navigation.NavController) {
    // We use the same list of screens defined globally
    NavigationBar {
        // Get the current back stack entry to determine the current route
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        // Create an item for each screen
        screens.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                icon = { Icon(screen.icon, contentDescription = screen.title) },

                // Check if the current destination's route matches this screen's route
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,

                // Handle the click event
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination to avoid building a stack
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Ensure only one copy of a destination is on the top of the back stack
                        launchSingleTop = true
                        // Restore state when re-navigating to a previously selected item
                        restoreState = true
                    }
                }
            )
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
fun GenericScreen(screenName: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = screenName, style = MaterialTheme.typography.headlineMedium)
    }
}