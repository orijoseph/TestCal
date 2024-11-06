package com.example.cal.utils

import com.example.cal.domain.Recipe
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object JsonHelper {

    fun convertRecipeToJsonWithMoshi(recipe: Recipe): String {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(Recipe::class.java)
        return jsonAdapter.toJson(recipe)
    }

    fun convertJsonToRecipe(jsonString: String): Recipe? {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter = moshi.adapter(Recipe::class.java)
        return jsonAdapter.fromJson(jsonString)
    }
}