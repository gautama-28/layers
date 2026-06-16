package com.shopease.app.di

import android.content.Context

/**
 * Simple manual dependency container (ServiceLocator pattern).
 *
 * We're not pulling in Hilt/Koin for this project — with a handful of repositories
 * a plain container is easier to reason about and explain than DI-framework magic.
 * Repositories, Room database, and DataStore instances will be wired in here as
 * each data-layer piece is built (Phase 2 onward).
 */
class AppContainer(private val context: Context) {

    // TODO Phase 2: expose ProductRepository (backed by local JSON assets)
    // TODO Phase 2: expose CartRepository (backed by Room)
    // TODO Phase 2: expose WishlistRepository (backed by Room)
    // TODO Phase 2: expose UserPreferencesRepository (backed by DataStore)
}
