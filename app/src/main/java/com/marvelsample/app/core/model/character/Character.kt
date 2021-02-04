package com.marvelsample.app.core.model.character

import com.marvelsample.app.core.model.ExternalCollection
import com.marvelsample.app.core.model.Thumbnail
import com.marvelsample.app.core.model.Url

data class Character(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: Thumbnail,
    val comics: ExternalCollection,
    val events: ExternalCollection,
    val modified: String,
    val resourceURI: String,
    val series: ExternalCollection,
    val stories: ExternalCollection,
    val urls: List<Url>
)