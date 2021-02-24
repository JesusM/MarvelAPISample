package com.marvelsample.app.ui.base.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.marvelsample.app.R
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun Image(
    imageUrl: String?,
    imageLabel: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    CoilImage(
        data = imageUrl ?: R.drawable.ic_baseline_face_24,
        modifier = modifier,
        contentScale = ContentScale.Crop,
        loading = {
            Box(Modifier.matchParentSize()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        },
        error = {
            Image(
                painter = painterResource(R.drawable.ic_baseline_face_24),
                contentDescription = contentDescription ?: "Image $imageLabel error image."
            )
        },
        contentDescription = contentDescription ?: "$imageLabel image."
    )
}