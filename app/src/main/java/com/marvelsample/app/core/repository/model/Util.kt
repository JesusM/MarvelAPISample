package com.marvelsample.app.core.repository.model

fun Thumbnail.fullPath(): String =
    "$path.$extension"