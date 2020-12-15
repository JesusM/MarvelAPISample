package com.marvelsample.app.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.base.error.ResourceError
import com.marvelsample.app.core.repository.model.characters.Character
import com.marvelsample.app.core.repository.CharacterDetailsRepository
import com.marvelsample.app.core.usecases.characterdetails.CharacterDetailsUseCase
import com.marvelsample.app.ui.base.model.Result
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class DetailViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun tearDown() {
        testCoroutineDispatcher.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

    @Test
    fun `view model request posts loading state first`() = runBlockingTest {
        val mock = Mockito.mock(CharacterDetailsRepository::class.java)
        val observer = mock<Observer<Result<Character>>>()
        viewModel = DetailViewModel(
            CharacterDetailsUseCase(mock),
            dispatcher = testCoroutineDispatcher
        )
        viewModel.itemObservable.observeForever(observer)
        viewModel.load(0)

        verify(observer).onChanged(Result.Loading)
    }

    @Test
    fun `view model posts success state`() = runBlockingTest {
        val mockRepository = mock<CharacterDetailsRepository>()
        val observer = mock<Observer<Result<Character>>>()
        val characterMock = mock<Character>()
        val expectedResult = Resource.Success(characterMock)
        Mockito.`when`(mockRepository.getItem(anyInt())).thenReturn(expectedResult)
        viewModel = DetailViewModel(
            CharacterDetailsUseCase(mockRepository),
            dispatcher = testCoroutineDispatcher
        )
        viewModel.itemObservable.observeForever(observer)
        viewModel.load(0)

        verify(observer).onChanged(Result.Success(characterMock))
    }

    @Test
    fun `view model posts correct error state`() = runBlockingTest {
        val mockRepository = mock<CharacterDetailsRepository>()
        val observer = mock<Observer<Result<Character>>>()
        val expectedError = ResourceError.EmptyContent
        Mockito.`when`(mockRepository.getItem(anyInt()))
            .thenReturn(Resource.Error(expectedError))
        viewModel = DetailViewModel(
            CharacterDetailsUseCase(mockRepository),
            dispatcher = testCoroutineDispatcher
        )
        viewModel.itemObservable.observeForever(observer)
        viewModel.load(0)

        verify(observer).onChanged(Result.Error(expectedError))
    }
}