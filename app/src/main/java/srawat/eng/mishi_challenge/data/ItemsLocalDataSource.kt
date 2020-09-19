package srawat.eng.mishi_challenge.data

class ItemsLocalDataSource(val dao: ItemDao) : ItemsDataSource {
    override suspend fun fetchAllItems(): List<Item> {
        return dao.getAll()
    }

    override suspend fun fetchItem(title: String): Item {
        return dao.getItemByTitle(title)
    }

    override suspend fun fetchItemById(id: Int): Item {
        return dao.getItemById(id)
    }
}