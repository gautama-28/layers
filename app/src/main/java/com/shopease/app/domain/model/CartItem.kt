package com.shopease.app.domain.model

/**
 * Domain-layer representation of a cart line item. Kept distinct from
 * CartItemEntity so the UI/domain layers never depend directly on a Room
 * annotation class — the repository maps between the two.
 */
data class CartItem(
    val productId: String,
    val name: String,
    val imageUrl: String,
    val unitPrice: Double,
    val quantity: Int
) {
    val lineTotal: Double
        get() = unitPrice * quantity
}
