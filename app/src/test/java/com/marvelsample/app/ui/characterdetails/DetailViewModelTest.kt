package com.marvelsample.app.ui.characterdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.model.ExternalCollection
import com.marvelsample.app.core.model.Thumbnail
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.usecases.characterdetails.CharacterDetailsUseCase
import com.marvelsample.app.core.usecases.characterdetails.repository.CharacterDetailsRepository
import com.marvelsample.app.ui.base.model.Result
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
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class DetailViewModelTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testCoroutineDispatcher)
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun tearDown() {
        testCoroutineDispatcher.cleanupTestCoroutines()
        Dispatchers.resetMain()
    }

    @Mock
    private lateinit var detailsObserver: Observer<Result<CharacterModel>>

    @Test
    fun `view model request posts loading state first`() = runBlockingTest {
        val mock = mock(CharacterDetailsRepository::class.java)
        viewModel = DetailViewModel(
            CharacterDetailsUseCase(mock),
            dispatcher = testCoroutineDispatcher
        )
        viewModel.itemObservable.observeForever(detailsObserver)
        viewModel.load(0)

        verify(detailsObserver).onChanged(Result.Loading)

        viewModel.itemObservable.removeObserver(detailsObserver)
    }

    @Test
    fun `view model posts success state`() = runBlockingTest {
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

        val mockRepository = mock(CharacterDetailsRepository::class.java)
        Mockito.`when`(mockRepository.getItem(anyInt())).thenReturn(Resource.Success(element))
        viewModel = DetailViewModel(
            CharacterDetailsUseCase(mockRepository),
            dispatcher = testCoroutineDispatcher
        )
        viewModel.itemObservable.observeForever(detailsObserver)

        viewModel.load(0)

        verify(detailsObserver).onChanged(Result.Success(repositoryCharacter))

        viewModel.itemObservable.removeObserver(detailsObserver)
    }

    @Test
    fun `view model posts correct error state`() = runBlockingTest {
        val mockRepository = mock(CharacterDetailsRepository::class.java)
        val expectedError = ResourceError.EmptyContent
        Mockito.`when`(mockRepository.getItem(anyInt()))
            .thenReturn(Resource.Error(expectedError))
        viewModel = DetailViewModel(
            CharacterDetailsUseCase(mockRepository),
            dispatcher = testCoroutineDispatcher
        )
        viewModel.itemObservable.observeForever(detailsObserver)
        viewModel.load(0)

        verify(detailsObserver).onChanged(Result.Error(expectedError))
    }

    private fun createEmptyExternalCollection(): ExternalCollection =
        ExternalCollection(0, "", emptyList(), 0)
}