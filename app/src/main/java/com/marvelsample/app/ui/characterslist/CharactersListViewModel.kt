package com.marvelsample.app.ui.characterslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.marvelsample.app.core.usecases.characterslist.CharactersListUseCase
import kotlinx.coroutines.flow.Flow

class CharactersListViewModel(
    private val useCase: CharactersListUseCase,
    private val charactersSource: CharactersSource = CharactersSource(useCase)
) : ViewModel() {

    fun load(): Flow<PagingData<ListItem>> =
        Pager(
            config = PagingConfig(
                initialLoadSize = 25,
                pageSize = 25
            ),
            pagingSourceFactory = { charactersSource }
        ).flow.cachedIn(viewModelScope)
}