package com.shopease.app.ui.cart

import com.shopease.app.domain.model.CartItem
import com.shopease.app.ui.base.UiEffect
import com.shopease.app.ui.base.UiEvent
import com.shopease.app.ui.base.UiState

data class CartState(
    val items: List<CartItem> = emptyList()
) : UiState {
    val subtotal: Double
        get() = items.sumOf { it.lineTotal }

    val itemCount: Int
        get() = items.sumOf { it.quantity }
}

sealed interface CartEvent : UiEvent {
    data class IncrementQuantity(val productId: String, val currentQuantity: Int) : CartEvent
    data class DecrementQuantity(val productId: String, val currentQuantity: Int) : CartEvent
    data class RemoveClicked(val productId: String) : CartEvent
    data object CheckoutClicked : CartEvent
}

sealed interface CartEffect : UiEffect {
    data object NavigateToCheckout : CartEffect
    data object ShowEmptyCartMessage : CartEffect
}
