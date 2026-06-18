package com.shopease.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
    @PrimaryKey
    val productId: String,
    val name: String,
    val imageUrl: String,
    val price: Double,
    val addedAtEpochMillis: Long
)
