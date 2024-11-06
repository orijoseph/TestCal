package com.example.cal.data.network

import com.example.cal.data.RecipeResponseBase
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("android-test/recipes.json")
    suspend fun getRecipes(): Response<RecipeResponseBase>
}