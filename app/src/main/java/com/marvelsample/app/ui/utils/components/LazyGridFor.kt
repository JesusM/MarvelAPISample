package com.marvelsample.app.ui.utils.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed

/**
 * TODO: For now, current compose paging and lazy grid APIs don't seem to allow a proper way to bind
 *  lazy items into a grid. Therefore, using a list instead but keeping code (commented out) to
 *  wait for updates.
 */
@Composable
fun <T : Any> LazyPagingGridFor(
    items: LazyPagingItems<T>,
    modifier: Modifier,
//    cols: Int = 3,
    itemContent: @Composable LazyItemScope.(index: Int, value: T) -> Unit,
    gridContent: @Composable LazyItemScope.() -> Unit = { },
) {
//    val chunkedList = items.snapshot().chunked(cols)
//    LazyVerticalGrid(
//        cells = GridCells.Fixed(cols)
//    )
//    {
//        itemsGridIndexed(items, itemContent = itemContent)
////        gridContent
//    }

    // For now, current compose paging and lazy grid APIs don't seem to allow a proper way to bind
    // lazy items into a grid. Therefore, using a list instead but keeping code (commented out) to
    // wait for updates.
    //
    LazyColumn(modifier = modifier) {
        itemsIndexed(items) { index, item ->
            item?.let {
                itemContent.invoke(this, index, it)
            }
        }
        item {
            gridContent.invoke(this)
        }
    }
}