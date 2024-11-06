package com.example.cal.presentation.recipes

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cal.domain.CryptoManager
import com.example.cal.domain.DataError
import com.example.cal.domain.Error
import com.example.cal.domain.Recipe
import com.example.cal.domain.RecipeRepository
import com.example.cal.domain.Result
import com.example.cal.utils.JsonHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import javax.inject.Inject


@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val cryptoManagerImpl: CryptoManager
) : ViewModel() {

    private lateinit var selectedRecipe: Recipe

    private val _state =
        MutableStateFlow(
            RecipesListState(
                recipes = listOf(),
                isLoading = false, error = null
            )
        )
    val state = _state.onStart {
        loadRecipes()
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000L),
        RecipesListState(
            recipes = listOf(),
            isLoading = false,
            error = null
        )
    )

    private val _recipesListEvent = MutableSharedFlow<RecipesListEvent>()
    val recipesListEvent = _recipesListEvent.asSharedFlow()

    private fun loadRecipes() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val recipesState = async { recipeRepository.getRecipes() }
            when (val recipes = recipesState.await()) {
                is Result.Error -> {
                    _state.update { it.copy(error = recipes.error, isLoading = false) }
                }

                is Result.Success -> {
                    _state.update {
                        it.copy(
                            recipes = recipes.data,
                            error = null,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: RecipesListAction) {
        when (action) {
            is RecipesListAction.OnRecipeClicked -> {
                selectedRecipe = action.recipe
                emitEvent(RecipesListEvent.DisplayBioDialog(action.recipe))
            }

            is RecipesListAction.EncryptMessage -> {
                viewModelScope.launch {
                    val jsonRecipe = JsonHelper.convertRecipeToJsonWithMoshi(selectedRecipe)
                    val messageToEncrypt = cryptoManagerImpl.encryptMessage(jsonRecipe)
                    if (messageToEncrypt != null) {
                        emitEvent(RecipesListEvent.NavigateToRecipeScreen(EncryptedData(text = messageToEncrypt)))
                    } else {
                        _state.update {
                            it.copy(
                                error = DataError.Crypto.ENCRYPTION,
                                isLoading = false
                            )
                        }
                    }
                }
            }

            is RecipesListAction.LoadRecipes -> {
                loadRecipes()
            }
        }
    }

    private fun emitEvent(event: RecipesListEvent) {
        viewModelScope.launch {
            _recipesListEvent.emit(event)
        }
    }
}

data class RecipesListState(
    val isLoading: Boolean,
    val recipes: List<Recipe>,
    val error: Error?
)

sealed interface RecipesListAction {
    data class OnRecipeClicked(val recipe: Recipe) : RecipesListAction
    data object EncryptMessage : RecipesListAction
    data object LoadRecipes : RecipesListAction
}

sealed interface RecipesListEvent {
    data class DisplayBioDialog(val recipe: Recipe) : RecipesListEvent
    data class NavigateToRecipeScreen(val messageToEncrypt: EncryptedData) : RecipesListEvent
}

@Parcelize
@Serializable
data class EncryptedData(val text: String) : Parcelable
