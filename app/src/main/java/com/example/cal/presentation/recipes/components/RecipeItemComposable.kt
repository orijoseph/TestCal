package com.example.cal.presentation.recipes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.example.cal.domain.Recipe

@Composable
fun RecipeItemComposable(
    recipe: Recipe,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {

        SubcomposeAsyncImage(
            model = recipe.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .aspectRatio(16 / 9f)
                .clip(RoundedCornerShape(15.dp)),
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            error = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = recipe.name ?: "",
            fontSize = 22.sp,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = ("Fats: " + recipe.fats),
            fontSize = 18.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Calories: " + recipe.calories,
            fontSize = 18.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Carbos: " + recipe.carbos,
            fontSize = 18.sp,
            color = Color.LightGray,
            fontWeight = FontWeight.Bold,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}