package com.shopease.app.ui.productdetail

import com.shopease.app.domain.model.Product
import com.shopease.app.ui.base.UiEffect
import com.shopease.app.ui.base.UiEvent
import com.shopease.app.ui.base.UiState

data class ProductDetailState(
    val isLoading: Boolean = true,
    val product: Product? = null,
    val quantity: Int = 1,
    val isWishlisted: Boolean = false,
    val errorMessage: String? = null
) : UiState

sealed interface ProductDetailEvent : UiEvent {
    data class LoadProduct(val productId: String) : ProductDetailEvent
    data object IncrementQuantity : ProductDetailEvent
    data object DecrementQuantity : ProductDetailEvent
    data object AddToCartClicked : ProductDetailEvent
    data object WishlistToggled : ProductDetailEvent
    data object BackClicked : ProductDetailEvent
}

sealed interface ProductDetailEffect : UiEffect {
    data object NavigateBack : ProductDetailEffect
    data class ShowAddedToCartMessage(val productName: String) : ProductDetailEffect
}
