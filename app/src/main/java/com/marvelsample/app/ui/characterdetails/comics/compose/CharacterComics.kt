package com.marvelsample.app.ui.characterdetails.comics.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.marvelsample.app.R
import com.marvelsample.app.ui.base.compose.Loading
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem
import com.marvelsample.app.ui.characterslist.compose.ErrorList
import com.marvelsample.app.ui.utils.components.LazyPagingRowFor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

@Composable
fun CharacterComics(
    comics: Flow<PagingData<ComicListItem>>,
    modifier: Modifier = Modifier
) {
    val lazyPagingItems: LazyPagingItems<ComicListItem> = comics.collectAsLazyPagingItems()

    Column(
        modifier = modifier
    ) {
        Text(
            modifier = modifier
                .padding(start = 16.dp)
                .semantics {
                    contentDescription = "Characters comics list header"
                },
            text = stringResource(id = R.string.comics),
            style = MaterialTheme.typography.h6,
        )

        LazyPagingRowFor(
            items = lazyPagingItems,
            modifier = modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .semantics {
                    contentDescription = "Character comics list"
                },
            itemContent = { _: Int, item: ComicListItem ->
                CharacterComicListItem(
                    item = item,
                    onClick = { comicId ->
                    }
                )
            },
            gridContent = {
                lazyPagingItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            Loading(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            )
                        }
                        loadState.append is LoadState.Loading -> {
                            Loading(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                            )
                        }
                        loadState.refresh is LoadState.Error -> {
                            val error = lazyPagingItems.loadState.refresh as LoadState.Error
                            ErrorList(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                error = error
                            )
                        }
                        loadState.append is LoadState.Error -> {
                            val error = lazyPagingItems.loadState.append as LoadState.Error
                            ErrorList(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                error = error
                            )
                        }
                    }
                }
            })
    }
}

@Preview(name = "Character comics preview")
@Composable
fun DefaultPreview() {
    val comics = listOf(
        ComicListItem(
            "3",
            "Character's comic 1",
            "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"
        )
    )

    CharacterComics(flowOf(PagingData.from(comics)), Modifier.fillMaxWidth())
}