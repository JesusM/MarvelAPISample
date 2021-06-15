package com.marvelsample.app.ui.characterdetails.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.paging.PagingData
import com.marvelsample.app.R
import com.marvelsample.app.ui.base.compose.Image
import com.marvelsample.app.ui.base.compose.Loading
import com.marvelsample.app.ui.base.model.Result
import com.marvelsample.app.ui.characterdetails.CharacterModel
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem
import com.marvelsample.app.ui.characterdetails.comics.compose.CharacterComics
import kotlinx.coroutines.flow.Flow

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
                    Image(
                        imageUrl = characterResult.result.image,
                        imageLabel = characterResult.result.name,
                        modifier = Modifier
                            .height(initialImageMaxSize)
                            .fillMaxWidth(),
                        contentDescription = "Character detail image"
                    )
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
