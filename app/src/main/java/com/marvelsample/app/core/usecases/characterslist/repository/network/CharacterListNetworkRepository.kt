package com.marvelsample.app.core.usecases.characterslist.repository.network

import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams

interface CharacterListNetworkRepository {
    suspend fun getCharacters(collectionQuery: CollectionRequestParams): Resource<Pager<Character>>
}