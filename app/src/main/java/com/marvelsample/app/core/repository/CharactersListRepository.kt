package com.marvelsample.app.core.repository

import com.marvelsample.app.core.repository.model.base.Pager
import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.characters.Character
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.repository.memory.PagedCollectionMemoryRepository
import com.marvelsample.app.core.repository.network.CharacterListNetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharactersListRepository(
    private val memoryRepository: PagedCollectionMemoryRepository<Character>,
    private val networkRepository: CharacterListNetworkRepository,
    private val IODispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getContent(collectionQuery: CollectionRequestParams): Resource<Pager<Character>> {
        return withContext(IODispatcher) {
            when (val memoryPage = memoryRepository.getPage(
                collectionQuery.offset,
                collectionQuery.limit
            )) {
                is Resource.Error -> {
                    when (val charactersFromNetwork =
                        networkRepository.getCharacters(collectionQuery)) {
                        is Resource.Error -> {
                            Resource.Error(charactersFromNetwork.error)
                        }
                        is Resource.Success -> {
                            val newItems = charactersFromNetwork.result
                            memoryRepository.add(newItems.results)
                            Resource.Success(newItems)
                        }
                    }
                }
                else -> memoryPage
            }
        }
    }
}