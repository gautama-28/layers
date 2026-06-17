package com.shopease.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shopease.app.data.local.dao.CartDao
import com.shopease.app.data.local.entity.CartItemEntity

/**
 * Room database for offline persistence. Only the cart lives here for now;
 * WishlistEntity gets added in a later migration once the wishlist feature lands.
 */
@Database(
    entities = [CartItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ShopEaseDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao

    companion object {
        @Volatile
        private var INSTANCE: ShopEaseDatabase? = null

        fun getInstance(context: Context): ShopEaseDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ShopEaseDatabase::class.java,
                    "shopease.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
