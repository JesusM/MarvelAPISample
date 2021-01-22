package com.marvelsample.app.core.usecases.characterslist.repository

import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.repository.memory.PagedCollectionMemoryRepository
import com.marvelsample.app.core.usecases.characterslist.repository.network.CharacterListNetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharactersListRepositoryImpl(
    private val memoryRepository: PagedCollectionMemoryRepository<Character>,
    private val networkRepository: CharacterListNetworkRepository,
    private val IODispatcher: CoroutineDispatcher = Dispatchers.IO
) : CharactersListRepository {
    override suspend fun getContent(collectionQuery: CollectionRequestParams): Resource<Pager<Character>> {
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