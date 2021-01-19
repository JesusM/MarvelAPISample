package com.marvelsample.app.ui.characterslist.compose

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.marvelsample.app.ui.base.compose.Loading
import com.marvelsample.app.ui.characterslist.ListItem
import com.marvelsample.app.ui.utils.components.LazyPagingGridFor
import kotlinx.coroutines.flow.Flow

@Composable
fun CharactersList(
    characters: Flow<PagingData<ListItem>>,
    modifier: Modifier = Modifier,
    onClick: (characterId: Int) -> Unit = {}
) {
    val lazyCharacters: LazyPagingItems<ListItem> = characters.collectAsLazyPagingItems()

    LazyPagingGridFor(
        items = lazyCharacters,
        modifier = modifier,
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
                        val error = lazyCharacters.loadState.refresh as LoadState.Error
                        ErrorList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(), error = error
                        )
                    }
                    loadState.append is LoadState.Error -> {
                        val error = lazyCharacters.loadState.append as LoadState.Error
                        ErrorList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(), error = error
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