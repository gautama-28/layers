package com.shopease.app.domain.model

data class WishlistItem(
    val productId: String,
    val name: String,
    val imageUrl: String,
    val price: Double
)
