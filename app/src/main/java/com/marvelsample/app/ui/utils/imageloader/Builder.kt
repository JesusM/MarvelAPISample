package com.marvelsample.app.ui.utils.imageloader

class Builder(
    var width: Int = -1,
    var height: Int = -1,
    var errorResource: Int = -1,
    var placeholderResource: Int = -1
) {
    fun resize(width: Int, height: Int): Builder {
        this.width = width
        this.height = height
        return this
    }

    fun error(errorResource: Int): Builder {
        this.errorResource = errorResource
        return this
    }

    fun placeHolder(placeholderResource: Int): Builder {
        this.placeholderResource = placeholderResource
        return this
    }
}