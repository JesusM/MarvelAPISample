package com.marvelsample.app.ui.utils.imageloader

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.DrawableRes
import androidx.palette.graphics.Palette
import com.marvelsample.app.ui.utils.afterMeasured
import com.marvelsample.app.ui.utils.paletteAsync

fun ImageView.loadImageAfterMeasureWithPalette(
    imageLoader: ImageLoader,
    image: String?,
    functionToManipulatePalette: (palette: Palette) -> Unit,
    functionToManipulateSuccessfulLoad: (bitmap: Bitmap?) -> Unit = {},
    resize: Boolean = false,
    @DrawableRes defaultImage: Int? = null
) {
    setImageBitmap(null)
    image?.let { imageUrl ->
        loadImageAfterMeasure(imageLoader, imageUrl, null, {
            functionToManipulateSuccessfulLoad.invoke(it)
            it?.paletteAsync { palette ->
                palette?.apply {
                    functionToManipulatePalette(this)
                }
            }
        }, resize = resize, defaultImage = defaultImage)
    }
}

fun ImageView.loadImageAfterMeasure(
    imageLoader: ImageLoader, image: String, progressBar: ProgressBar? = null,
    functionToHandleSuccessfulDownload: ((bitmap: Bitmap?) -> Unit) = {},
    resize: Boolean = true,
    @DrawableRes defaultImage: Int? = null
) {
    setImageBitmap(null)
    afterMeasured {
        loadImage(
            imageLoader,
            image,
            progressBar,
            functionToHandleSuccessfulDownload,
            resize,
            defaultImage
        )
    }
}

fun ImageView.loadImage(
    imageLoader: ImageLoader, url: String, progressBar: ProgressBar? = null,
    functionToHandleSuccessfulDownload: ((bitmap: Bitmap?) -> Unit) = {},
    resize: Boolean = false,
    @DrawableRes defaultImage: Int? = null
) {
    val builder = Builder()
    if (resize) {
        builder.resize(width, height)
    }

    defaultImage?.let {
        builder.placeHolder(it).error(it)
    }

    val listener = object : LoadListener {
        override fun onLoadFailed() {
            progressBar?.visibility = View.GONE
            functionToHandleSuccessfulDownload.invoke(null)
        }

        override fun onStart() {
            progressBar?.visibility = View.VISIBLE
        }

        override fun onBitmapLoaded(bitmap: Bitmap?) {
            progressBar?.visibility = View.GONE
            bitmap?.let {
                functionToHandleSuccessfulDownload.invoke(it)
            }
        }
    }
    imageLoader.load(url, builder, listener, this)
}