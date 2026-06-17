package com.shopease.app.ui.productlist

import androidx.lifecycle.viewModelScope
import com.shopease.app.domain.model.Product
import com.shopease.app.domain.repository.CartRepository
import com.shopease.app.domain.repository.ProductRepository
import com.shopease.app.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : BaseViewModel<ProductListState, ProductListEvent, ProductListEffect>(ProductListState()) {

    init {
        setEvent(ProductListEvent.LoadProducts)
    }

    override fun handleEvent(event: ProductListEvent) {
        when (event) {
            is ProductListEvent.LoadProducts -> loadProducts()
            is ProductListEvent.CategorySelected -> filterByCategory(event.categoryId)
            is ProductListEvent.ProductClicked -> setEffect {
                ProductListEffect.NavigateToProductDetail(event.productId)
            }
            is ProductListEvent.AddToCartClicked -> addToCart(event.product)
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            setState { copy(isLoading = true, errorMessage = null) }
            try {
                val categories = productRepository.getAllCategories()
                val products = productRepository.getAllProducts()
                setState {
                    copy(isLoading = false, products = products, categories = categories)
                }
            } catch (e: Exception) {
                setState { copy(isLoading = false, errorMessage = "Couldn't load products. Pull to refresh.") }
            }
        }
    }

    private fun filterByCategory(categoryId: String?) {
        viewModelScope.launch {
            setState { copy(isLoading = true, selectedCategoryId = categoryId) }
            val products = if (categoryId == null) {
                productRepository.getAllProducts()
            } else {
                productRepository.getProductsByCategory(categoryId)
            }
            setState { copy(isLoading = false, products = products) }
        }
    }

    private fun addToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(product)
            setEffect { ProductListEffect.ShowAddedToCartMessage(product.name) }
        }
    }
}
