package srawat.eng.mishi_challenge.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors

@Database(entities = arrayOf(Item::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemsDao(): ItemDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun get(context: Context): AppDatabase {
            Log.d("Shivam", "Database get was called")
            if(instance == null) {
                Log.d("Shivam", "database instance was null")
                instance =
                    Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "MishiDb")
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                Log.d("Shivam", "Populating")
                                populateDb(context)
                            }
                        }).build()

            }

            return instance!!
        }

        private fun populateDb(context: Context) {
            val item1 = Item(1,"Google Pixel 4a", 400.00, "file:///android_asset/pixel_4a.jpg")
            val item2 = Item(2, "Broccoli Soup",  8.00, "file:///android_asset/broccoli_soup.jpeg")
            val item3 = Item(3,"Nike Shoes", 120.00, "file:///android_asset/nike_shoes.jpeg")
            val item4 = Item(4, "Banana",  2.00, "file:///android_asset/banana.png")

            val l = listOf<Item>(item1, item2, item3, item4)
            Executors.newSingleThreadExecutor().execute {
                get(context).itemsDao().insert(l)
            }
        }
    }
}