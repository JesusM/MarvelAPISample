package com.marvelsample.app.core.usecases.characterdetails

import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.model.comic.Comic
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.usecases.characterdetails.comics.repository.CharacterComicsRepository
import com.marvelsample.app.core.usecases.characterdetails.repository.CharacterDetailsRepository

class CharacterDetailsUseCase(
    private val detailsRepository: CharacterDetailsRepository,
    private val comicsRepository: CharacterComicsRepository
) {
    suspend fun getCharacter(id: Int): Resource<Character> = detailsRepository.getItem(id)
    suspend fun getCharacterComics(
        characterId: Int,
        collectionRequestParams: CollectionRequestParams
    ): Resource<Pager<Comic>> = comicsRepository.getComics(characterId, collectionRequestParams)
}