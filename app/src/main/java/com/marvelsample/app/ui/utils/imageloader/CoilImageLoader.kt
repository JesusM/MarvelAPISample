package com.marvelsample.app.ui.utils.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import coil.bitmap.BitmapPool
import coil.load
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.Transformation
import coil.util.DebugLogger
import com.marvelsample.app.BuildConfig

class CoilImageLoader(
    private val context: Context,
    private val imageLoader: coil.ImageLoader = coil.ImageLoader.Builder(context)
        .crossfade(true) // Show a short crossfade when loading images from network or disk.
        .apply {
            // Enable logging to the standard Android log if this is a debug build.
            if (BuildConfig.DEBUG) {
                logger(DebugLogger(Log.VERBOSE))
            }
        }
        .build()
) : ImageLoader {
    override fun load(url: String, builder: Builder, loadListener: LoadListener, into: ImageView) {
        into.load(url) {
            listener(
                onStart = {
                    loadListener.onStart()
                },
                onError = { _: ImageRequest, _: Throwable ->
                    loadListener.onLoadFailed()
                }
            )
            transformations(object : Transformation {
                override fun key(): String = "loadListenerTransformation"

                override suspend fun transform(
                    pool: BitmapPool,
                    input: Bitmap,
                    size: Size
                ): Bitmap {
                    loadListener.onBitmapLoaded(input)
                    return input
                }
            })
        }
    }

    override fun load(url: String, builder: Builder, loadListener: LoadListener) {
        val request = loadRequestBuilder(builder)
            .data(url)
            .target(
                onStart = {
                    loadListener.onStart()
                },
                onSuccess = { result ->
                    loadListener.onBitmapLoaded(drawableToBitmap(result))
                },
                onError = { error ->
                    error?.let {
                        loadListener.onBitmapLoaded(drawableToBitmap(it))
                    }
                }
            )
            .build()
        imageLoader.enqueue(request)
    }

    override fun load(uri: Uri, builder: Builder, loadListener: LoadListener) {
        val request = loadRequestBuilder(builder)
            .data(uri)
            .target(
                onStart = {
                    loadListener.onStart()
                },
                onSuccess = { result ->
                    loadListener.onBitmapLoaded(drawableToBitmap(result))
                },
                onError = { error ->
                    error?.let {
                        loadListener.onBitmapLoaded(drawableToBitmap(it))
                    }
                }
            )
            .build()
        imageLoader.enqueue(request)
    }

    private fun loadRequestBuilder(builder: Builder): ImageRequest.Builder {
        return ImageRequest.Builder(context)
            .apply {
                allowHardware(false)
                if (builder.width > 1 && builder.height > 1) {
                    size(builder.width, builder.height)
                }
                if (builder.errorResource != -1) {
                    error(builder.errorResource)
                }
                if (builder.placeholderResource != -1) {
                    placeholder(builder.placeholderResource)
                }
            }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1
        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }
}