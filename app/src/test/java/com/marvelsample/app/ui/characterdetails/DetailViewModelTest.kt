package com.marvelsample.app.ui.characterdetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.marvelsample.app.core.model.ExternalCollection
import com.marvelsample.app.core.model.Thumbnail
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.usecases.characterdetails.CharacterDetailsUseCase
import com.marvelsample.app.core.usecases.characterdetails.comics.repository.CharacterComicsRepository
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
    fun `view model request posts details loading state first`() = runBlockingTest {
        viewModel = DetailViewModel(
            0,
            createFakeUseCase(),
            dispatcher = testCoroutineDispatcher
        )
        viewModel.itemDetailsObservable.observeForever(detailsObserver)
        viewModel.load()

        verify(detailsObserver).onChanged(Result.Loading)

        viewModel.itemDetailsObservable.removeObserver(detailsObserver)
    }

    @Test
    fun `view model posts success details state`() = runBlockingTest {
        val expectedName = "name"
        val expectedId = 1
        val element = Character(
            expectedId,
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
            expectedId,
            createFakeUseCase(characterDetailsRepository = mockRepository),
            dispatcher = testCoroutineDispatcher
        )
        viewModel.itemDetailsObservable.observeForever(detailsObserver)

        viewModel.load()

        verify(detailsObserver).onChanged(Result.Success(repositoryCharacter))

        viewModel.itemDetailsObservable.removeObserver(detailsObserver)
    }

    @Test
    fun `view model posts correct details error state`() = runBlockingTest {
        val mockRepository = mock(CharacterDetailsRepository::class.java)
        val expectedError = ResourceError.EmptyContent
        Mockito.`when`(mockRepository.getItem(anyInt()))
            .thenReturn(Resource.Error(expectedError))
        viewModel = DetailViewModel(
            1,
            createFakeUseCase(characterDetailsRepository = mockRepository),
            dispatcher = testCoroutineDispatcher
        )
        viewModel.itemDetailsObservable.observeForever(detailsObserver)
        viewModel.load()

        verify(detailsObserver).onChanged(Result.Error(expectedError))
    }

    private fun createEmptyExternalCollection(): ExternalCollection =
        ExternalCollection(0, "", emptyList(), 0)

    private fun createFakeUseCase(
        characterDetailsRepository: CharacterDetailsRepository = mock(CharacterDetailsRepository::class.java),
        comicsRepository: CharacterComicsRepository = mock(CharacterComicsRepository::class.java)
    ): CharacterDetailsUseCase {
        return CharacterDetailsUseCase(characterDetailsRepository, comicsRepository)
    }
}