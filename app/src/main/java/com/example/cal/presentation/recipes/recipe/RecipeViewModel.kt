package com.example.cal.presentation.recipes.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cal.domain.CryptoManager
import com.example.cal.domain.Error
import com.example.cal.domain.Recipe
import com.example.cal.utils.JsonHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val cryptoManagerImpl: CryptoManager
) : ViewModel() {

    private val _state =
        MutableStateFlow(RecipeState(hasAccess = false, recipe = null, error = null))
    val state = _state.asStateFlow()

    private val _recipeEvent = MutableSharedFlow<RecipeEvent>()
    val recipeEvent = _recipeEvent.asSharedFlow()

    fun onAction(action: RecipeAction) {
        when (action) {
            is RecipeAction.BioAuthFinished -> {
                _state.update { it.copy(hasAccess = true) }
            }
        }
    }

    fun decrypt(encryptedMessage: String) {
        val messageToDecrypt = cryptoManagerImpl.decryptMessage(encryptedMessage)
        val recipe = JsonHelper.convertJsonToRecipe(messageToDecrypt!!)
        _state.update { it.copy(recipe = recipe) }
        emitEvent(RecipeEvent.DisplayBioDialog)
    }

    private fun emitEvent(event: RecipeEvent) {
        viewModelScope.launch {
            _recipeEvent.emit(event)
        }
    }
}

data class RecipeState(
    val hasAccess: Boolean,
    val recipe: Recipe? = null,
    val error: Error?
)

sealed interface RecipeAction {
    data object BioAuthFinished : RecipeAction
}

sealed interface RecipeEvent {
    data object DisplayBioDialog : RecipeEvent
}