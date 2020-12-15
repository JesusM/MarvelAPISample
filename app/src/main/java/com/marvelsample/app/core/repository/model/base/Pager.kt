package com.marvelsample.app.core.repository.model.base

import androidx.annotation.Keep

@Keep
data class Pager<T>(
    val results: List<T>,
    val offset: Int = 0,
    val limit: Int = 0,
    val count: Int = results.size,
    val total: Int = results.size
)