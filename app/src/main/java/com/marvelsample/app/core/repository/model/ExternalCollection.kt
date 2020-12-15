package com.marvelsample.app.core.repository.model

data class ExternalCollection (
    val available: Int,
    val collectionURI: String,
    val externalItems: List<ExternalItem>,
    val returned: Int
)