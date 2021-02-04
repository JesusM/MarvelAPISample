package com.marvelsample.app.core.usecases.characterdetails.comics.repository

import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.model.comic.Comic
import com.marvelsample.app.core.repository.base.generateHash
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.repository.network.ApiService
import java.util.*

class CharacterComicsNetworkRepository(
    private val apiService: ApiService,
    private val privateKey: String,
    private val publicKey: String
) : CharacterComicsRepository {
    override suspend fun getComics(id: Int, collectionQuery: CollectionRequestParams): Resource<Pager<Comic>> {
        val time = Date().time
        val hash = generateHash(time, privateKey, publicKey)
        val networkComicsResponse = apiService.getCharacterComics(
            id,
            time.toString(),
            publicKey,
            hash,
            collectionQuery.offset,
            collectionQuery.limit
        )
        val networkComics = networkComicsResponse.body()?.data
        return if (networkComicsResponse.isSuccessful && networkComics != null) {
            Resource.Success(networkComics)
        } else {
            Resource.Error(ResourceError.EmptyContent)
        }
    }
}