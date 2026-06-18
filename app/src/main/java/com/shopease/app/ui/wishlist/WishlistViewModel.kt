package com.shopease.app.ui.wishlist

import androidx.lifecycle.viewModelScope
import com.shopease.app.domain.repository.WishlistRepository
import com.shopease.app.ui.base.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class WishlistViewModel(
    private val wishlistRepository: WishlistRepository
) : BaseViewModel<WishlistState, WishlistEvent, WishlistEffect>(WishlistState()) {

    init {
        wishlistRepository.observeWishlistItems()
            .onEach { items -> setState { copy(items = items) } }
            .launchIn(viewModelScope)
    }

    override fun handleEvent(event: WishlistEvent) {
        when (event) {
            is WishlistEvent.ItemClicked ->
                setEffect { WishlistEffect.NavigateToProductDetail(event.productId) }
            is WishlistEvent.RemoveClicked -> viewModelScope.launch {
                wishlistRepository.removeFromWishlist(event.productId)
            }
        }
    }
}
