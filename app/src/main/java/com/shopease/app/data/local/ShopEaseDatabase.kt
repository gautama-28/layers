package com.shopease.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.shopease.app.data.local.dao.CartDao
import com.shopease.app.data.local.dao.WishlistDao
import com.shopease.app.data.local.entity.CartItemEntity
import com.shopease.app.data.local.entity.WishlistItemEntity

/**
 * Room database for offline persistence.
 *
 * v1 -> v2: added the wishlist_items table once the Wishlist feature was built.
 * Using a real Migration (instead of fallbackToDestructiveMigration) so upgrading
 * the app doesn't wipe an existing user's cart — a realistic thing to have to
 * handle and explain in an interview.
 */
@Database(
    entities = [CartItemEntity::class, WishlistItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class ShopEaseDatabase : RoomDatabase() {

    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao

    companion object {
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `wishlist_items` (
                        `productId` TEXT NOT NULL,
                        `name` TEXT NOT NULL,
                        `imageUrl` TEXT NOT NULL,
                        `price` REAL NOT NULL,
                        `addedAtEpochMillis` INTEGER NOT NULL,
                        PRIMARY KEY(`productId`)
                    )
                    """.trimIndent()
                )
            }
        }

        @Volatile
        private var INSTANCE: ShopEaseDatabase? = null

        fun getInstance(context: Context): ShopEaseDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ShopEaseDatabase::class.java,
                    "shopease.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
