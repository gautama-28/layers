package com.shopease.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shopease.app.ShopEaseApplication
import com.shopease.app.ui.base.ViewModelFactory
import com.shopease.app.ui.productdetail.ProductDetailScreen
import com.shopease.app.ui.productdetail.ProductDetailViewModel
import com.shopease.app.ui.productlist.ProductListScreen
import com.shopease.app.ui.productlist.ProductListViewModel
import com.shopease.app.ui.search.SearchScreen
import com.shopease.app.ui.search.SearchViewModel

/**
 * Root NavHost. ProductList is fully wired (Phase 4); remaining screens get
 * filled in as each feature is built (Phases 5-6).
 */
@Composable
fun ShopEaseNavHost(
    navController: NavHostController = rememberNavController()
) {
    val container = (LocalContext.current.applicationContext as ShopEaseApplication).container

    NavHost(
        navController = navController,
        startDestination = Screen.ProductList.route
    ) {
        composable(Screen.ProductList.route) {
            val viewModel: ProductListViewModel = viewModel(
                factory = ViewModelFactory {
                    ProductListViewModel(container.productRepository, container.cartRepository)
                }
            )
            ProductListScreen(
                viewModel = viewModel,
                onNavigateToProductDetail = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                }
            )
        }
        composable(Screen.Search.route) {
            val viewModel: SearchViewModel = viewModel(
                factory = ViewModelFactory {
                    SearchViewModel(container.productRepository, container.cartRepository)
                }
            )
            SearchScreen(
                viewModel = viewModel,
                onNavigateToProductDetail = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                }
            )
        }
        composable(Screen.Wishlist.route) {
            PlaceholderScreen(label = "Wishlist — coming in Phase 6")
        }
        composable(Screen.Cart.route) {
            PlaceholderScreen(label = "Cart — coming in Phase 6")
        }
        composable(Screen.Checkout.route) {
            PlaceholderScreen(label = "Checkout — coming in Phase 7")
        }
        composable(Screen.Profile.route) {
            PlaceholderScreen(label = "Profile — coming in Phase 7")
        }
        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
            val viewModel: ProductDetailViewModel = viewModel(
                factory = ViewModelFactory {
                    ProductDetailViewModel(productId, container.productRepository, container.cartRepository)
                }
            )
            ProductDetailScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
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
