package com.marvelsample.app.ui.characterdetails.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.PagingData
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
    up : () -> Unit
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
                val scroll = rememberScrollState(0f)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(scroll)
                ) {
                    CharacterDetailCard(character = characterResult.result)
                    CharacterComics(
                        comics = comics,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
        Up {
            up.invoke()
        }
    }
}