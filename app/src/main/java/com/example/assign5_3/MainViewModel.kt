package com.example.assign5_3

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {
    private val _recipes = MutableStateFlow<List<Recipe>>(emptyList())
    val recipes: StateFlow<List<Recipe>> = _recipes.asStateFlow()

    fun addRecipe(recipe: Recipe) {
        _recipes.value = _recipes.value + recipe
    }

    fun getRecipe(index: Int): Recipe {
        return _recipes.value[index]
    }
}