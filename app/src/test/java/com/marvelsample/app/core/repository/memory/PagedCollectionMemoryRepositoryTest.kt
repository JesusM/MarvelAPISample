package com.marvelsample.app.core.repository.memory

import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError
import com.marvelsample.app.core.repository.base.Foo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PagedCollectionMemoryRepositoryTest {
    @Test
    fun `should return empty if repository is empty`() {
        val itemMemoryRepository = PagedCollectionMemoryRepository<Foo>()

        val item = itemMemoryRepository.getPage(1, 25)
        assertTrue(item is Resource.Error)
        assertTrue((item as Resource.Error).error is ResourceError.EmptyContent)
    }

    @Test
    fun `should return content if request size is lower then request params`() {
        val itemMemoryRepository = PagedCollectionMemoryRepository<Foo>()
        itemMemoryRepository.add(createNItems(5))

        val item = itemMemoryRepository.getPage(0, 25)
        assertTrue(item is Resource.Success)
        assertEquals(5, (item as Resource.Success).result.count)
    }

    @Test
    fun `should return list if request is in page`() {
        val itemMemoryRepository = PagedCollectionMemoryRepository<Foo>()
        itemMemoryRepository.add(createNItems(5))

        val item = itemMemoryRepository.getPage(0, 5)
        assertTrue(item is Resource.Success)
        assertEquals(5, (item as Resource.Success).result.count)
    }

    @Test
    fun `should return list in page if request is correct and full list is bigger`() {
        val collectionMemoryRepository = PagedCollectionMemoryRepository<Foo>()
        collectionMemoryRepository.add(createNItems(25))

        val item = collectionMemoryRepository.getPage(1, 5)
        assertTrue(item is Resource.Success)
        assertEquals(5, (item as Resource.Success).result.count)
    }

    @Test
    fun `should return all content in page if request is default and full list is bigger`() {
        val collectionMemoryRepository = PagedCollectionMemoryRepository<Foo>()
        collectionMemoryRepository.add(createNItems(25))

        val item = collectionMemoryRepository.getPage(limit = 20)
        assertTrue(item is Resource.Success)
        assertEquals(20, (item as Resource.Success).result.count)
    }

    @Test
    fun `should return all content in page if request params are greater than list full size`() {
        val collectionMemoryRepository = PagedCollectionMemoryRepository<Foo>()
        collectionMemoryRepository.add(createNItems(5))

        val item = collectionMemoryRepository.getPage(limit = 6)
        assertTrue(item is Resource.Success)
        assertEquals(5, (item as Resource.Success).result.count)
    }

    @Test
    fun `should return empty page if request is greater than full list size`() {
        val collectionMemoryRepository = PagedCollectionMemoryRepository<Foo>()
        collectionMemoryRepository.add(createNItems(5))

        val item = collectionMemoryRepository.getPage(5, 5)
        assertTrue(item is Resource.Error)
        assertTrue((item as Resource.Error).error is ResourceError.EmptyContent)
    }

    @Test
    fun `should return correct sublist after adding content to repository already full`() {
        val itemMemoryRepository = PagedCollectionMemoryRepository<Foo>(15)
        itemMemoryRepository.add(createNItems(5))
        itemMemoryRepository.add(createNItems(8))
        val lastItemsAdded = createNItems(5)
        itemMemoryRepository.add(lastItemsAdded)

        val page = itemMemoryRepository.getPage(2, 5)
        assertTrue(page is Resource.Success)
        assertEquals(lastItemsAdded, (page as Resource.Success).result.results)
    }

    @Test
    fun `should not increase total size adding content to repository already full`() {
        val itemMemoryRepository = PagedCollectionMemoryRepository<Foo>(15)
        itemMemoryRepository.add(createNItems(5))
        itemMemoryRepository.add(createNItems(8))
        val lastItemsAdded = createNItems(5)
        itemMemoryRepository.add(lastItemsAdded)

        assertEquals(15, itemMemoryRepository.totalSize())
    }

    private fun createNItems(size: Int): List<Foo> {
        val list = mutableListOf<Foo>()
        for (i in 0 until size) {
            list.add(i, Foo(i.toString()))
        }

        return list
    }
}