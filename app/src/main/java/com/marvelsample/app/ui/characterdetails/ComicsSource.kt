package com.marvelsample.app.ui.characterdetails

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.model.fullPath
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.usecases.characterdetails.CharacterDetailsUseCase
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem

class ComicsSource(
    private val characterId: Int,
    private val characterDetailsUseCase: CharacterDetailsUseCase
) : PagingSource<Int, ComicListItem>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ComicListItem> {
        val page = params.key ?: 0
        val pagedCollection = characterDetailsUseCase.getCharacterComics(
            characterId,
            CollectionRequestParams(
                page,
                params.loadSize
            )
        )
        return when (pagedCollection) {
            is Resource.Error -> {
                LoadResult.Error(
                    throwable = Throwable(
                        when (val error = pagedCollection.error) {
                            ResourceError.EmptyContent -> "No content"
                            is ResourceError.RequestFailError -> error.errorMessage
                            ResourceError.NoNetworkError -> "No network"
                        }
                    )
                )
            }
            is Resource.Success -> {
                val pagedCollectionResult = pagedCollection.result
                val characters = pagedCollectionResult.results
                // Only paging forward.
                val prevKey = null
                val canLoadNextPage =
                    pagedCollectionResult.offset + pagedCollectionResult.count < pagedCollection.result.total
                val nextKey =
                    if (canLoadNextPage) pagedCollectionResult.offset + pagedCollectionResult.count else null
                LoadResult.Page(characters.map { comic ->
                    ComicListItem(comic.id, comic.title, comic.thumbnail.fullPath())
                }, prevKey, nextKey)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ComicListItem>): Int? {
        return state.anchorPosition
    }
}