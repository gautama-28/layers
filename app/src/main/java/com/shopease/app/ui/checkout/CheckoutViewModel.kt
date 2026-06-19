package com.shopease.app.ui.checkout

import androidx.lifecycle.viewModelScope
import com.shopease.app.domain.repository.CartRepository
import com.shopease.app.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val SIMULATED_ORDER_PROCESSING_MS = 900L

class CheckoutViewModel(
    private val cartRepository: CartRepository
) : BaseViewModel<CheckoutState, CheckoutEvent, CheckoutEffect>(CheckoutState()) {

    init {
        cartRepository.observeCartItems()
            .onEach { items -> setState { copy(items = items) } }
            .launchIn(viewModelScope)
    }

    override fun handleEvent(event: CheckoutEvent) {
        when (event) {
            is CheckoutEvent.AddressChanged -> setState { copy(addressLine = event.addressLine) }
            is CheckoutEvent.PaymentMethodSelected ->
                setState { copy(selectedPaymentMethod = event.method) }
            is CheckoutEvent.PlaceOrderClicked -> placeOrder()
        }
    }

    private fun placeOrder() {
        if (!currentState.isAddressValid) {
            setEffect { CheckoutEffect.ShowInvalidAddressMessage }
            return
        }
        viewModelScope.launch {
            setState { copy(isPlacingOrder = true) }
            // No real payment gateway here — this simulates order processing
            // latency so the UI has something honest to show (a spinner) rather
            // than an instant, unrealistic "success".
            delay(SIMULATED_ORDER_PROCESSING_MS)
            cartRepository.clearCart()
            setState { copy(isPlacingOrder = false, orderPlaced = true) }
            setEffect { CheckoutEffect.NavigateToProductListAfterOrder }
        }
    }
}
