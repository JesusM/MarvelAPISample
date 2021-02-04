package com.marvelsample.app.core.model.comic

import com.marvelsample.app.core.model.ExternalCollection
import com.marvelsample.app.core.model.ExternalItem
import com.marvelsample.app.core.model.Thumbnail
import com.marvelsample.app.core.model.Url

data class Comic(
    val id : String,
    val digitalId : String,
    val title : String,
    val issueNumber : String,
    val variantDescription : String,
    val description : String,
    val modified : String,
    val isbn : String,
    val upc : String,
    val diamondCode : String,
    val ean : String,
    val issn : String,
    val format : String,
    val pageCount : String,
    val textObjects : List<TextObjects>,
    val resourceURI : String,
    val urls : List<Url>,
    val series : ExternalItem,
    val variants : List<ExternalItem>,
    val collections : List<ExternalItem>,
    val collectedIssues : List<ExternalItem>,
    val dates : List<Dates>,
    val prices : List<Prices>,
    val thumbnail : Thumbnail,
    val images : List<Thumbnail>,
    val creators : ExternalCollection,
    val characters : ExternalCollection,
    val stories : ExternalCollection,
    val events : ExternalCollection
)