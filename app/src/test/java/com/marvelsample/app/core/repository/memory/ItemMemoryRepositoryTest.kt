package com.marvelsample.app.core.repository.memory

import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.base.error.ResourceError
import com.marvelsample.app.core.repository.base.Foo
import org.junit.Assert.*
import org.junit.Test

class ItemMemoryRepositoryTest {
    private val defaultKey = "key"

    @Test
    fun `should return empty if repository is empty`() {
        val itemMemoryRepository = ItemMemoryRepository<String, Foo>()

        val item = itemMemoryRepository.get("foo")
        assertTrue(item is Resource.Error)
        assertTrue((item as Resource.Error).error is ResourceError.EmptyContent)
    }

    @Test
    fun `should return empty if id not present in repository`() {
        val itemMemoryRepository =
            ItemMemoryRepository<String, Foo>()
        itemMemoryRepository.add(defaultKey, createFooItemWithKey(defaultKey))

        val item = itemMemoryRepository.get("foo")
        assertTrue(item is Resource.Error)
        assertTrue((item as Resource.Error).error is ResourceError.EmptyContent)
    }

    @Test
    fun `should return correct element`() {
        val itemMemoryRepository = ItemMemoryRepository<String, Foo>()
        itemMemoryRepository.add(defaultKey, createFooItemWithKey(defaultKey))

        val item = itemMemoryRepository.get(defaultKey)
        assertTrue(item is Resource.Success)
        assertEquals(defaultKey, (item as Resource.Success).result.id)
    }

    @Test
    fun `should not add new item if it already exists in repository`() {
        val itemMemoryRepository = ItemMemoryRepository<String, Foo>()
        itemMemoryRepository.add(defaultKey, createFooItemWithKey(defaultKey))
        itemMemoryRepository.add(defaultKey, createFooItemWithKey(defaultKey))
        assertEquals(1, itemMemoryRepository.size())
    }

    @Test
    fun `should correctly add element if it already doesn't exist`() {
        val itemMemoryRepository = ItemMemoryRepository<String, Foo>()
        itemMemoryRepository.add(defaultKey, createFooItemWithKey(defaultKey))
        itemMemoryRepository.add("key1", createFooItemWithKey("key1"))

        assertEquals(2, itemMemoryRepository.size())
    }

    @Test
    fun `should correctly add element when repository is already full`() {
        val itemMemoryRepository = ItemMemoryRepository<String, Foo>(2)
        val expectedItemKey = "key3"

        itemMemoryRepository.add("key1", createFooItemWithKey("key1"))
        itemMemoryRepository.add("key2", createFooItemWithKey("key2"))
        itemMemoryRepository.add(expectedItemKey, createFooItemWithKey(expectedItemKey))

        assertEquals(2, itemMemoryRepository.size())

        val item = itemMemoryRepository.get(expectedItemKey)
        assertNotNull(item)
    }

    private fun createFooItemWithKey(key: String) = Foo(key)
}