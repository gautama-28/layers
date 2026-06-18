package com.shopease.app.domain.repository

import com.shopease.app.domain.model.Category
import com.shopease.app.domain.model.Product

interface ProductRepository {
    suspend fun getAllProducts(): List<Product>
    suspend fun getProductById(productId: String): Product?
    suspend fun getProductsByCategory(categoryId: String): List<Product>
    suspend fun searchProducts(query: String): List<Product>
    suspend fun getAllCategories(): List<Category>
}
