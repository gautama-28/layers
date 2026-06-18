package com.shopease.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shopease.app.data.local.entity.WishlistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Query("SELECT * FROM wishlist_items ORDER BY addedAtEpochMillis DESC")
    fun observeWishlistItems(): Flow<List<WishlistItemEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE productId = :productId)")
    fun observeIsWishlisted(productId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WishlistItemEntity)

    @Query("DELETE FROM wishlist_items WHERE productId = :productId")
    suspend fun deleteByProductId(productId: String)
}
