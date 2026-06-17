package com.shopease.app.ui.productlist

import com.shopease.app.domain.model.Category
import com.shopease.app.domain.model.Product
import com.shopease.app.ui.base.UiEffect
import com.shopease.app.ui.base.UiEvent
import com.shopease.app.ui.base.UiState

data class ProductListState(
    val isLoading: Boolean = true,
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null, // null = "All"
    val errorMessage: String? = null
) : UiState

sealed interface ProductListEvent : UiEvent {
    data object LoadProducts : ProductListEvent
    data class CategorySelected(val categoryId: String?) : ProductListEvent
    data class ProductClicked(val productId: String) : ProductListEvent
    data class AddToCartClicked(val product: Product) : ProductListEvent
}

sealed interface ProductListEffect : UiEffect {
    data class NavigateToProductDetail(val productId: String) : ProductListEffect
    data class ShowAddedToCartMessage(val productName: String) : ProductListEffect
}
