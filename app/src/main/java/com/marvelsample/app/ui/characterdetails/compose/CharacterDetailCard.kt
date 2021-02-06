package com.marvelsample.app.ui.characterdetails.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marvelsample.app.ui.characterdetails.CharacterModel

@Composable
fun CharacterDetailCard(
    modifier: Modifier = Modifier,
    character: CharacterModel
) {
    Column(modifier = modifier.padding(16.dp)) {
            Column {
                Text(character.name, style = MaterialTheme.typography.h5)
                character.description?.let { descriptionText ->
                    if (descriptionText.isNotBlank()) {
                        Text(
                            text = descriptionText,
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier.semantics {
                                contentDescription = "Character detail description"
                            })
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
    CharacterDetailCard(modifier = Modifier.fillMaxWidth(), character = character)
}

@Composable
@Preview
fun DefaultCharacterCardWithoutDescription() {
    val character = CharacterModel(
        "3-D Man",
        image = "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
    )
    CharacterDetailCard(modifier = Modifier.fillMaxWidth(), character = character)
}