package com.example.cal.presentation.recipes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cal.domain.DataError
import com.example.cal.domain.Error

@Composable
fun ErrorView(
    error: Error,
    modifier: Modifier = Modifier,
    errorCtaClicked: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var title = "error"
        var message = "oops something went wrong"
        when (error) {
            is DataError.Network -> {
                title = "Network error"
                message = "check your internet connection and try again"
            }

            is DataError.Crypto -> {
                title = "Crypto error"
                message = "we could not encrypt your data"
            }
        }
        Text(
            text = title,
            fontSize = 32.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        CalAppButton("Retry",
            fontSize = 18.sp,
            onClicked = {
                errorCtaClicked.invoke()
            })
    }
}