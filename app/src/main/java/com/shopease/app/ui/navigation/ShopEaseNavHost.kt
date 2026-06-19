package com.shopease.app.ui.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shopease.app.ShopEaseApplication
import com.shopease.app.ui.base.ViewModelFactory
import com.shopease.app.ui.cart.CartScreen
import com.shopease.app.ui.cart.CartViewModel
import com.shopease.app.ui.checkout.CheckoutScreen
import com.shopease.app.ui.checkout.CheckoutViewModel
import com.shopease.app.ui.productdetail.ProductDetailScreen
import com.shopease.app.ui.productdetail.ProductDetailViewModel
import com.shopease.app.ui.productlist.ProductListScreen
import com.shopease.app.ui.productlist.ProductListViewModel
import com.shopease.app.ui.profile.ProfileScreen
import com.shopease.app.ui.profile.ProfileViewModel
import com.shopease.app.ui.search.SearchScreen
import com.shopease.app.ui.search.SearchViewModel
import com.shopease.app.ui.wishlist.WishlistScreen
import com.shopease.app.ui.wishlist.WishlistViewModel

/**
 * Root NavHost. All seven screens are fully wired to real ViewModels and
 * repositories from AppContainer. Wrapped in SharedTransitionLayout so the
 * product image can morph from the grid/list straight into the Detail screen
 * instead of just cutting/cross-fading between two separate composables.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ShopEaseNavHost(
    navController: NavHostController = rememberNavController()
) {
    val container = (LocalContext.current.applicationContext as ShopEaseApplication).container

    SharedTransitionLayout {
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
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
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
                    },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }
            composable(Screen.Wishlist.route) {
                val viewModel: WishlistViewModel = viewModel(
                    factory = ViewModelFactory { WishlistViewModel(container.wishlistRepository) }
                )
                WishlistScreen(
                    viewModel = viewModel,
                    onNavigateToProductDetail = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    }
                )
            }
            composable(Screen.Cart.route) {
                val viewModel: CartViewModel = viewModel(
                    factory = ViewModelFactory { CartViewModel(container.cartRepository) }
                )
                CartScreen(
                    viewModel = viewModel,
                    onNavigateToCheckout = { navController.navigate(Screen.Checkout.route) }
                )
            }
            composable(Screen.Checkout.route) {
                val viewModel: CheckoutViewModel = viewModel(
                    factory = ViewModelFactory { CheckoutViewModel(container.cartRepository) }
                )
                CheckoutScreen(
                    viewModel = viewModel,
                    onOrderPlaced = {
                        navController.navigate(Screen.ProductList.route) {
                            popUpTo(Screen.ProductList.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.Profile.route) {
                val viewModel: ProfileViewModel = viewModel(
                    factory = ViewModelFactory { ProfileViewModel(container.userPreferencesRepository) }
                )
                ProfileScreen(viewModel = viewModel)
            }
            composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getString("productId") ?: return@composable
                val viewModel: ProductDetailViewModel = viewModel(
                    factory = ViewModelFactory {
                        ProductDetailViewModel(
                            productId,
                            container.productRepository,
                            container.cartRepository,
                            container.wishlistRepository
                        )
                    }
                )
                ProductDetailScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@composable
                )
            }
        }
    }
}
