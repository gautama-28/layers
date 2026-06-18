package com.shopease.app.ui.cart

import androidx.lifecycle.viewModelScope
import com.shopease.app.domain.repository.CartRepository
import com.shopease.app.ui.base.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository
) : BaseViewModel<CartState, CartEvent, CartEffect>(CartState()) {

    init {
        cartRepository.observeCartItems()
            .onEach { items -> setState { copy(items = items) } }
            .launchIn(viewModelScope)
    }

    override fun handleEvent(event: CartEvent) {
        when (event) {
            is CartEvent.IncrementQuantity ->
                updateQuantity(event.productId, event.currentQuantity + 1)
            is CartEvent.DecrementQuantity ->
                updateQuantity(event.productId, event.currentQuantity - 1)
            is CartEvent.RemoveClicked -> viewModelScope.launch {
                cartRepository.removeFromCart(event.productId)
            }
            is CartEvent.CheckoutClicked -> {
                if (currentState.items.isEmpty()) {
                    setEffect { CartEffect.ShowEmptyCartMessage }
                } else {
                    setEffect { CartEffect.NavigateToCheckout }
                }
            }
        }
    }

    private fun updateQuantity(productId: String, newQuantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(productId, newQuantity)
        }
    }
}
