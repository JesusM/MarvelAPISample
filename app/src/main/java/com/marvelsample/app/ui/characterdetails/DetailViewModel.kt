package com.marvelsample.app.ui.characterdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.fullPath
import com.marvelsample.app.core.usecases.characterdetails.CharacterDetailsUseCase
import com.marvelsample.app.ui.base.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(
    private val useCase: CharacterDetailsUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) :
    ViewModel() {
    private var _item = MutableLiveData<Result<CharacterModel>>()
    val itemObservable: LiveData<Result<CharacterModel>>
        get() = _item

    fun load(id: Int) {
        viewModelScope.launch(dispatcher) {
            _item.postValue(Result.Loading)

            when (val characterResult = useCase.getCharacter(id)) {
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
}
