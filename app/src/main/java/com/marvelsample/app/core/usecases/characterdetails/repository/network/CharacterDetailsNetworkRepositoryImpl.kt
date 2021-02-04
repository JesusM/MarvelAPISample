package com.marvelsample.app.core.usecases.characterdetails.repository.network

import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.repository.base.generateHash
import com.marvelsample.app.core.repository.network.ApiService
import java.util.*

class CharacterDetailsNetworkRepositoryImpl(
    private val apiService: ApiService,
    private val privateKey: String,
    private val publicKey: String
) : CharacterDetailsNetworkRepository {
    override suspend fun getCharacter(id: Int): Resource<Character> {
        val time = Date().time
        val hash = generateHash(time, privateKey, publicKey)
        val networkCharacterResponse = apiService.getCharacter(
            id.toString(),
            time.toString(),
            publicKey,
            hash
        )
        val networkCharacter = networkCharacterResponse.body()?.data?.results?.first()
        return if (networkCharacterResponse.isSuccessful && networkCharacter != null) {
            Resource.Success(networkCharacter)
        } else {
            Resource.Error(ResourceError.EmptyContent)
        }
    }
}