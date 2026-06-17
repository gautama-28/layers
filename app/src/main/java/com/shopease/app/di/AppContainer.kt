package com.shopease.app.di

import android.content.Context
import com.shopease.app.data.local.AssetDataSource
import com.shopease.app.data.local.ShopEaseDatabase
import com.shopease.app.data.local.datastore.UserPreferencesDataStore
import com.shopease.app.data.repository.CartRepositoryImpl
import com.shopease.app.data.repository.ProductRepositoryImpl
import com.shopease.app.data.repository.UserPreferencesRepositoryImpl
import com.shopease.app.domain.repository.CartRepository
import com.shopease.app.domain.repository.ProductRepository
import com.shopease.app.domain.repository.UserPreferencesRepository

/**
 * Simple manual dependency container (ServiceLocator pattern).
 *
 * We're not pulling in Hilt/Koin for this project — with a handful of repositories
 * a plain container is easier to reason about and explain than DI-framework magic.
 * Everything below is created lazily and only once per process, mirroring what
 * a Hilt @Singleton binding would give you, just without the codegen.
 */
class AppContainer(private val context: Context) {

    private val database: ShopEaseDatabase by lazy {
        ShopEaseDatabase.getInstance(context)
    }

    private val assetDataSource: AssetDataSource by lazy {
        AssetDataSource(context)
    }

    private val userPreferencesDataStore: UserPreferencesDataStore by lazy {
        UserPreferencesDataStore(context)
    }

    // TODO Phase 5/7: expose WishlistRepository (backed by Room, added in a later migration)

    val productRepository: ProductRepository by lazy {
        ProductRepositoryImpl(assetDataSource)
    }

    val cartRepository: CartRepository by lazy {
        CartRepositoryImpl(database.cartDao())
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepositoryImpl(userPreferencesDataStore)
    }
}
