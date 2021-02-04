package com.marvelsample.app.core.usecases.characterdetails.repository

import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.usecases.characterdetails.repository.memory.CharacterDetailMemoryRepository
import com.marvelsample.app.core.usecases.characterdetails.repository.network.CharacterDetailsNetworkRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterDetailsRepositoryImpl(
    private val memoryRepository: CharacterDetailMemoryRepository,
    private val networkRepository: CharacterDetailsNetworkRepository,
    private val IODispatcher: CoroutineDispatcher = Dispatchers.IO
) : CharacterDetailsRepository {
    override suspend fun getItem(id: Int): Resource<Character> {
        return withContext(IODispatcher) {
            when (val item = memoryRepository.getItem(id)) {
                is Resource.Error -> {
                    // Go to network for it
                    when (val itemFromNetwork = networkRepository.getCharacter(id)) {
                        is Resource.Error -> {
                            itemFromNetwork
                        }
                        is Resource.Success -> {
                            memoryRepository.addItem(itemFromNetwork.result)
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