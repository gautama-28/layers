package com.shopease.app.domain.repository

import com.shopease.app.domain.model.CartItem
import com.shopease.app.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(product: Product, quantity: Int = 1)
    suspend fun updateQuantity(productId: String, quantity: Int)
    suspend fun removeFromCart(productId: String)
    suspend fun clearCart()
}
