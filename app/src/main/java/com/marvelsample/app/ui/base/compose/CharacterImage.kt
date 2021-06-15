package com.marvelsample.app.ui.base.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.imageloading.ImageLoadState
import com.marvelsample.app.R

@Composable
fun Image(
    imageUrl: String?,
    imageLabel: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val painter = rememberCoilPainter(imageUrl)
    Image(
        painter = painter,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        contentDescription = contentDescription ?: "$imageLabel image."
    )

    when (painter.loadState) {
        is ImageLoadState.Loading -> {
            // Display a circular progress indicator whilst loading
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
        is ImageLoadState.Error -> {
            // If you wish to display some content if the request fails
            Image(
                painter = painterResource(R.drawable.ic_baseline_face_24),
                contentDescription = contentDescription ?: "Image $imageLabel error image."
            )
        }
    }
}