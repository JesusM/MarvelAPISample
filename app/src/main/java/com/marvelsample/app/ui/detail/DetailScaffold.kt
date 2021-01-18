package com.marvelsample.app.ui.detail

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.marvelsample.app.ui.base.compose.MainTheme

@Composable
fun ThemedScaffold(children: @Composable () -> Unit) {
    MainTheme {
        Surface(color = MaterialTheme.colors.primary) {
            children()
        }
    }
}