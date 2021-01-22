package com.marvelsample.app.core.usecases.characterdetails.repository

import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.model.base.Resource

interface CharacterDetailsRepository {
    suspend fun getItem(id: Int): Resource<Character>
}