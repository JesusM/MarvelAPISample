package com.marvelsample.app.core.usecases.characterdetails

import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.usecases.characterdetails.repository.CharacterDetailsRepository

class CharacterDetailsUseCase(private val repository: CharacterDetailsRepository) {
    suspend fun getCharacter(id: Int): Resource<Character> = repository.getItem(id)
}