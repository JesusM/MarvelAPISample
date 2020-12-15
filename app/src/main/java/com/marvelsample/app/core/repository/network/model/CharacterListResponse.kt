package com.marvelsample.app.core.repository.network.model

import com.marvelsample.app.core.repository.model.base.Pager
import com.marvelsample.app.core.repository.model.characters.Character

data class CharacterListResponse(
    val code: Int,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val etag: String,
    val data: Pager<Character>
)