package com.marvelsample.app.ui.characterdetails.repository

import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.model.character.Character
import com.marvelsample.app.core.repository.base.MainCoroutineRule
import com.marvelsample.app.core.usecases.characterdetails.repository.CharacterDetailsRepositoryImpl
import com.marvelsample.app.core.usecases.characterdetails.repository.memory.CharacterDetailMemoryRepository
import com.marvelsample.app.core.usecases.characterdetails.repository.network.CharacterDetailsNetworkRepository
import com.marvelsample.app.createFakeCharacter
import junit.framework.Assert.assertEquals
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
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.verify

@ExperimentalCoroutinesApi
class CharacterDetailsRepositoryTest {
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule(testDispatcher)

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `should correctly return details empty state if repositories don't have content`() =
        mainCoroutineRule.runBlockingTest {
            val emptyMemoryRepository =
                Mockito.mock(CharacterDetailMemoryRepository::class.java)
            Mockito.`when`(emptyMemoryRepository.getItem(anyInt()))
                .thenReturn(Resource.Error(ResourceError.EmptyContent))

            val emptyNetworkRepository = EmptyNetworkRepository()
            val repository =
                CharacterDetailsRepositoryImpl(
                    emptyMemoryRepository,
                    emptyNetworkRepository,
                    IODispatcher = testDispatcher
                )
            val content = repository.getItem(0)

            assert(content is Resource.Error)
            assert((content as Resource.Error).error is ResourceError.EmptyContent)
        }

    @Test
    fun `should correctly return memory item details if available`() =
        mainCoroutineRule.runBlockingTest {
            val memoryRepository =
                Mockito.mock(CharacterDetailMemoryRepository::class.java)
            val expectedItemId = 0
            val memoryElement = createFakeCharacter(expectedItemId)
            Mockito.`when`(memoryRepository.getItem(anyInt()))
                .thenReturn(Resource.Success(memoryElement))
            val repository =
                CharacterDetailsRepositoryImpl(
                    memoryRepository,
                    EmptyNetworkRepository(),
                    IODispatcher = testDispatcher
                )
            val content = repository.getItem(expectedItemId)

            verify(memoryRepository).getItem(expectedItemId)

            assert(content is Resource.Success)
            assertEquals((content as Resource.Success).result.id, memoryElement.id)
        }

    @Test
    fun `should correctly return item details from network if memory repository is empty`() =
        mainCoroutineRule.runBlockingTest {
            val networkElement = createFakeCharacter(1)
            val networkRepository = Mockito.mock(CharacterDetailsNetworkRepository::class.java)
            Mockito.`when`(networkRepository.getCharacter(networkElement.id))
                .thenReturn(Resource.Success(networkElement))

            val memoryRepository = Mockito.mock(CharacterDetailMemoryRepository::class.java)
            Mockito.`when`(memoryRepository.getItem(networkElement.id))
                .thenReturn(Resource.Error(ResourceError.EmptyContent))

            val detailRepository = CharacterDetailsRepositoryImpl(
                memoryRepository,
                networkRepository, IODispatcher = testDispatcher
            )

            val content = detailRepository.getItem(networkElement.id)

            verify(networkRepository).getCharacter(networkElement.id)

            assert(content is Resource.Success)
            assertEquals((content as Resource.Success).result.id, networkElement.id)
        }

    @Test
    fun `item details from network are correctly added to memory repository`() =
        mainCoroutineRule.runBlockingTest {
            val expectedElementId = 0
            val emptyMemoryRepository =
                Mockito.mock(CharacterDetailMemoryRepository::class.java)
            Mockito.`when`(emptyMemoryRepository.getItem(anyInt()))
                .thenReturn(Resource.Error(ResourceError.EmptyContent))
            val networkElement = createFakeCharacter(expectedElementId)
            val listRepository = CharacterDetailsRepositoryImpl(
                emptyMemoryRepository,
                object : CharacterDetailsNetworkRepository {
                    override suspend fun getCharacter(id: Int): Resource<Character> {
                        return Resource.Success(networkElement)
                    }
                }, IODispatcher = testDispatcher
            )
            listRepository.getItem(anyInt())

            verify(emptyMemoryRepository).addItem(networkElement)
        }


    private class EmptyNetworkRepository : CharacterDetailsNetworkRepository {
        override suspend fun getCharacter(id: Int): Resource<Character> =
            Resource.Error(ResourceError.EmptyContent)
    }
}