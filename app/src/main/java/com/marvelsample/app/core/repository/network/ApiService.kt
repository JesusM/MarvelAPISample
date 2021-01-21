package com.marvelsample.app.core.repository.network

import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.repository.network.model.ListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/v1/public/characters")
    suspend fun getCharacters(
        @Query("ts") ts: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int,
    ): Response<ListResponse<Character>>

    @GET("/v1/public/characters/{characterId}")
    suspend fun getCharacter(
        @Path("characterId") userId: String,
        @Query("ts") ts: String,
        @Query("apikey") apiKey: String,
        @Query("hash") hash: String,
    ): Response<ListResponse<Character>>
}