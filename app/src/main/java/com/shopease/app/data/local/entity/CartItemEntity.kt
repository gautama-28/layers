package com.shopease.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * A row in the cart table. We store a denormalized snapshot of the product's
 * price/name/image at add-to-cart time rather than just a foreign key, so the
 * cart still displays correctly even if the "catalog" (our local JSON) changes
 * between app updates — mirrors how a real e-commerce cart survives price changes
 * mid-session.
 */
@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val productId: String,
    val name: String,
    val imageUrl: String,
    val unitPrice: Double,
    val quantity: Int,
    val addedAtEpochMillis: Long
)
