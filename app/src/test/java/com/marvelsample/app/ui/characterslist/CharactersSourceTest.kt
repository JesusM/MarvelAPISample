package com.marvelsample.app.ui.characterslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.marvelsample.app.core.model.Character
import com.marvelsample.app.core.model.ExternalCollection
import com.marvelsample.app.core.model.Thumbnail
import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.model.fullPath
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.usecases.characterslist.CharactersListUseCase
import com.marvelsample.app.core.usecases.characterslist.repository.CharactersListRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class CharactersSourceTest {
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
        val charactersListRepository = Mockito.mock(CharactersListRepository::class.java)
        val expectedError = ResourceError.EmptyContent
        val collectionQuery = CollectionRequestParams(0, 20)
        Mockito.`when`(charactersListRepository.getContent(collectionQuery))
            .thenReturn(Resource.Error(expectedError))
        val charactersSource = CharactersSource(CharactersListUseCase(charactersListRepository))
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
        val charactersListRepository = Mockito.mock(CharactersListRepository::class.java)
        val expectedContent: MutableList<ListItem> = mutableListOf()
        val repositoryContent: MutableList<Character> = mutableListOf()
        val expectedId = 1
        val element = Character(
            expectedId,
            "",
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
        repositoryContent.add(
            element
        )
        expectedContent.add(ListItem(element.id, element.name, element.thumbnail.fullPath()))
        val params = CollectionRequestParams(0, 20)
        Mockito.`when`(charactersListRepository.getContent(params))
            .thenReturn(Resource.Success(Pager(repositoryContent)))
        val load = CharactersSource(CharactersListUseCase(charactersListRepository)).load(
            PagingSource.LoadParams.Refresh(
                params.offset,
                params.limit,
                false
            )
        )

        assert(load is PagingSource.LoadResult.Page)
        assertEquals((load as PagingSource.LoadResult.Page).data, expectedContent)
        assertEquals(load.data.first().id, expectedId)
    }

    private fun createEmptyExternalCollection(): ExternalCollection =
        ExternalCollection(0, "", emptyList(), 0)
}