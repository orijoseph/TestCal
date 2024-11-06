package com.example.cal.presentation.recipes.recipe

import android.graphics.BlurMaskFilter
import android.text.TextPaint
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cal.R
import com.example.cal.presentation.recipes.RecipesListAction
import com.example.cal.utils.ObserveAsEvents

@Composable
fun RecipeScreenRoot(
    encryptedMessage: String,
    navController: NavController,
    viewModel: RecipeViewModel = hiltViewModel(),
) {

    val context = LocalContext.current as FragmentActivity

    val biometricPrompt = BiometricPrompt(
        context,
        ContextCompat.getMainExecutor(context),
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                viewModel.onAction(RecipeAction.BioAuthFinished)
            }
        })

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        viewModel.recipeEvent.collect {
            when (it) {
                is RecipeEvent.DisplayBioDialog -> {
                    if (BiometricManager.from(context).canAuthenticate() == BiometricManager
                            .BIOMETRIC_SUCCESS
                    ) {
                        biometricPrompt.authenticate(
                            BiometricPrompt.PromptInfo.Builder()
                                .setTitle("Biometric authentication")
                                .setSubtitle("Use your fingerprint or face to decrypt your data")
                                .setNegativeButtonText("Cancel")
                                .build()
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.decrypt(encryptedMessage)
    }

    RecipeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun RecipeScreen(
    state: RecipeState,
    onAction: (RecipeAction) -> Unit
) {
    if (!state.hasAccess) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painterResource(id = R.drawable.ic_lock),
                contentDescription = null
            )
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val hasAccess = state.hasAccess
        val recipe = state.recipe
        if (hasAccess && recipe != null) {
            state.recipe.name?.let {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            }
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .aspectRatio(16 / 9f),
                model = state.recipe.image,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
            state.recipe.fats?.let {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            }
            state.recipe.calories?.let {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            }
            state.recipe.carbos?.let {
                Text(
                    text = it,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            }
            state.recipe.description?.let {
                Text(
                    text = state.recipe.description,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}
