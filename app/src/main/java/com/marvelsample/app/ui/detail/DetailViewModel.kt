package com.marvelsample.app.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.characters.Character
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
    private var _item = MutableLiveData<Result<Character>>()
    val itemObservable: LiveData<Result<Character>>
        get() = _item

    fun load(id: Int) {
        viewModelScope.launch(dispatcher) {
            _item.postValue(Result.Loading)

            when (val characterResult = useCase.getCharacter(id)) {
                is Resource.Error -> {
                    _item.postValue(Result.Error(characterResult.error))
                }
                is Resource.Success -> {
                    _item.postValue(Result.Success(characterResult.result))
                }
            }
        }
    }
}
