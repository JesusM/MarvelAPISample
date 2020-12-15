package com.marvelsample.app.core.repository.network

import com.marvelsample.app.core.repository.model.base.Pager
import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.characters.Character
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams

interface CharacterListNetworkRepository {
    suspend fun getCharacters(collectionQuery: CollectionRequestParams): Resource<Pager<Character>>
}