package srawat.eng.mishi_challenge.data

import srawat.eng.mishi_challenge.domain.ItemsRepository

class ItemsRepositoryImpl(private val itemsDataSource: ItemsDataSource) : ItemsRepository {
    override suspend fun fetchAllItems(): List<Item> {
        return itemsDataSource.fetchAllItems()
    }

    override suspend fun fetchItem(title: String): Item {
        return itemsDataSource.fetchItem(title)
    }

    override suspend fun fetchItemById(id: Int): Item? {
        return itemsDataSource.fetchItemById(id)
    }
}