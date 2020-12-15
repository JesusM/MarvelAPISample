package com.marvelsample.app.core.repository.memory

import com.marvelsample.app.core.repository.model.base.Resource
import com.marvelsample.app.core.repository.model.base.error.ResourceError
import com.marvelsample.app.core.repository.base.Foo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CollectionMemoryRepositoryTest {
    @Test
    fun `should return empty if repository is empty`() {
        val collectionMemoryRepository = CollectionMemoryRepository<Foo>()

        val item = collectionMemoryRepository.getAll()
        assertTrue(item is Resource.Error)
        assertTrue((item as Resource.Error).error is ResourceError.EmptyContent)
    }

    @Test
    fun `should correctly return list`() {
        val collectionMemoryRepository = CollectionMemoryRepository<Foo>()
        collectionMemoryRepository.add(createNItems(5))

        val item = collectionMemoryRepository.getAll()
        assertTrue(item is Resource.Success)
        assertEquals(5, (item as Resource.Success).result.size)
    }

    @Test
    fun `should correctly add new content`() {
        val collectionMemoryRepository = CollectionMemoryRepository<Foo>()
        collectionMemoryRepository.add(createNItems(25))

        collectionMemoryRepository.add(createNItems(5, "key"))
        val newItems = collectionMemoryRepository.getAll()
        assertTrue(newItems is Resource.Success)
        assertEquals(30, (newItems as Resource.Success).result.size)
    }

    @Test
    fun `should not add duplicated content`() {
        val collectionMemoryRepository = CollectionMemoryRepository<Foo>()
        val items = createNItems(5)
        collectionMemoryRepository.add(items)
        collectionMemoryRepository.add(items)

        val newItems = collectionMemoryRepository.getAll()
        assertTrue(newItems is Resource.Success)
        assertEquals(5, (newItems as Resource.Success).result.size)
    }

    @Test
    fun `should add only non duplicated content`() {
        val collectionMemoryRepository = CollectionMemoryRepository<Foo>()
        val initialItems = createNItems(5)
        collectionMemoryRepository.add(initialItems)

        val toMutableList = initialItems.toMutableList()
        toMutableList.add(createItem("a"))
        collectionMemoryRepository.add(toMutableList)

        val newItems = collectionMemoryRepository.getAll()
        assertTrue(newItems is Resource.Success)
        assertEquals(6, (newItems as Resource.Success).result.size)
    }

    private fun createNItems(size: Int, keySeed: String = ""): List<Foo> {
        val list = mutableListOf<Foo>()
        for (i in 0 until size) {
            list.add(i, createItem(keySeed + i.toString()))
        }

        return list
    }

    private fun createItem(key: String): Foo = Foo(key)
}