package com.marvelsample.app.core.repository.memory

import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError

open class CollectionMemoryRepository<T>(
    private val items: MutableList<T> = mutableListOf()
) {
    open fun getAll(): Resource<List<T>> =
        if (items.isEmpty()) {
            Resource.Error(ResourceError.EmptyContent)
        } else {
            Resource.Success(items.toList())
        }

    open fun add(newItems: List<T>): Boolean {
        val newItemsToAdd = newItems.filter { newItem ->
            shouldAddItem(newItem)
        }

        return if (newItemsToAdd.isEmpty()) {
            false
        } else {
            items.addAll(newItemsToAdd)
            true
        }
    }

    protected fun removeAt(index: Int) {
        items.removeAt(index)
    }

    protected fun subList(fromIndex: Int, toIndex: Int): List<T> = items.subList(fromIndex, toIndex)

    open fun shouldAddItem(it: T): Boolean = !items.contains(it)

    fun totalSize(): Int = items.size

    fun isEmpty(): Boolean = totalSize() == 0
}