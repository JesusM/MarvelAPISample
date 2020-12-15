package com.marvelsample.app.core.repository.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val API_ENDPOINT = "https://gateway.marvel.com/"

class ApiClient(private val okHttpClient: OkHttpClient) {
    private val apiService: ApiService by lazy {
        val restAdapter: Retrofit = Retrofit.Builder()
            .baseUrl(API_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        restAdapter.create(ApiService::class.java)
    }

    fun getService(): ApiService = apiService
}