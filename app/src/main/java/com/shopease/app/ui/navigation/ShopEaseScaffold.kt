package com.shopease.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

private data class BottomNavDestination(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

private val bottomNavDestinations = listOf(
    BottomNavDestination(Screen.ProductList, "Home", Icons.Filled.Home),
    BottomNavDestination(Screen.Search, "Search", Icons.Filled.Search),
    BottomNavDestination(Screen.Wishlist, "Wishlist", Icons.Filled.Favorite),
    BottomNavDestination(Screen.Cart, "Cart", Icons.Filled.ShoppingCart),
    BottomNavDestination(Screen.Profile, "Profile", Icons.Filled.Person)
)

/**
 * Top-level scaffold: bottom nav bar + the NavHost graph. The bar only shows
 * on the five top-level destinations — it hides itself on Product Detail,
 * Checkout, and Category Browse since those are pushed on top of the stack.
 */
@Composable
fun ShopEaseScaffold() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination

    val showBottomBar = bottomNavDestinations.any { it.screen.route == currentRoute?.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavDestinations.forEach { destination ->
                        val selected = currentRoute?.hierarchy?.any { it.route == destination.screen.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(destination.icon, contentDescription = destination.label) },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        // Each screen's own Scaffold handles its own padding/top bar, so we
        // intentionally don't re-apply paddingValues here beyond what NavHost needs —
        // ShopEaseNavHost's screens are full Scaffolds themselves.
        androidx.compose.foundation.layout.Box(
            modifier = androidx.compose.ui.Modifier.padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            ShopEaseNavHost(navController = navController)
        }
    }
}
