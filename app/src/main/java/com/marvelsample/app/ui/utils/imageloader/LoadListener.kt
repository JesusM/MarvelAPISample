package com.marvelsample.app.ui.utils.imageloader

import android.graphics.Bitmap

interface LoadListener {
    fun onLoadFailed()
    fun onStart()
    fun onBitmapLoaded(bitmap: Bitmap?)
}