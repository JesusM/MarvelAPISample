package com.marvelsample.app.core.repository.model.base

import com.marvelsample.app.core.repository.model.base.error.ResourceError

sealed class Resource<out T> {
    data class Error(val error: ResourceError) : Resource<Nothing>()
    data class Success<T>(val result: T) : Resource<T>()
}