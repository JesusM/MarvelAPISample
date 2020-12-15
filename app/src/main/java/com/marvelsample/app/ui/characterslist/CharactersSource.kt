package com.marvelsample.app.ui.characterslist

import androidx.paging.PagingSource
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.fullPath
import com.marvelsample.app.core.usecases.characterslist.CharactersListUseCase

class CharactersSource(private val charactersListUseCase: CharactersListUseCase) :
    PagingSource<Int, ListItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListItem> {
        val page = params.key ?: 0
        val pagedCollection = charactersListUseCase.getCharacters(
            CollectionRequestParams(
                page,
                params.loadSize
            )
        )
        return when (pagedCollection) {
            is Resource.Error -> LoadResult.Error(throwable = Throwable(pagedCollection.error.toString()))
            is Resource.Success -> {
                val pagedCollectionResult = pagedCollection.result
                val characters = pagedCollectionResult.results
                // Only paging forward.
                val prevKey = null
                val nextKey = pagedCollectionResult.offset + pagedCollectionResult.count
                LoadResult.Page(characters.map { character ->
                    ListItem(
                        character.id,
                        character.name,
                        character.thumbnail.fullPath()
                    )
                }, prevKey, nextKey)
            }
        }
    }
}