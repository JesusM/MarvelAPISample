package com.marvelsample.app.ui.characterdetails.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.marvelsample.app.core.model.base.error.ResourceError

@Composable
fun DetailsError(
    error: ResourceError
) {
    Column(
        modifier = Modifier
            .semantics {
                contentDescription = "Error message"
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val text = when (error) {
            ResourceError.EmptyContent -> {
                "No content"
            }
            is ResourceError.RequestFailError -> {
                "Request failed ${error.errorMessage}"
            }
            ResourceError.NoNetworkError -> {
                "No network"
            }
        }
        Text(text = text, style = MaterialTheme.typography.body1)
    }
}

@Composable
@Preview
fun DetailsErrorPreview() {
    DetailsError(ResourceError.EmptyContent)
}