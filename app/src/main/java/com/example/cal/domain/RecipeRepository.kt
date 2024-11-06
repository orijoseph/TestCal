package com.example.cal.domain

interface RecipeRepository {
    suspend fun getRecipes(): Result<List<Recipe>, Error>
}