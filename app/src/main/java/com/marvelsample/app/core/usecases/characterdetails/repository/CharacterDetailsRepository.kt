package com.marvelsample.app.core.usecases.characterdetails.repository

import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.character.Character

interface CharacterDetailsRepository {
    suspend fun getItem(id: Int): Resource<Character>
}