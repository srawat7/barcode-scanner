package srawat.eng.mishi_challenge.domain

import srawat.eng.mishi_challenge.data.Item

interface ItemsRepository {
    suspend fun fetchAllItems(): List<Item>
    suspend fun fetchItem(title: String): Item
    suspend fun fetchItemById(id: Int): Item?
}