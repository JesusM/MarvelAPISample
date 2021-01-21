package com.marvelsample.app.core.repository.memory

import com.marvelsample.app.core.model.base.Pager
import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError

open class PagedCollectionMemoryRepository<T>(
    private val size: Int = 200,
    items: MutableList<T> = mutableListOf()
) : CollectionMemoryRepository<T>(items) {

    open fun getPage(offset: Int = 0, limit: Int = 25): (Resource<Pager<T>>) =
        if (isEmpty()) {
            Resource.Error(ResourceError.EmptyContent)
        } else {
            if (offset >= totalSize()) {
                Resource.Error(ResourceError.EmptyContent)
            } else {
                val toIndex = (offset + limit).coerceAtMost(totalSize())
                val subList = subList(offset, toIndex)
                Resource.Success(Pager(subList, offset, toIndex, subList.size, totalSize()))
            }
        }

    override fun add(newItems: List<T>): Boolean {
        if (totalSize() + newItems.size >= size) {
            val itemsToRemove = (totalSize() + newItems.size) - size
            for (index in 1..itemsToRemove) {
                removeAt(0)
            }
        }

        return super.add(newItems)
    }

    override fun shouldAddItem(it: T): Boolean = true
}