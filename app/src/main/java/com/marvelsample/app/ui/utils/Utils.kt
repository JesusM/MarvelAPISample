package com.marvelsample.app.ui.utils

import android.graphics.Bitmap
import android.view.View
import androidx.palette.graphics.Palette

fun Bitmap.paletteAsync(clearFilter: Boolean = false, f: (palette: Palette?) -> Unit) {
    var builder = Palette.from(this)
    if (clearFilter) {
        builder = builder.clearFilters()
    }
    builder.generate {
        f(it)
    }
}

fun <T : View> T.afterMeasured(f: T.() -> Unit) {
    if (width > 0) {
        f()
        return
    }

    addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
        override fun onLayoutChange(
            p0: View?,
            p1: Int,
            p2: Int,
            p3: Int,
            p4: Int,
            p5: Int,
            p6: Int,
            p7: Int,
            p8: Int
        ) {
            if (measuredWidth > 0 && measuredHeight > 0) {
                p0?.removeOnLayoutChangeListener(this)
                f()
            }
        }
    })
}

/**
 * Adds the [LazyPagingItems] and their content to the scope where the content of an item is
 * aware of its local index. The range from 0 (inclusive) to [LazyPagingItems.itemCount] (inclusive)
 * always represents the full range of presentable items, because every event from
 * [PagingDataDiffer] will trigger a recomposition.
 *
 * @sample androidx.paging.compose.samples.ItemsIndexedDemo
 *
 * @param lazyPagingItems the items received from a [Flow] of [PagingData].
 * @param itemContent the content displayed by a single item. In case the item is `null`, the
 * [itemContent] method should handle the logic of displaying a placeholder instead of the main
 * content displayed by an item which is not `null`.
 */
//fun <T : Any> LazyGridScope.itemsGridIndexed(
//    lazyPagingItems: LazyPagingItems<T>,
//    itemContent: @Composable LazyItemScope.(index: Int, value: T?) -> Unit
//) {
//    // this state recomposes every time the LazyPagingItems receives an update and changes the
//    // value of recomposerPlaceholder
//        @Suppress("UNUSED_VARIABLE")
//        val recomposerPlaceholder = lazyPagingItems.recomposerPlaceholder.value
//
//    val items = (0 until lazyPagingItems.itemCount).toList()
//    items(items) { index ->
//        val item = lazyPagingItems[index]
//        itemContent(index, item)
//    }
//}
