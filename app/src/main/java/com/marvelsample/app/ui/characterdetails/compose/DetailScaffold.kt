package com.marvelsample.app.ui.characterdetails.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.marvelsample.app.ui.base.compose.MainTheme

@Composable
fun DetailScaffold(children: @Composable () -> Unit) {
    MainTheme {
        Surface(color = MaterialTheme.colors.surface) {
            children()
        }
    }
}