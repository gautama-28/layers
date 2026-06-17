package com.shopease.app.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val discountPercent: Int = 0,
    val categoryId: String,
    val imageUrl: String,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val inStock: Boolean = true
) {
    val discountedPrice: Double
        get() = price * (1 - discountPercent / 100.0)
}
