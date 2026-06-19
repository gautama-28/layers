package com.shopease.app.ui.search

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.shopease.app.ui.components.ProductCard
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onNavigateToProductDetail: (String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is SearchEffect.NavigateToProductDetail -> onNavigateToProductDetail(effect.productId)
                is SearchEffect.ShowAddedToCartMessage ->
                    snackbarHostState.showSnackbar("${effect.productName} added to cart")
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Search") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Column {
                    OutlinedTextField(
                        value = state.query,
                        onValueChange = { viewModel.setEvent(SearchEvent.QueryChanged(it)) },
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        placeholder = { Text("Search products…") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Box(modifier = Modifier.fillMaxSize().padding(top = 16.dp)) {
                        when {
                            state.isSearching ->
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                            state.hasSearched && state.results.isEmpty() ->
                                Text(
                                    text = "No products found for \"${state.query}\"",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.align(Alignment.Center)
                                )

                            state.results.isNotEmpty() -> {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    contentPadding = PaddingValues(bottom = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(state.results, key = { it.id }) { product ->
                                        ProductCard(
                                            product = product,
                                            onClick = {
                                                viewModel.setEvent(SearchEvent.ProductClicked(product.id))
                                            },
                                            onAddToCartClick = {
                                                viewModel.setEvent(SearchEvent.AddToCartClicked(product))
                                            },
                                            sharedTransitionScope = sharedTransitionScope,
                                            animatedVisibilityScope = animatedVisibilityScope
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
