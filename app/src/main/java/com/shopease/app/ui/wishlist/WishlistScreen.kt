package com.shopease.app.ui.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    viewModel: WishlistViewModel,
    onNavigateToProductDetail: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is WishlistEffect.NavigateToProductDetail -> onNavigateToProductDetail(effect.productId)
            }
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Wishlist") }) }) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (state.items.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Text(
                        text = "Your wishlist is empty",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                    Text(
                        text = "Tap the heart icon on any product to save it here.",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.items, key = { it.productId }) { item ->
                        Card(
                            onClick = { viewModel.setEvent(WishlistEvent.ItemClicked(item.productId)) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                AsyncImage(
                                    model = item.imageUrl,
                                    contentDescription = item.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 12.dp)
                                ) {
                                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                                    Text(
                                        "₹${"%.0f".format(item.price)}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                IconButton(onClick = {
                                    viewModel.setEvent(WishlistEvent.RemoveClicked(item.productId))
                                }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Remove from wishlist")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
