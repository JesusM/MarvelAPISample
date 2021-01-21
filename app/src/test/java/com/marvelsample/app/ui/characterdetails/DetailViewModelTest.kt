package com.marvelsample.app.ui.characterdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marvelsample.app.core.usecases.characterdetails.repository.CharacterDetailsRepository
import com.marvelsample.app.core.model.ExternalCollection
import com.marvelsample.app.core.model.Thumbnail
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.model.Character
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
        val observer = mock<Observer<Result<CharacterModel>>>()
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
        val observer = mock<Observer<Result<CharacterModel>>>()
        val expectedName = "name"
        val element = Character(
            1,
            expectedName,
            "",
            Thumbnail("", ""),
            createEmptyExternalCollection(),
            createEmptyExternalCollection(),
            "",
            "",
            createEmptyExternalCollection(),
            createEmptyExternalCollection(),
            emptyList()
        )
        val repositoryCharacter = CharacterModel(expectedName, "", null)

        val mockRepository = mock<CharacterDetailsRepository>()
        Mockito.`when`(mockRepository.getItem(anyInt())).thenReturn(Resource.Success(element))
        viewModel = DetailViewModel(
            CharacterDetailsUseCase(mockRepository),
            dispatcher = testCoroutineDispatcher
        )
        viewModel.itemObservable.observeForever(observer)

        viewModel.load(0)

        verify(observer).onChanged(Result.Success(repositoryCharacter))
    }

    @Test
    fun `view model posts correct error state`() = runBlockingTest {
        val mockRepository = mock<CharacterDetailsRepository>()
        val observer = mock<Observer<Result<CharacterModel>>>()
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

    private fun createEmptyExternalCollection(): ExternalCollection =
        ExternalCollection(0, "", emptyList(), 0)
}