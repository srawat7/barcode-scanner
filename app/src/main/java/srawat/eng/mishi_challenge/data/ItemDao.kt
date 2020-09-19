package srawat.eng.mishi_challenge.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ItemDao {
    @Query("SELECT * FROM item")
    fun getAll(): List<Item>

    @Query("SELECT * From item WHERE title = :value")
    fun getItemByTitle(value: String): Item

    @Query("SELECT * FROM item WHERE itemId = :value")
    fun getItemById(value: Int): Item

    @Insert
    fun insert(items: List<Item>)

    @Insert
    fun insert(item: Item)

    @Delete
    fun delete(item: Item)
}