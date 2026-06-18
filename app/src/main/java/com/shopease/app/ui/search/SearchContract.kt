package com.shopease.app.ui.search

import com.shopease.app.domain.model.Product
import com.shopease.app.ui.base.UiEffect
import com.shopease.app.ui.base.UiEvent
import com.shopease.app.ui.base.UiState

data class SearchState(
    val query: String = "",
    val isSearching: Boolean = false,
    val results: List<Product> = emptyList(),
    val hasSearched: Boolean = false
) : UiState

sealed interface SearchEvent : UiEvent {
    data class QueryChanged(val query: String) : SearchEvent
    data class ProductClicked(val productId: String) : SearchEvent
    data class AddToCartClicked(val product: Product) : SearchEvent
}

sealed interface SearchEffect : UiEffect {
    data class NavigateToProductDetail(val productId: String) : SearchEffect
    data class ShowAddedToCartMessage(val productName: String) : SearchEffect
}
