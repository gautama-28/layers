package com.shopease.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Root NavHost. Only ProductList is wired to a real (placeholder) composable for now;
 * remaining screens get filled in as each feature is built (Phases 4-6).
 */
@Composable
fun ShopEaseNavHost(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.ProductList.route
    ) {
        composable(Screen.ProductList.route) {
            PlaceholderScreen(label = "Product List — coming in Phase 4")
        }
        composable(Screen.Search.route) {
            PlaceholderScreen(label = "Search — coming in Phase 4")
        }
        composable(Screen.Wishlist.route) {
            PlaceholderScreen(label = "Wishlist — coming in Phase 5")
        }
        composable(Screen.Cart.route) {
            PlaceholderScreen(label = "Cart — coming in Phase 5")
        }
        composable(Screen.Checkout.route) {
            PlaceholderScreen(label = "Checkout — coming in Phase 6")
        }
        composable(Screen.Profile.route) {
            PlaceholderScreen(label = "Profile — coming in Phase 6")
        }
    }
}

@Composable
private fun PlaceholderScreen(label: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label)
    }
}
