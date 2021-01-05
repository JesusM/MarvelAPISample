package com.marvelsample.app.ui.detail

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.marvelsample.app.ui.base.compose.DetailsTheme

@Composable
fun DetailScaffold(children: @Composable () -> Unit) {
    DetailsTheme {
        Surface(color = MaterialTheme.colors.primary) {
            children()
        }
    }
}