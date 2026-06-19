package com.shopease.app.ui.productlist

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shopease.app.domain.model.Product
import com.shopease.app.ui.components.ProductCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel,
    onNavigateToProductDetail: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProductListEffect.NavigateToProductDetail ->
                    onNavigateToProductDetail(effect.productId)
                is ProductListEffect.ShowAddedToCartMessage ->
                    snackbarHostState.showSnackbar("${effect.productName} added to cart")
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("ShopEase") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                state.errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = state.errorMessage ?: "",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
                else -> {
                    ProductListContent(
                        state = state,
                        onCategorySelected = { viewModel.setEvent(ProductListEvent.CategorySelected(it)) },
                        onProductClick = { viewModel.setEvent(ProductListEvent.ProductClicked(it)) },
                        onAddToCartClick = { viewModel.setEvent(ProductListEvent.AddToCartClicked(it)) },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ProductListContent(
    state: ProductListState,
    onCategorySelected: (String?) -> Unit,
    onProductClick: (String) -> Unit,
    onAddToCartClick: (Product) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Category filter row spans both grid columns
            item(span = { GridItemSpan(2) }) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = state.selectedCategoryId == null,
                            onClick = { onCategorySelected(null) },
                            label = { Text("All") }
                        )
                    }
                    items(state.categories) { category ->
                        FilterChip(
                            selected = state.selectedCategoryId == category.id,
                            onClick = { onCategorySelected(category.id) },
                            label = { Text(category.name) }
                        )
                    }
                }
            }

            items(state.products, key = { it.id }) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product.id) },
                    onAddToCartClick = { onAddToCartClick(product) },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }
        }
    }
}
