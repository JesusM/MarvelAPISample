package com.marvelsample.app.ui.utils.imageloader

import android.net.Uri
import android.widget.ImageView

interface ImageLoader {
    fun load(url: String, builder: Builder, loadListener: LoadListener, into: ImageView)
    fun load(url: String, builder: Builder, loadListener: LoadListener)
    fun load(uri: Uri, builder: Builder, loadListener: LoadListener)
}