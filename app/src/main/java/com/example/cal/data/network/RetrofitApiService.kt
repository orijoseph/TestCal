package com.example.cal.data.network

import com.example.cal.data.RecipeResponseItem
import com.example.cal.domain.DataError
import com.example.cal.domain.Error
import com.example.cal.domain.Recipe
import com.example.cal.domain.Result
import com.example.cal.domain.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitApiService @Inject constructor(
    private val apiService: ApiService
) : ApiClient {

    override suspend fun getRecipes(): Result<List<Recipe>, Error> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getRecipes()
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val recipes = responseBody?.mapNotNull {
                        it.toRecipe()
                    }
                    Result.Success(recipes?: emptyList())
                } else {
                    Result.Error(DataError.Network.UNKNOWN)
                }
            } catch (e: Exception) {
                Result.Error(DataError.Network.SERVER_ERROR)
            }
        }
    }
}

fun RecipeResponseItem.toRecipe(): Recipe {
    return Recipe(
        calories = calories,
        carbos = carbos,
        country = country,
        description = description,
        difficulty = difficulty,
        fats = fats,
        headline = headline,
        id = id,
        image = image,
        name = name,
        proteins = proteins,
        thumb = thumb,
        time = time
    )
}
