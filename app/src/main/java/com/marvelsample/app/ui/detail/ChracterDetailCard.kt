package com.marvelsample.app.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marvelsample.app.R
import com.marvelsample.app.core.repository.model.ExternalCollection
import com.marvelsample.app.core.repository.model.Thumbnail
import com.marvelsample.app.core.repository.model.base.error.ResourceError
import com.marvelsample.app.core.repository.model.characters.Character
import com.marvelsample.app.core.repository.model.fullPath
import com.marvelsample.app.ui.base.model.Result
import dev.chrisbanes.accompanist.coil.CoilImage


@Composable
fun CharacterDetailCard(characterResult: Result<Character>, modifier: Modifier) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        when (characterResult) {
            Result.Loading -> {
                Column(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }
            is Result.Error -> {
                Column(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val text = when (characterResult.error) {
                        ResourceError.EmptyContent -> {
                            "No content"
                        }
                        is ResourceError.RequestFailError -> {
                            "Request failed ${characterResult.error.errorMessage}"
                        }
                        ResourceError.NoNetworkError -> {
                            "No network"
                        }
                    }
                    Text(text = text, style = MaterialTheme.typography.body1)
                }
            }
            is Result.Success -> {
                val character = characterResult.result
                Column {
                    CoilImage(
                        modifier = Modifier.height(dimensionResource(R.dimen.collection_item_detail_height)),
                        data = character.thumbnail.fullPath(),
                        contentScale = ContentScale.Crop,
                        fadeIn = true,
                        loading = {
                            Box(Modifier.fillMaxWidth()) {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }
                        },
                        error = {
                            Image(bitmap = imageResource(R.drawable.ic_launcher_background))
                        }
                    )
                    Column(modifier = modifier) {
                        Text(character.name, style = MaterialTheme.typography.h4)
                        Text(character.description, style = MaterialTheme.typography.body1)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun DefaultCharacterCard() {
    val externalCollection = ExternalCollection(
        available = 1,
        collectionURI = "",
        externalItems = emptyList(),
        returned = 0
    )
    val character = Character(
        externalCollection,
        description = "3-D Man",
        events = externalCollection,
        id = 1,
        modified = "",
        name = "Character preview",
        resourceURI = "",
        series = externalCollection,
        stories = externalCollection,
        thumbnail = Thumbnail("jpg", "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784"),
        urls = emptyList()
    )
    CharacterDetailCard(Result.Success(character), Modifier.padding(16.dp))
}