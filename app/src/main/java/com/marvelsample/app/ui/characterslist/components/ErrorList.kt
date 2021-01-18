package com.marvelsample.app.ui.characterslist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState

@Composable
fun ErrorList(modifier: Modifier, error: LoadState.Error) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val text = error.error.localizedMessage ?: "Error"
        Text(text = text, style = MaterialTheme.typography.body1)
    }
}
