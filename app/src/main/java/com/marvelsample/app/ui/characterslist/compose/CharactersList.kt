package com.marvelsample.app.ui.characterslist.compose

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.marvelsample.app.ui.utils.components.LazyPagingGridFor
import kotlinx.coroutines.flow.Flow

@Composable
fun CharactersList(characters: Flow<PagingData<ListItem>>, navigation : (characterId : Int) -> Unit) {
    MainTheme {
        Surface(color = MaterialTheme.colors.surface) {
            Scaffold(
                topBar = {
                    TopAppBar(title = {
                        Text(text = "Characters")
                    })
                },
                bodyContent = {
                    CharactersListContent(characters = characters) { characterId : Int ->
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

    LazyPagingGridFor(
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
                        Loading(modifier = Modifier)
                    }
                    loadState.append is LoadState.Loading -> {
                        Loading(modifier = Modifier)
                    }
                    loadState.refresh is LoadState.Error -> {
                        val error = lazyCharacters.loadState.refresh as LoadState.Error
                        ErrorList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            error = error
                        )
                    }
                    loadState.append is LoadState.Error -> {
                        val error = lazyCharacters.loadState.append as LoadState.Error
                        ErrorList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            error = error
                        )
                    }
                }
            }
        })

//    LazyColumn {
//        itemsIndexed(lazyCharacters) { _, item ->
//            if (item != null) {
//                CharacterListItem(
//                    character = item,
//                    onClick = { characterId ->
//                        val intent =
//                            Intent(context, DetailActivity::class.java)
//                        intent.putExtra(DetailActivity.ITEM_ID_ARG, characterId)
//                        context.startActivity(intent)
//                    }
//                )
//            }
//        }
//    }
}