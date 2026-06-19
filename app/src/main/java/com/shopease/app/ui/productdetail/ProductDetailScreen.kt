package com.shopease.app.ui.productdetail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.shopease.app.ui.components.SharedElementKeys
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun ProductDetailScreen(
    viewModel: ProductDetailViewModel,
    onNavigateBack: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is ProductDetailEffect.NavigateBack -> onNavigateBack()
                is ProductDetailEffect.ShowAddedToCartMessage ->
                    snackbarHostState.showSnackbar("${effect.productName} added to cart")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.product?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = { viewModel.setEvent(ProductDetailEvent.BackClicked) }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.setEvent(ProductDetailEvent.WishlistToggled) }) {
                        androidx.compose.animation.Crossfade(
                            targetState = state.isWishlisted,
                            label = "wishlist_heart"
                        ) { isWishlisted ->
                            Icon(
                                imageVector = if (isWishlisted) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Toggle wishlist"
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                state.errorMessage != null ->
                    Text(state.errorMessage ?: "", modifier = Modifier.align(Alignment.Center))
                state.product != null -> ProductDetailContent(
                    state,
                    viewModel,
                    sharedTransitionScope,
                    animatedVisibilityScope
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ProductDetailContent(
    state: ProductDetailState,
    viewModel: ProductDetailViewModel,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
) {
    val product = state.product ?: return
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        with(sharedTransitionScope) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(
                            key = SharedElementKeys.productImage(product.id)
                        ),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
            )
        }

        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = "₹${"%.0f".format(product.discountedPrice)}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            if (product.discountPercent > 0) {
                Text(
                    text = "₹${"%.0f".format(product.price)}",
                    style = MaterialTheme.typography.bodyLarge,
                    textDecoration = TextDecoration.LineThrough,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Text(
                    text = "${product.discountPercent}% off",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        Text(
            text = "★ ${product.rating} (${product.reviewCount} reviews)",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )

        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )

        // Quantity selector
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 24.dp)
        ) {
            OutlinedButton(onClick = { viewModel.setEvent(ProductDetailEvent.DecrementQuantity) }) {
                Text("−")
            }
            Text(text = state.quantity.toString(), style = MaterialTheme.typography.titleMedium)
            OutlinedButton(onClick = { viewModel.setEvent(ProductDetailEvent.IncrementQuantity) }) {
                Text("+")
            }
        }

        Button(
            onClick = { viewModel.setEvent(ProductDetailEvent.AddToCartClicked) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            enabled = product.inStock
        ) {
            Text(if (product.inStock) "Add to Cart" else "Out of Stock")
        }
    }
}
