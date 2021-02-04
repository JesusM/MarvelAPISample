package com.marvelsample.app.core.usecases.characterdetails.repository.memory

import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.repository.memory.ItemMemoryRepository

class CharacterDetailMemoryRepositoryImpl(val repository: ItemMemoryRepository<Int, Character>) :
    CharacterDetailMemoryRepository {
    override fun getItem(id: Int): Resource<Character> {
        return repository.get(id)
    }

    override fun addItem(item: Character) {
        repository.add(item.id, item)
    }
}