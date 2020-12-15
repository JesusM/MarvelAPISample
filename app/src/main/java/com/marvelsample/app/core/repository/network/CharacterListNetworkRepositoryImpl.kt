package com.marvelsample.app.core.repository.network

import com.marvelsample.app.core.repository.model.base.Pager
import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.base.error.ResourceError
import com.marvelsample.app.core.repository.model.characters.Character
import com.marvelsample.app.core.repository.base.generateHash
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import java.util.*

class CharacterListNetworkRepositoryImpl(
    private val apiService: ApiService,
    private val privateKey: String,
    private val publicKey: String
) : CharacterListNetworkRepository {
    override suspend fun getCharacters(collectionQuery: CollectionRequestParams): Resource<Pager<Character>> {
        val time = Date().time
        val characters = apiService.getCharacters(
            time.toString(),
            publicKey,
            generateHash(time, privateKey, publicKey),
            collectionQuery.offset,
            collectionQuery.limit
        )

        if (characters.isSuccessful.not()) {
            return Resource.Error(ResourceError.RequestFailError())
        } else {
            val body = characters.body()
            body ?: return Resource.Error(ResourceError.RequestFailError())

            if (body.data.results.isEmpty()) {
                return Resource.Error(ResourceError.EmptyContent)
            }

            return Resource.Success(body.data)
        }
    }
}