package com.marvelsample.app.core.usecases.characterslist.repository

import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams

interface CharactersListRepository {
    suspend fun getContent(collectionQuery: CollectionRequestParams): Resource<Pager<Character>>
}