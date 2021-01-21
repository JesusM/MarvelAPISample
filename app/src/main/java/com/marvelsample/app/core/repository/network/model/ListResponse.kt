package com.marvelsample.app.core.repository.network.model

import com.marvelsample.app.core.model.base.Pager

data class ListResponse<T>(
    val code: Int,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val etag: String,
    val data: Pager<T>
)