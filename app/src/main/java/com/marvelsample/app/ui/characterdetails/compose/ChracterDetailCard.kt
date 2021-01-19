package com.marvelsample.app.ui.characterdetails.compose

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marvelsample.app.R
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.ui.base.compose.Loading
import com.marvelsample.app.ui.base.model.Result
import com.marvelsample.app.ui.characterdetails.CharacterModel
import com.marvelsample.app.ui.characterslist.compose.CharacterImage

@Composable
fun CharacterDetailCard(characterResult: Result<CharacterModel>, modifier: Modifier) {
    Surface(modifier = Modifier.fillMaxWidth()) {
        when (characterResult) {
            Result.Loading -> {
                Loading(modifier = modifier)
            }
            is Result.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
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
                    CharacterImage(
                        imageUrl = character.image,
                        characterName = character.name,
                        modifier = Modifier.height(dimensionResource(R.dimen.collection_item_detail_height)),
                    )
                    Column(modifier = modifier) {
                        Text(character.name, style = MaterialTheme.typography.h4)
                        Text(character.description ?: "", style = MaterialTheme.typography.body1)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun DefaultCharacterCard() {
    val character = CharacterModel(
        "3-D Man",
        "3-D Man Description",
        "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
    )
    CharacterDetailCard(Result.Success(character), Modifier.padding(16.dp))
}