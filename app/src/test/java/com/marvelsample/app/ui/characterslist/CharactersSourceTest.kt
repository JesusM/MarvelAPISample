package com.marvelsample.app.ui.characterslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.marvelsample.app.core.repository.model.ExternalCollection
import com.marvelsample.app.core.repository.model.Thumbnail
import com.marvelsample.app.core.repository.model.base.Pager
import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.base.error.ResourceError
import com.marvelsample.app.core.repository.model.characters.Character
import com.marvelsample.app.core.repository.model.fullPath
import com.marvelsample.app.core.usecases.characterslist.CharactersListUseCase
import com.nhaarman.mockitokotlin2.any
import org.junit.Assert.assertEquals
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
        val charactersListUseCase = Mockito.mock(CharactersListUseCase::class.java)
        val expectedError = ResourceError.EmptyContent
        Mockito.`when`(charactersListUseCase.getCharacters(any()))
            .thenReturn(Resource.Error(expectedError))
        val charactersSource = CharactersSource(charactersListUseCase)
        val load = charactersSource.load(PagingSource.LoadParams.Refresh(0, 20, false))
        assert(load is PagingSource.LoadResult.Error)
    }

    @Test
    fun `return correct content`() = runBlockingTest {
        val charactersListUseCase = Mockito.mock(CharactersListUseCase::class.java)
        val expectedContent: MutableList<ListItem> = mutableListOf()
        val repositoryContent: MutableList<Character> = mutableListOf()
        val expectedId = 1
        val element = Character(
            createEmptyExternalCollection(),
            "",
            createEmptyExternalCollection(),
            expectedId,
            "",
            "",
            "",
            createEmptyExternalCollection(),
            createEmptyExternalCollection(),
            Thumbnail("", ""),
            emptyList()
        )
        repositoryContent.add(
            element
        )
        expectedContent.add(ListItem(element.id, element.name, element.thumbnail.fullPath()))
        Mockito.`when`(charactersListUseCase.getCharacters(any()))
            .thenReturn(Resource.Success(Pager(repositoryContent)))
        val load = CharactersSource(charactersListUseCase).load(
            PagingSource.LoadParams.Refresh(
                0,
                20,
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