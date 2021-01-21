package com.marvelsample.app.core.repository.memory

import com.marvelsample.app.core.model.base.Resource
import com.marvelsample.app.core.model.base.error.ResourceError

open class ItemMemoryRepository<K, T>(
    private val size: Int = 75,
    private val items: HashMap<K, T> = hashMapOf()
) {
    open fun get(itemId: K): Resource<T> =
        if (items.isEmpty() || items.containsKey(itemId).not()) {
            Resource.Error(ResourceError.EmptyContent)
        } else {
            val item = items[itemId]
            if (item == null) {
                Resource.Error(ResourceError.EmptyContent)
            } else {
                Resource.Success(item)
            }
        }

    fun add(itemId: K, item: T) {
        if (items.size >= size) {
            val firstKey = items.keys.first()
            items.remove(firstKey)
        }

        items[itemId] = item
    }

    fun size(): Int = items.size

    fun remove(id: K) = items.remove(id)
}