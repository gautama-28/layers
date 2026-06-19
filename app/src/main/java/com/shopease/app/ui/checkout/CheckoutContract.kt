package com.shopease.app.ui.checkout

import com.shopease.app.domain.model.CartItem
import com.shopease.app.ui.base.UiEffect
import com.shopease.app.ui.base.UiEvent
import com.shopease.app.ui.base.UiState

enum class PaymentMethod(val displayName: String) {
    CASH_ON_DELIVERY("Cash on Delivery"),
    UPI("UPI"),
    CARD("Credit / Debit Card")
}

data class CheckoutState(
    val items: List<CartItem> = emptyList(),
    val addressLine: String = "",
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.CASH_ON_DELIVERY,
    val isPlacingOrder: Boolean = false,
    val orderPlaced: Boolean = false
) : UiState {
    val subtotal: Double
        get() = items.sumOf { it.lineTotal }

    val deliveryFee: Double
        get() = if (subtotal >= 999.0 || subtotal == 0.0) 0.0 else 49.0

    val total: Double
        get() = subtotal + deliveryFee

    val isAddressValid: Boolean
        get() = addressLine.trim().length >= 10
}

sealed interface CheckoutEvent : UiEvent {
    data class AddressChanged(val addressLine: String) : CheckoutEvent
    data class PaymentMethodSelected(val method: PaymentMethod) : CheckoutEvent
    data object PlaceOrderClicked : CheckoutEvent
}

sealed interface CheckoutEffect : UiEffect {
    data object NavigateToProductListAfterOrder : CheckoutEffect
    data object ShowInvalidAddressMessage : CheckoutEffect
}
