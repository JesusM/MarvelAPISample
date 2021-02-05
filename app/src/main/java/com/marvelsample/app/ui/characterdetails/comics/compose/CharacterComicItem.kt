package com.marvelsample.app.ui.characterdetails.comics.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.marvelsample.app.ui.base.compose.Image
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem

@Composable
fun CharacterComicListItem(item: ComicListItem, onClick: (String) -> Unit) {
    Card(
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .clearAndSetSemantics {
                contentDescription = "Character ${item.name} comic card."
            }
            .padding(start = 8.dp)
            .clickable(onClick = {
                onClick(item.id)
            })
    ) {
        Column(
            modifier = Modifier
                .width(120.dp)
                .aspectRatio(0.56f)
        ) {
            item.image?.let { image ->
                Image(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    imageUrl = image,
                    imageLabel = item.name
                )
            }
            Text(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 4.dp)
                    .fillMaxWidth(),
                text = item.name,
                maxLines = 1,
                style = MaterialTheme.typography.body2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(name = "Character comics item preview")
@Composable
fun CharacterComicListItemPreview() {
    val item = ComicListItem(
        "3",
        "Character's comic 1",
        "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
    )

    CharacterComicListItem(item) {}
}