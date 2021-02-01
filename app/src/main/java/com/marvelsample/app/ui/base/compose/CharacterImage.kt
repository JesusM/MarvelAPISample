package com.marvelsample.app.ui.characterslist.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import com.marvelsample.app.R
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun CharacterImage(
    imageUrl: String?,
    characterName: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    CoilImage(
        data = imageUrl ?: R.drawable.ic_baseline_face_24,
        modifier = modifier,
        fadeIn = true,
        contentScale = ContentScale.Crop,
        loading = {
            Box(Modifier.fillMaxWidth()) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        },
        error = {
            Image(
                imageVector = vectorResource(R.drawable.ic_baseline_face_24),
                contentDescription = contentDescription ?: "Character $characterName error image."
            )
        },
        contentDescription = contentDescription ?: "Character $characterName image."
    )
}