package com.marvelsample.app.core.repository.model.base.error

sealed class ResourceError {
    object EmptyContent : ResourceError()
    class RequestFailError(val errorMessage: String? = "") : ResourceError()
    object NoNetworkError : ResourceError()
}