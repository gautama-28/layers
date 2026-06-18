package com.shopease.app.data.repository

import com.shopease.app.data.local.dao.WishlistDao
import com.shopease.app.data.local.entity.WishlistItemEntity
import com.shopease.app.domain.model.Product
import com.shopease.app.domain.model.WishlistItem
import com.shopease.app.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WishlistRepositoryImpl(
    private val wishlistDao: WishlistDao
) : WishlistRepository {

    override fun observeWishlistItems(): Flow<List<WishlistItem>> =
        wishlistDao.observeWishlistItems().map { entities -> entities.map { it.toDomain() } }

    override fun observeIsWishlisted(productId: String): Flow<Boolean> =
        wishlistDao.observeIsWishlisted(productId)

    override suspend fun addToWishlist(product: Product) {
        wishlistDao.insert(
            WishlistItemEntity(
                productId = product.id,
                name = product.name,
                imageUrl = product.imageUrl,
                price = product.discountedPrice,
                addedAtEpochMillis = System.currentTimeMillis()
            )
        )
    }

    override suspend fun removeFromWishlist(productId: String) {
        wishlistDao.deleteByProductId(productId)
    }

    private fun WishlistItemEntity.toDomain() = WishlistItem(
        productId = productId,
        name = name,
        imageUrl = imageUrl,
        price = price
    )
}
