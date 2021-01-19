package com.marvelsample.app.ui.characterslist.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marvelsample.app.ui.characterslist.ListItem

@Composable
fun CharacterListItem(character: ListItem, onClick: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(3.dp),
        elevation = 6.dp,
        modifier = Modifier
            .clickable(onClick = {
                onClick(character.id)
            })
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            character.image?.let { image ->
                CharacterImage(
                    image,
                    character.name,
                    modifier = Modifier
                        .preferredSize(90.dp)
                )
            }
            CharacterTitle(
                character.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
        }
    }
}