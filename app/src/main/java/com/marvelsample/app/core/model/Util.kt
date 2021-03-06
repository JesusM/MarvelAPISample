package com.marvelsample.app.core.model

fun Thumbnail.fullPath(): String? {
    if (path.isEmpty() or extension.isEmpty()) {
        return null
    }
    return "$path.$extension"
}
