package com.example.cal.domain.network

import com.example.cal.domain.Error
import com.example.cal.domain.Recipe
import com.example.cal.domain.Result

interface ApiClient {
    suspend fun getRecipes(): Result<List<Recipe>, Error>
}