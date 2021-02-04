package com.marvelsample.app.core.usecases.characterdetails.comics.repository

import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.comic.Comic
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams

interface CharacterComicsRepository {
    suspend fun getComics(id: Int, collectionQuery: CollectionRequestParams): Resource<Pager<Comic>>
}