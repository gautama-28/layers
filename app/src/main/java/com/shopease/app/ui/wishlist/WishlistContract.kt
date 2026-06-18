package com.shopease.app.ui.wishlist

import com.shopease.app.domain.model.WishlistItem
import com.shopease.app.ui.base.UiEffect
import com.shopease.app.ui.base.UiEvent
import com.shopease.app.ui.base.UiState

data class WishlistState(
    val items: List<WishlistItem> = emptyList()
) : UiState

sealed interface WishlistEvent : UiEvent {
    data class ItemClicked(val productId: String) : WishlistEvent
    data class RemoveClicked(val productId: String) : WishlistEvent
}

sealed interface WishlistEffect : UiEffect {
    data class NavigateToProductDetail(val productId: String) : WishlistEffect
}
