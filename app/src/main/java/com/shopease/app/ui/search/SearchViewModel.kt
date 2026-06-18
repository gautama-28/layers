package com.shopease.app.ui.search

import androidx.lifecycle.viewModelScope
import com.shopease.app.domain.repository.CartRepository
import com.shopease.app.domain.repository.ProductRepository
import com.shopease.app.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SEARCH_DEBOUNCE_MS = 350L

class SearchViewModel(
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository
) : BaseViewModel<SearchState, SearchEvent, SearchEffect>(SearchState()) {

    private var searchJob: Job? = null

    override fun handleEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.QueryChanged -> onQueryChanged(event.query)
            is SearchEvent.ProductClicked ->
                setEffect { SearchEffect.NavigateToProductDetail(event.productId) }
            is SearchEvent.AddToCartClicked -> viewModelScope.launch {
                cartRepository.addToCart(event.product)
                setEffect { SearchEffect.ShowAddedToCartMessage(event.product.name) }
            }
        }
    }

    private fun onQueryChanged(query: String) {
        setState { copy(query = query) }
        searchJob?.cancel()

        if (query.isBlank()) {
            setState { copy(results = emptyList(), isSearching = false, hasSearched = false) }
            return
        }

        searchJob = viewModelScope.launch {
            setState { copy(isSearching = true) }
            delay(SEARCH_DEBOUNCE_MS) // debounce so we don't hit the "backend" on every keystroke
            val results = productRepository.searchProducts(query)
            setState { copy(isSearching = false, results = results, hasSearched = true) }
        }
    }
}
