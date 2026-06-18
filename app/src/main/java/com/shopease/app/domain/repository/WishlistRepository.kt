package com.shopease.app.domain.repository

import com.shopease.app.domain.model.Product
import com.shopease.app.domain.model.WishlistItem
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun observeWishlistItems(): Flow<List<WishlistItem>>
    fun observeIsWishlisted(productId: String): Flow<Boolean>
    suspend fun addToWishlist(product: Product)
    suspend fun removeFromWishlist(productId: String)
}
