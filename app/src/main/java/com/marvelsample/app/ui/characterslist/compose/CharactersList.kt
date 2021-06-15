package com.marvelsample.app.ui.characterslist.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.marvelsample.app.ui.base.compose.Loading
import com.marvelsample.app.ui.base.compose.MainTheme
import com.marvelsample.app.ui.characterslist.ListItem
import com.marvelsample.app.ui.utils.components.LazyPagingColumnFor
import kotlinx.coroutines.flow.Flow

@Composable
fun CharactersList(characters: Flow<PagingData<ListItem>>, navigation: (characterId: Int) -> Unit) {
    MainTheme {
        Surface(color = MaterialTheme.colors.surface) {
            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(text = "Characters")
                    })
                },
                content = {
                    CharactersListContent(characters = characters) { characterId: Int ->
                        navigation.invoke(characterId)
                    }
                }
            )
        }
    }
}

@Composable
private fun CharactersListContent(
    characters: Flow<PagingData<ListItem>>,
    modifier: Modifier = Modifier,
    onClick: (characterId: Int) -> Unit = {}
) {
    val lazyCharacters: LazyPagingItems<ListItem> = characters.collectAsLazyPagingItems()
    LazyPagingColumnFor(
        items = lazyCharacters,
        modifier = modifier.semantics {
            contentDescription = "Characters list"
        },
        itemContent = { _: Int, item: ListItem ->
            CharacterListItem(
                character = item,
                onClick = { characterId ->
                    onClick.invoke(characterId)
                }
            )
        },
        gridContent = {
            lazyCharacters.apply {
                when {
                    loadState.refresh is LoadState.Loading -> {
                        Loading(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    loadState.append is LoadState.Loading -> {
                        Loading(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }
                    loadState.refresh is LoadState.Error -> {
                        val error = lazyCharacters.loadState.refresh as LoadState.Error
                        ErrorList(
                            modifier = Modifier
                                .fillMaxSize(),
                            error = error
                        )
                    }
                    loadState.append is LoadState.Error -> {
                        val error = lazyCharacters.loadState.append as LoadState.Error
                        ErrorList(
                            modifier = Modifier
                                .fillMaxSize(),
                            error = error
                        )
                    }
                }
            }
        })
}