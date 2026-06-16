package com.shopease.app.ui.navigation

/**
 * All navigable destinations in the app. Kept as a sealed class so the compiler
 * forces us to handle every screen when we wire up the NavHost.
 */
sealed class Screen(val route: String) {
    data object ProductList : Screen("product_list")
    data object CategoryBrowse : Screen("category_browse/{categoryId}") {
        fun createRoute(categoryId: String) = "category_browse/$categoryId"
    }
    data object Search : Screen("search")
    data object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    data object Wishlist : Screen("wishlist")
    data object Cart : Screen("cart")
    data object Checkout : Screen("checkout")
    data object Profile : Screen("profile")
}
