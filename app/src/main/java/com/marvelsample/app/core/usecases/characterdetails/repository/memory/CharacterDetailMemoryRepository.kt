package com.marvelsample.app.core.usecases.characterdetails.repository.memory

import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.model.base.Resource

interface CharacterDetailMemoryRepository {
    fun getItem(id : Int) : Resource<Character>
    fun addItem(item : Character)
}