package com.marvelsample.app.ui.characterslist.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.marvelsample.app.ui.characterslist.ListItem
import com.marvelsample.app.ui.detail.DetailActivity
import com.marvelsample.app.ui.utils.LazyPagingGridFor
import kotlinx.coroutines.flow.Flow

@ExperimentalFoundationApi
@Composable
fun CharactersList(characters: Flow<PagingData<ListItem>>, context: Context) {
    val lazyCharacters: LazyPagingItems<ListItem> = characters.collectAsLazyPagingItems()

    LazyPagingGridFor(items = lazyCharacters, itemContent = { _: Int, item: ListItem ->
        CharacterListItem(
            character = item,
            onClick = { characterId ->
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.ITEM_ID_ARG, characterId)
                context.startActivity(intent)
            }
        )
    }, gridContent = {
        lazyCharacters.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    LoadingList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )
                }
                loadState.append is LoadState.Loading -> {
                    LoadingList(
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