package com.shopease.app.data.repository

import com.shopease.app.data.local.dao.CartDao
import com.shopease.app.data.local.entity.CartItemEntity
import com.shopease.app.domain.model.CartItem
import com.shopease.app.domain.model.Product
import com.shopease.app.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {

    override fun observeCartItems(): Flow<List<CartItem>> =
        cartDao.observeCartItems().map { entities -> entities.map { it.toDomain() } }

    override suspend fun addToCart(product: Product, quantity: Int) {
        val existing = cartDao.getCartItem(product.id)
        if (existing != null) {
            cartDao.update(existing.copy(quantity = existing.quantity + quantity))
        } else {
            cartDao.upsert(
                CartItemEntity(
                    productId = product.id,
                    name = product.name,
                    imageUrl = product.imageUrl,
                    unitPrice = product.discountedPrice,
                    quantity = quantity,
                    addedAtEpochMillis = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteByProductId(productId)
            return
        }
        val existing = cartDao.getCartItem(productId) ?: return
        cartDao.update(existing.copy(quantity = quantity))
    }

    override suspend fun removeFromCart(productId: String) {
        cartDao.deleteByProductId(productId)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }

    private fun CartItemEntity.toDomain() = CartItem(
        productId = productId,
        name = name,
        imageUrl = imageUrl,
        unitPrice = unitPrice,
        quantity = quantity
    )
}
