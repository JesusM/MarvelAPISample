package com.marvelsample.app.ui.base.model

import com.marvelsample.app.core.repository.model.base.error.ResourceError

sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Error(val error: ResourceError) : Result<Nothing>()
    data class Success<T>(val result: T) : Result<T>()
}