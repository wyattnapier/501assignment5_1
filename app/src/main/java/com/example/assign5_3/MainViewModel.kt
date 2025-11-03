package com.example.assign5_3

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(listOf(Recipe(
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
        )))
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    fun addRecipe(recipe: Recipe) {
        _recipes.value = _recipes.value + recipe
    }

    fun getRecipe(index: Int): Recipe {
        return _recipes.value[index]
    }
}