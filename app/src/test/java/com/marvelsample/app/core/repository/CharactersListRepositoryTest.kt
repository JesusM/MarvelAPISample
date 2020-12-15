package com.marvelsample.app.core.repository

import com.marvelsample.app.core.repository.model.base.Pager
import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.base.error.ResourceError
import com.marvelsample.app.core.repository.model.characters.Character
import com.marvelsample.app.core.repository.base.MainCoroutineRule
import com.marvelsample.app.core.repository.base.queries.CollectionRequestParams
import com.marvelsample.app.core.repository.memory.PagedCollectionMemoryRepository
import com.marvelsample.app.core.repository.network.CharacterListNetworkRepository
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

@ExperimentalCoroutinesApi
class CharactersListRepositoryTest {
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
    fun `should correctly return empty state if repositories don't have content`() =
        mainCoroutineRule.runBlockingTest {
            val emptyMemoryRepository = PagedCollectionMemoryRepository<Character>()
            val emptyNetworkRepository = EmptyNetworkRepository()
            val listRepository =
                CharactersListRepository(
                    emptyMemoryRepository,
                    emptyNetworkRepository,
                    IODispatcher = testDispatcher
                )
            val content = listRepository.getContent(CollectionRequestParams())

            assert(content is Resource.Error)
            assert((content as Resource.Error).error is ResourceError.EmptyContent)
        }

    @Test
    fun `should correctly return memory content first`() =
        mainCoroutineRule.runBlockingTest {
            val memoryRepository = PagedCollectionMemoryRepository<Character>()
            val memoryElement = Mockito.mock(Character::class.java)
            memoryRepository.add(listOf(memoryElement))
            val listRepository =
                CharactersListRepository(
                    memoryRepository,
                    EmptyNetworkRepository(),
                    IODispatcher = testDispatcher
                )
            val content = listRepository.getContent(CollectionRequestParams())

            assert(content is Resource.Success)
            assertEquals((content as Resource.Success).result.count, 1)
            assertEquals(content.result.results.first().id, memoryElement.id)
        }

    @Test
    fun `should correctly return list from network if memory repository is empty`() =
        mainCoroutineRule.runBlockingTest {
            val emptyMemoryRepository = PagedCollectionMemoryRepository<Character>()
            val networkElement = Mockito.mock(Character::class.java)
            val networkRepository = Mockito.mock(CharacterListNetworkRepository::class.java)
            val collectionQuery = CollectionRequestParams()
            Mockito.`when`(networkRepository.getCharacters(collectionQuery))
                .thenReturn(Resource.Success(Pager(listOf(networkElement))))

            val listRepository = CharactersListRepository(
                emptyMemoryRepository,
                networkRepository,
                IODispatcher = testDispatcher
            )

            val content = listRepository.getContent(collectionQuery)

            Mockito.verify(networkRepository).getCharacters(collectionQuery)

            assert(content is Resource.Success)
            assertEquals((content as Resource.Success).result.count, 1)
            assertEquals(content.result.results.first().id, networkElement.id)
        }

    @Test
    fun `item from network is correctly added to memory repository`() =
        mainCoroutineRule.runBlockingTest {
            val emptyMemoryRepository =
                Mockito.mock(PagedCollectionMemoryRepository::class.java) as PagedCollectionMemoryRepository<Character>
            Mockito.`when`(emptyMemoryRepository.getPage())
                .thenReturn(Resource.Error(ResourceError.EmptyContent))
            val networkElement = Mockito.mock(Character::class.java)
            val newElements = listOf(networkElement)
            val listRepository = CharactersListRepository(
                emptyMemoryRepository,
                object : CharacterListNetworkRepository {
                    override suspend fun getCharacters(collectionQuery: CollectionRequestParams): Resource<Pager<Character>> {
                        return Resource.Success(Pager(newElements))
                    }

                }, IODispatcher = testDispatcher
            )
            listRepository.getContent(CollectionRequestParams())

            Mockito.verify(emptyMemoryRepository).add(newElements)
        }

    @Test
    fun `should correctly return consecutive pages`() =
        mainCoroutineRule.runBlockingTest {
            val memoryRepository = PagedCollectionMemoryRepository<Character>()
            memoryRepository.add(createNItems(20))
            val emptyNetworkRepository = EmptyNetworkRepository()
            val listRepository =
                CharactersListRepository(
                    memoryRepository,
                    emptyNetworkRepository,
                    IODispatcher = testDispatcher
                )
            val content = listRepository.getContent(CollectionRequestParams(0, 10))

            assert(content is Resource.Success)
            assertEquals((content as Resource.Success).result.count, 10)
            assertEquals(content.result.total, 20)

            val newContent = listRepository.getContent(CollectionRequestParams(10, 20))

            assert(newContent is Resource.Success)
            assertEquals((newContent as Resource.Success).result.count, 10)
            assertEquals(newContent.result.offset, 10)
        }

    private fun createNItems(count: Int): List<Character> {
        val result = mutableListOf<Character>()
        for (i in 0 until count) {
            result.add(Mockito.mock(Character::class.java))
        }

        return result
    }

    private class EmptyNetworkRepository : CharacterListNetworkRepository {
        override suspend fun getCharacters(collectionQuery: CollectionRequestParams): Resource<Pager<Character>> =
            Resource.Error(ResourceError.EmptyContent)
    }
}