package com.marvelsample.app.core.repository.network

import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.characters.Character

interface CharacterDetailsNetworkRepository {
    suspend fun getCharacter(id: Int): Resource<Character>
}