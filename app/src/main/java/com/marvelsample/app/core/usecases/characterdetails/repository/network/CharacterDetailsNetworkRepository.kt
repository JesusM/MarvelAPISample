package com.marvelsample.app.core.usecases.characterdetails.repository.network

import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.model.base.Resource

interface CharacterDetailsNetworkRepository {
    suspend fun getCharacter(id: Int): Resource<Character>
}