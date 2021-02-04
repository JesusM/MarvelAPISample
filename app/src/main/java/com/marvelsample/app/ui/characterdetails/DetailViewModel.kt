package com.marvelsample.app.ui.characterdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.fullPath
import com.marvelsample.app.core.usecases.characterdetails.CharacterDetailsUseCase
import com.marvelsample.app.ui.base.model.Result
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val characterId: Int,
    private val useCase: CharacterDetailsUseCase,
    private val comicsSource: ComicsSource = ComicsSource(characterId, useCase),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    private var _item = MutableLiveData<Result<CharacterModel>>()
    val itemDetailsObservable: LiveData<Result<CharacterModel>>
        get() = _item

    fun load() {
        viewModelScope.launch(dispatcher) {
            _item.postValue(Result.Loading)

            when (val characterResult = useCase.getCharacter(characterId)) {
                is Resource.Error -> {
                    _item.postValue(Result.Error(characterResult.error))
                }
                is Resource.Success -> {
                    val result = characterResult.result
                    result.thumbnail.fullPath()
                    _item.postValue(
                        Result.Success(
                            CharacterModel(
                                result.name,
                                result.description,
                                result.thumbnail.fullPath()
                            )
                        )
                    )
                }
            }
        }
    }

    fun loadComics(): Flow<PagingData<ComicListItem>> =
        Pager(
            config = PagingConfig(
                initialLoadSize = 20,
                pageSize = 20
            ),
            pagingSourceFactory = { comicsSource }
        ).flow.cachedIn(viewModelScope)
}
