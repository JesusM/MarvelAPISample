package com.marvelsample.app.ui.characterdetails.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.paging.PagingData
import com.marvelsample.app.R
import com.marvelsample.app.ui.base.compose.Image
import com.marvelsample.app.ui.base.compose.Loading
import com.marvelsample.app.ui.base.model.Result
import com.marvelsample.app.ui.characterdetails.CharacterModel
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem
import com.marvelsample.app.ui.characterdetails.comics.compose.CharacterComics
import kotlinx.coroutines.flow.Flow
import kotlin.math.max
import kotlin.math.min

private val CollapsedImageSize = 50.dp

@Composable
fun CharacterDetailBody(
    characterResult: Result<CharacterModel>,
    comics: Flow<PagingData<ComicListItem>>,
    upPress: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (characterResult) {
            Result.Loading -> {
                Loading(modifier = Modifier.align(Alignment.Center))
            }
            is Result.Error -> {
                DetailsError(error = characterResult.error)
            }
            is Result.Success -> {
                val scroll = rememberScrollState(0)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scroll)
                ) {
                    val initialImageMaxSize =
                        dimensionResource(R.dimen.collection_item_detail_height)
                    val collapseRange = with(LocalDensity.current) { (initialImageMaxSize).toPx() }
                    val collapseFraction = (scroll.value / collapseRange).coerceIn(0f, 1f)

                    CollapsingImageLayout(
                        collapseFraction = collapseFraction,
                        modifier = Modifier,
                        initialImageMaxSize = initialImageMaxSize
                    ) {
                        Image(
                            imageUrl = characterResult.result.image,
                            imageLabel = characterResult.result.name,
                            modifier = Modifier.height(initialImageMaxSize),
                            contentDescription = "Character detail image"
                        )
                    }
                    Surface {
                        Column {
                            CharacterDetailCard(
                                character = characterResult.result
                            )
                            CharacterComics(
                                comics = comics,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
        Up(upPress)
    }
}

@Composable
private fun CollapsingImageLayout(
    collapseFraction: Float,
    modifier: Modifier = Modifier,
    initialImageMaxSize: Dp,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)
        val imageMaxHeight = min(initialImageMaxSize.toPx().toInt(), constraints.maxWidth)
        val imageMinHeight = max(CollapsedImageSize.toPx().toInt(), constraints.minWidth)
        val imageHeight =
            lerp(imageMaxHeight.toDp(), imageMinHeight.toDp(), collapseFraction).toPx().toInt()
        val imagePlaceable =
            measurables[0].measure(Constraints.fixed(constraints.maxWidth, imageHeight))

        val imageY = lerp(0.dp, imageMaxHeight.toDp(), collapseFraction).toPx().toInt()
        layout(
            width = constraints.maxWidth,
            height = imageHeight
        ) {
            imagePlaceable.place(0, imageY)
        }
    }
}