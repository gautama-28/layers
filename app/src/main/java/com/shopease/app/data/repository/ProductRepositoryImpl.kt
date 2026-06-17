package com.shopease.app.data.repository

import com.shopease.app.data.local.AssetDataSource
import com.shopease.app.domain.model.Category
import com.shopease.app.domain.model.Product
import com.shopease.app.domain.repository.ProductRepository

/**
 * Backed by the local JSON "catalog" in assets/. Products/categories are cached
 * in memory after the first load since the underlying files never change at
 * runtime — avoids re-parsing JSON on every screen visit.
 */
class ProductRepositoryImpl(
    private val assetDataSource: AssetDataSource
) : ProductRepository {

    private var cachedProducts: List<Product>? = null
    private var cachedCategories: List<Category>? = null

    private suspend fun products(): List<Product> =
        cachedProducts ?: assetDataSource.loadProducts().also { cachedProducts = it }

    private suspend fun categories(): List<Category> =
        cachedCategories ?: assetDataSource.loadCategories().also { cachedCategories = it }

    override suspend fun getAllProducts(): List<Product> = products()

    override suspend fun getProductById(productId: String): Product? =
        products().find { it.id == productId }

    override suspend fun getProductsByCategory(categoryId: String): List<Product> =
        products().filter { it.categoryId == categoryId }

    override suspend fun searchProducts(query: String): List<Product> {
        if (query.isBlank()) return emptyList()
        val lowerQuery = query.trim().lowercase()
        return products().filter {
            it.name.lowercase().contains(lowerQuery) ||
                it.description.lowercase().contains(lowerQuery)
        }
    }

    override suspend fun getAllCategories(): List<Category> = categories()
}
