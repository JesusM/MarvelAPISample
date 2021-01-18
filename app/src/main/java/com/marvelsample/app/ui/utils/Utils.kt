package com.marvelsample.app.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.LazyGridScope
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import androidx.paging.compose.LazyPagingItems
import androidx.palette.graphics.Palette
import com.marvelsample.app.App
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

fun Bitmap.paletteAsync(clearFilter: Boolean = false, f: (palette: Palette?) -> Unit) {
    var builder = Palette.from(this)
    if (clearFilter) {
        builder = builder.clearFilters()
    }
    builder.generate {
        f(it)
    }
}

fun AppCompatActivity.inflateFragment(
    fragment: Fragment, containerViewId: Int,
    addPopBackStack: Boolean = false,
    sharedElement: Pair<View, String>? = null,
    arguments: Bundle? = null
) {
    var replace = supportFragmentManager.beginTransaction()

    arguments?.let {
        fragment.arguments = arguments
    }

    sharedElement?.let {
        val fragmentArguments = fragment.arguments
        if (fragmentArguments == null) {
            fragment.arguments = Bundle()
        }
//        fragmentArguments?.putString(ITEM_TITLE_TRANSITION_NAME, sharedElement.second)
        fragment.arguments = fragmentArguments

        val transition =
            TransitionInflater.from(baseContext).inflateTransition(android.R.transition.move)
        fragment.sharedElementEnterTransition = transition
        replace = replace.addSharedElement(sharedElement.first, sharedElement.second)
    }

    replace = replace.replace(containerViewId, fragment)

    if (addPopBackStack) {
        replace = replace.addToBackStack(null)
    }

    replace.commit()
}

fun Context.inflateView(layoutRes: Int, parent: ViewGroup?): View =
    LayoutInflater.from(this).inflate(layoutRes, parent, false)

open class TransitionListenerAdapter : Transition.TransitionListener {
    override fun onTransitionEnd(transition: Transition?) = Unit

    override fun onTransitionResume(transition: Transition?) = Unit

    override fun onTransitionPause(transition: Transition?) = Unit

    override fun onTransitionCancel(transition: Transition?) = Unit

    override fun onTransitionStart(transition: Transition?) = Unit
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

fun Context.getCompatColor(@ColorRes color: Int) =
    ResourcesCompat.getColor(resources, color, null)

fun ProgressBar.applyTint(color: Int) {
    val createBlendModeColorFilterCompat =
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_IN)
    indeterminateDrawable.colorFilter = createBlendModeColorFilterCompat
    progressDrawable.colorFilter = createBlendModeColorFilterCompat
}

fun ProgressBar.showProgress(show: Boolean = true) {
    visibility = if (show) VISIBLE else GONE
}

fun Bitmap.textPaletteAsync(coroutineScope: CoroutineScope = GlobalScope): Deferred<BitmapPalette?> =
    coroutineScope.async {
        val palette: Palette = Palette.from(this@textPaletteAsync).generate()
        palette.dominantSwatch?.let {
            BitmapPalette(it.rgb, it.bodyTextColor, it.titleTextColor)
        }
    }

suspend fun Bitmap.textPaletteSync(): BitmapPalette? {
    try {
        return object : Operation<BitmapPalette> {
            override fun performAsync(callback: (BitmapPalette?, Throwable?) -> Unit) {
                Palette.from(this@textPaletteSync).generate { palette ->
                    if (palette == null) {
                        callback.invoke(null, Throwable("Error loading palette"))
                    } else {
                        palette.dominantSwatch?.let { swatch ->
                            callback.invoke(
                                BitmapPalette(
                                    swatch.rgb,
                                    swatch.bodyTextColor,
                                    swatch.titleTextColor
                                ), null
                            )
                        } ?: callback.invoke(null, Throwable("Error loading swatch"))
                    }
                }
            }
        }.perform()
    } catch (e : Exception) {
        return null
    }
}

private suspend fun <T> Operation<T>.perform(): T =
    suspendCoroutine { continuation ->
        performAsync { value, exception ->
            when {
                exception != null -> // operation had failed
                    continuation.resumeWithException(exception)
                else -> // succeeded, there is a value
                    continuation.resume(value as T)
            }
        }
    }

interface Operation<T> {
    fun performAsync(callback: (T?, Throwable?) -> Unit)
}

fun Bitmap.playButtonPaletteAsync(coroutineScope: CoroutineScope): Deferred<BitmapPalette?> =
    coroutineScope.async {
        val palette: Palette = Palette.from(this@playButtonPaletteAsync).generate()
        palette.dominantSwatch?.let {
            BitmapPalette(it.rgb, it.bodyTextColor, it.titleTextColor)
        }
    }

fun ProgressBar.changeAccentColor(@ColorInt color: Int) {
    indeterminateDrawable.colorFilter =
        BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_IN)
}

/**
 * Configure CoroutineScope injection for production and testing.
 *
 * @receiver ViewModel provides viewModelScope for production
 * @param coroutineScope null for production, injects TestCoroutineScope for unit tests
 * @return CoroutineScope to launch coroutines on
 */
fun ViewModel.getViewModelScope(coroutineScope: CoroutineScope?) =
    coroutineScope ?: this.viewModelScope

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
fun <T : Any> LazyGridScope.itemsGridIndexed(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyItemScope.(index: Int, value: T?) -> Unit
) {
    // this state recomposes every time the LazyPagingItems receives an update and changes the
    // value of recomposerPlaceholder
//        @Suppress("UNUSED_VARIABLE")
//        val recomposerPlaceholder = lazyPagingItems.recomposerPlaceholder.value

    val items = (0 until lazyPagingItems.itemCount).toList()
    items(items) { index ->
        val item = lazyPagingItems[index]
        itemContent(index, item)
    }
}
