package com.example.cal

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.cal.presentation.recipes.EncryptedData
import com.example.cal.presentation.recipes.RecipesListScreenRoot
import com.example.cal.presentation.recipes.recipe.RecipeScreenRoot
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf


@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = RecipeList
            ) {
                composable<RecipeList> {
                    RecipesListScreenRoot(navController = navController)
                }

                composable<RecipeScreen>(
                    typeMap = mapOf(typeOf<EncryptedData>() to RecipeParametersType)
                ) {
                    val encryptedMessage = it.toRoute<RecipeScreen>().encryptedData
                    RecipeScreenRoot(encryptedMessage = encryptedMessage.text, navController = navController)
                }
            }
        }
    }
}


@Serializable
data object RecipeList

@Serializable
data class RecipeScreen(val encryptedData: EncryptedData)

val RecipeParametersType = object : NavType<EncryptedData>(
    isNullableAllowed = false
) {
    override fun put(bundle: Bundle, key: String, value: EncryptedData) {
        bundle.putParcelable(key, value)
    }

    override fun get(bundle: Bundle, key: String): EncryptedData? {
        return bundle.getParcelable(key) as EncryptedData?
    }

    override fun serializeAsValue(value: EncryptedData): String {
        return Uri.encode(Json.encodeToString(value))
    }

    override fun parseValue(value: String): EncryptedData {
        return Json.decodeFromString<EncryptedData>(value)
    }
}