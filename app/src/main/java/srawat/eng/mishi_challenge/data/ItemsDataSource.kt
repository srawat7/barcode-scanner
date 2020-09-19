package srawat.eng.mishi_challenge.data

interface ItemsDataSource {
    suspend fun fetchAllItems(): List<Item>
    suspend fun fetchItem(title: String): Item
    suspend fun fetchItemById(id: Int): Item
}