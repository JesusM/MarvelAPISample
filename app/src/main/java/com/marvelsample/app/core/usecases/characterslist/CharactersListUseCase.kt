package com.marvelsample.app.core.usecases.characterslist

import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.usecases.characterslist.repository.CharactersListRepository

class CharactersListUseCase(private val repository: CharactersListRepository) {
    suspend fun getCharacters(collectionQuery: CollectionRequestParams): Resource<Pager<Character>> =
        repository.getContent(collectionQuery)
}