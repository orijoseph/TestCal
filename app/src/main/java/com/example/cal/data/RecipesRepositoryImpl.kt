package com.example.cal.data

import com.example.cal.domain.Error
import com.example.cal.domain.Recipe
import com.example.cal.domain.RecipeRepository
import com.example.cal.domain.Result
import com.example.cal.domain.network.ApiClient
import javax.inject.Inject

class RecipesRepositoryImpl @Inject constructor(
    private val apiService: ApiClient
): RecipeRepository {

    override suspend fun getRecipes(): Result<List<Recipe>, Error> {
        return apiService.getRecipes()
    }
}
