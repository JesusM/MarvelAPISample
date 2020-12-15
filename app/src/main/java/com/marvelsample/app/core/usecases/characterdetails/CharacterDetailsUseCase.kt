package com.marvelsample.app.core.usecases.characterdetails

import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.characters.Character
import com.marvelsample.app.core.repository.CharacterDetailsRepository

class CharacterDetailsUseCase(private val repository: CharacterDetailsRepository) {
    suspend fun getCharacter(id: Int): Resource<Character> = repository.getItem(id)
}