package com.example.assign5_3

import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
    data object ViewRecipe : Screen("viewRecipe/{recipeId}", "View Recipe", Icons.Default.RemoveRedEye) {
        // Helper function to create the correct route for a specific recipe
        fun createRoute(recipeId: String) = "viewRecipe/$recipeId"
    }
    data object AddRecipe : Screen("addRecipe", "Add Recipe", Icons.Default.Add)
    data object Settings: Screen("settings", "Settings", Icons.Default.Settings)
}

val screens = listOf(
    Screen.Home,
    Screen.ViewRecipe,
    Screen.AddRecipe,
    Screen.Settings
)

val bottomBarScreens = listOf(
    Screen.Home,
    Screen.AddRecipe,
    Screen.Settings
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
            composable(Screen.Home.route) { HomeScreen(
                recipes = recipes,
                onRecipeClick = { recipeId ->
                    // Use the helper function to navigate
                    navController.navigate(Screen.ViewRecipe.createRoute(recipeId))
                },
                modifier = Modifier.fillMaxSize()
            ) }
            composable(
                route = Screen.ViewRecipe.route,
                arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
            ) { backStackEntry ->
                // 3. READ the argument from the backStackEntry
                val recipeId = backStackEntry.arguments?.getString("recipeId")
                val recipe = recipes.find { it.title == recipeId }

                if (recipe != null) {
                    ViewRecipeScreen(recipe = recipe, modifier = Modifier.fillMaxSize())
                } else {
                    GenericScreen(screenName = "Recipe not found")
                }
            }
            composable(Screen.AddRecipe.route) { AddRecipeScreen(vm = viewModel, navController = navController, modifier = Modifier.padding(horizontal=8.dp)) }
            composable(Screen.Settings.route) { GenericScreen(screenName = Screen.Settings.title) }
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
        bottomBarScreens.forEach { screen ->
            NavigationBarItem(
                label = { Text(screen.title) },
                icon = { Icon(screen.icon, contentDescription = screen.title) },

                // Check if the current destination's route matches this screen's route
                selected = currentDestination?.hierarchy?.any { dest ->
                    dest.route?.startsWith(screen.route.substringBefore("/{")) == true
                } == true,

                onClick = {
                    val baseRoute = screen.route.substringBefore("/{")

                    // Only navigate if we’re not already on that base route
                    val currentBaseRoute = currentDestination?.route?.substringBefore("/")
                    if (currentBaseRoute != baseRoute) {
                        navController.navigate(baseRoute) {
                            popUpTo(0) { inclusive = false } // clear stack safely
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }

            )
        }
    }
}

@Composable
fun HomeScreen(
    recipes: List<Recipe>,
    onRecipeClick: (String) -> Unit, // Pass the recipe ID (title) on click
    modifier: Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Recipes", style = MaterialTheme.typography.headlineMedium)
        LazyColumn {
            items(recipes) { recipe ->
                Text(
                    text = recipe.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRecipeClick(recipe.title) }
                        .padding(16.dp) // Add padding for a better touch target
                )
            }
        }
    }
}

@Composable
fun ViewRecipeScreen(recipe: Recipe, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = recipe.title, style = MaterialTheme.typography.headlineMedium)
        Text(text = "Ingredients", style = MaterialTheme.typography.headlineSmall)
        recipe.ingredients.forEach { ingredient ->
            Text(text = "- $ingredient")
        }
        Text(text = "Instructions", style = MaterialTheme.typography.headlineSmall)
        recipe.instructions.forEach { instruction ->
            Text(text = "- $instruction")
        }
    }
}

@Composable
fun AddRecipeScreen(vm: MainViewModel, navController: NavController, modifier: Modifier = Modifier) {
    var title by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var isTitleEmpty by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Add a New Recipe", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // 2. Text field for the recipe title
        OutlinedTextField(
            value = title,
            onValueChange = {
                title = it
                isTitleEmpty = it.isBlank() // Check if the title is blank
            },
            label = { Text("Recipe Title") },
            modifier = Modifier.fillMaxWidth(),
            isError = isTitleEmpty, // Show error if title is empty
            singleLine = true
        )
        if (isTitleEmpty) {
            Text(
                text = "Title cannot be empty",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        // 3. Text field for ingredients
        OutlinedTextField(
            value = ingredients,
            onValueChange = { ingredients = it },
            label = { Text("Ingredients (one per line)") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Use weight to take up available space
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 4. Text field for instructions
        OutlinedTextField(
            value = instructions,
            onValueChange = { instructions = it },
            label = { Text("Instructions (one per line)") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))
        // 5. Save button
        Button(
            onClick = {
                isTitleEmpty = title.isBlank()
                if (!isTitleEmpty) {
                    // Create the new recipe object
                    val newRecipe = Recipe(
                        title = title.trim(),
                        // Split the strings by newline, trim whitespace, and filter out empty lines
                        ingredients = ingredients.lines().map { it.trim() }.filter { it.isNotEmpty() },
                        instructions = instructions.lines().map { it.trim() }.filter { it.isNotEmpty() }
                    )
                    // Call the ViewModel to add the recipe
                    vm.addRecipe(newRecipe)
                    // Navigate back to the home screen
                    navController.navigate(Screen.Home.route) {
                        // Clear the back stack to prevent going back to the add screen
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            // Disable the button if the title is empty
            enabled = title.isNotBlank()
        ) {
            Text("SAVE RECIPE")
        }

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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val testRecipeList = listOf(
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
            recipes = testRecipeList, modifier = Modifier.fillMaxSize(), onRecipeClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ViewRecipeScreenPreview(){
    val testRecipeList = listOf(
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
    val recipe = testRecipeList[0]
    Assign5_3Theme {
        ViewRecipeScreen(recipe = recipe, modifier = Modifier.fillMaxSize())
    }
}