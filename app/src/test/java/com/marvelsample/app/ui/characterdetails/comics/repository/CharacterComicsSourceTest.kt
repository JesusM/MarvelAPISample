package com.marvelsample.app.ui.characterdetails.comics.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.marvelsample.app.core.model.ExternalItem
import com.marvelsample.app.core.model.Thumbnail
import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.model.comic.Comic
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.usecases.characterdetails.CharacterDetailsUseCase
import com.marvelsample.app.core.usecases.characterdetails.comics.repository.CharacterComicsRepository
import com.marvelsample.app.core.usecases.characterdetails.repository.CharacterDetailsRepository
import com.marvelsample.app.createEmptyExternalCollection
import com.marvelsample.app.ui.characterdetails.CharacterComicsSource
import com.marvelsample.app.ui.characterdetails.comics.ComicListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class CharacterComicsSourceTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private val testCoroutineDispatcher = TestCoroutineDispatcher()

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
    fun `return load result error if empty content`() = runBlockingTest {
        val characterComicsRepository = mock(CharacterComicsRepository::class.java)
        val expectedError = ResourceError.EmptyContent
        val collectionQuery = CollectionRequestParams(0, 20)
        val expectedCharacterId = 1
        `when`(characterComicsRepository.getComics(expectedCharacterId, collectionQuery))
            .thenReturn(Resource.Error(expectedError))
        val charactersSource = CharacterComicsSource(
            expectedCharacterId,
            CharacterDetailsUseCase(
                mock(CharacterDetailsRepository::class.java),
                characterComicsRepository
            )
        )
        val load = charactersSource.load(
            PagingSource.LoadParams.Refresh(
                collectionQuery.offset,
                collectionQuery.limit,
                false
            )
        )
        assert(load is PagingSource.LoadResult.Error)
    }

    @Test
    fun `return correct content`() = runBlockingTest {
        val characterComicsRepository = mock(CharacterComicsRepository::class.java)
        val expectedCharacterId = 1
        val expectedId = "1"
        val expectedTitle = "comic"
        val expectedRepositoryContent: List<Comic> = listOf(
            Comic(
                expectedId,
                "",
                expectedTitle,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                emptyList(),
                "",
                emptyList(),
                ExternalItem("", ""),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                emptyList(),
                Thumbnail("jpg", "a"),
                emptyList(),
                createEmptyExternalCollection(),
                createEmptyExternalCollection(),
                createEmptyExternalCollection(),
                createEmptyExternalCollection()
            )
        )
        val expectedComics = listOf(ComicListItem(expectedId, expectedTitle, "a.jpg"))
        val params = CollectionRequestParams(0, 20)
        `when`(characterComicsRepository.getComics(expectedCharacterId, params)).thenReturn(
            Resource.Success(Pager(expectedRepositoryContent))
        )
        val load = CharacterComicsSource(
            expectedCharacterId,
            CharacterDetailsUseCase(
                mock(CharacterDetailsRepository::class.java),
                characterComicsRepository
            )
        ).load(
            PagingSource.LoadParams.Refresh(
                params.offset,
                params.limit,
                false
            )
        )

        assert(load is PagingSource.LoadResult.Page)
        Assert.assertEquals((load as PagingSource.LoadResult.Page).data, expectedComics)
        Assert.assertEquals(load.data.first().id, expectedId)
    }
}