package com.marvelsample.app.core.usecases.characterdetails.repository

import com.marvelsample.app.core.repository.memory.ItemMemoryRepository
import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.usecases.characterdetails.repository.network.CharacterDetailsNetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterDetailsRepository(
    private val memoryRepository: ItemMemoryRepository<Int, Character>,
    private val networkRepository: CharacterDetailsNetworkRepository,
    private val IODispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getItem(id: Int): Resource<Character> {
        return withContext(IODispatcher) {
            when (val item = memoryRepository.get(id)) {
                is Resource.Error -> {
                    // Go to network for it
                    when (val itemFromNetwork = networkRepository.getCharacter(id)) {
                        is Resource.Error -> {
                            itemFromNetwork
                        }
                        is Resource.Success -> {
                            memoryRepository.add(id, itemFromNetwork.result)
                            itemFromNetwork
                        }
                    }
                }
                is Resource.Success -> {
                    Resource.Success(item.result)
                }
            }
        }
    }
}