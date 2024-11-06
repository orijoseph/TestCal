package com.example.cal.presentation.recipes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalAppButton(
    text: String,
    fontSize: TextUnit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClicked: () -> Unit
) {
    Button(
        onClick = onClicked,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Red,
            contentColor = Color.White,
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize
            )
        }
    }
}

@Preview
@Composable
fun MovieAppButtonPreview(modifier: Modifier = Modifier) {
    CalAppButton(text = "Retry", fontSize = 22.sp) {
    }
}