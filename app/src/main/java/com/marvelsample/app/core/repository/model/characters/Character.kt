package com.marvelsample.app.core.repository.model.characters

import com.marvelsample.app.core.repository.model.ExternalCollection
import com.marvelsample.app.core.repository.model.Thumbnail
import com.marvelsample.app.core.repository.model.Url

data class Character(
    val comics: ExternalCollection,
    val description: String,
    val events: ExternalCollection,
    val id: Int,
    val modified: String,
    val name: String,
    val resourceURI: String,
    val series: ExternalCollection,
    val stories: ExternalCollection,
    val thumbnail: Thumbnail,
    val urls: List<Url>
)