package com.example.cal.presentation.recipes

import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cal.RecipeScreen
import com.example.cal.domain.Recipe
import com.example.cal.presentation.recipes.components.ErrorView
import com.example.cal.presentation.recipes.components.Loader
import com.example.cal.presentation.recipes.components.RecipeItemComposable
import com.example.cal.utils.ObserveAsEvents

@Composable
fun RecipesListScreenRoot(
    navController: NavController,
    viewModel: RecipesViewModel = hiltViewModel(),
) {

    val context = LocalContext.current as FragmentActivity

    val biometricPrompt = BiometricPrompt(
        context,
        ContextCompat.getMainExecutor(context),
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                viewModel.onAction(RecipesListAction.EncryptMessage)
            }
        })

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(flow = viewModel.recipesListEvent) {
        when (it) {
            is RecipesListEvent.DisplayBioDialog -> {
                if (BiometricManager.from(context).canAuthenticate() == BiometricManager
                        .BIOMETRIC_SUCCESS
                ) {
                    biometricPrompt.authenticate(
                        BiometricPrompt.PromptInfo.Builder()
                            .setTitle("Biometric authentication")
                            .setSubtitle("Use your fingerprint or face to encrypt your data")
                            .setNegativeButtonText("Cancel")
                            .build()
                    )
                }
            }

            is RecipesListEvent.NavigateToRecipeScreen -> {
                navController.navigate(RecipeScreen(it.messageToEncrypt))
            }
        }
    }
    RecipesListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun RecipesListScreen(
    state: RecipesListState,
    onAction: (RecipesListAction) -> Unit
) {
    if (state.error != null) {
        ErrorView(error = state.error) {
            onAction(RecipesListAction.LoadRecipes)
        }
    }

    if (state.isLoading) {
        Loader()
    }

    if (state.recipes.isNotEmpty()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(state.recipes,
                key = { it.id!! }) {
                RecipeItemComposable(
                    recipe = it,
                    modifier = Modifier.clickable {
                        onAction(RecipesListAction.OnRecipeClicked(it))
                    }
                )
            }
        }
    } else {
//        display empty screen
    }
}

@Preview
@Composable
private fun RecipesListScreenPreview() {
    RecipesListScreen(
        state = RecipesListState(isLoading = false, recipes = listOf(Recipe(
            calories = "516 kcal",
            carbos = "47 g",
            country = "USA",
            description = "bla bla",
            difficulty = 0,
            fats = null,
            headline = null,
            id = "533143aaff604d567f8b4571",
            image = "https://img.hellofresh.com/f_auto,q_auto/hellofresh_s3/image/533143aaff604d567f8b4571.jpg",
            name = "Crispy Fish Goujons ",
            proteins = "43 g",
            thumb = "https://img.hellofresh.com/f_auto,q_auto,w_300/hellofresh_s3/image/533143aaff604d567f8b4571.jpg",
            time = "PT35M"
        )), error = null),
        onAction = {}
    )
}