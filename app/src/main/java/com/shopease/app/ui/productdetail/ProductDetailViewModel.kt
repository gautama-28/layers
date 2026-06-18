package com.shopease.app.ui.productdetail

import androidx.lifecycle.viewModelScope
import com.shopease.app.domain.repository.CartRepository
import com.shopease.app.domain.repository.ProductRepository
import com.shopease.app.domain.repository.WishlistRepository
import com.shopease.app.ui.base.BaseViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productId: String,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    private val wishlistRepository: WishlistRepository
) : BaseViewModel<ProductDetailState, ProductDetailEvent, ProductDetailEffect>(ProductDetailState()) {

    init {
        setEvent(ProductDetailEvent.LoadProduct(productId))
        wishlistRepository.observeIsWishlisted(productId)
            .onEach { isWishlisted -> setState { copy(isWishlisted = isWishlisted) } }
            .launchIn(viewModelScope)
    }

    override fun handleEvent(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.LoadProduct -> loadProduct(event.productId)
            is ProductDetailEvent.IncrementQuantity -> setState { copy(quantity = quantity + 1) }
            is ProductDetailEvent.DecrementQuantity ->
                setState { copy(quantity = (quantity - 1).coerceAtLeast(1)) }
            is ProductDetailEvent.AddToCartClicked -> addToCart()
            is ProductDetailEvent.WishlistToggled -> toggleWishlist()
            is ProductDetailEvent.BackClicked -> setEffect { ProductDetailEffect.NavigateBack }
        }
    }

    private fun toggleWishlist() {
        val product = currentState.product ?: return
        viewModelScope.launch {
            if (currentState.isWishlisted) {
                wishlistRepository.removeFromWishlist(product.id)
            } else {
                wishlistRepository.addToWishlist(product)
            }
        }
    }

    private fun loadProduct(productId: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            val product = productRepository.getProductById(productId)
            if (product == null) {
                setState { copy(isLoading = false, errorMessage = "Product not found") }
            } else {
                setState { copy(isLoading = false, product = product) }
            }
        }
    }

    private fun addToCart() {
        val product = currentState.product ?: return
        viewModelScope.launch {
            cartRepository.addToCart(product, currentState.quantity)
            setEffect { ProductDetailEffect.ShowAddedToCartMessage(product.name) }
        }
    }
}
