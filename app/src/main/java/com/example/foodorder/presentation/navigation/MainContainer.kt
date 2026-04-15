package com.example.foodorder.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodorder.presentation.screens.CartScreen
import com.example.foodorder.presentation.screens.DashboardScreen
import com.example.foodorder.presentation.viewmodel.CartViewModel
import com.example.foodorder.presentation.viewmodel.FoodViewModel
import com.example.foodorder.ui.theme.CardBackground
import com.example.foodorder.ui.theme.DeepOrange

@Composable
fun MainContainer(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val cartViewModel: CartViewModel = viewModel()
    val foodViewModel: FoodViewModel = viewModel()

    val bottomNavItems = listOf(Screen.Dashboard, Screen.Cart, Screen.Profile)

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = CardBackground,
                tonalElevation = 8.dp
            ) {
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(screen.icon!!, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepOrange,
                            selectedTextColor = DeepOrange,
                            indicatorColor = DeepOrange.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                // Pass the SHARED ViewModels
                DashboardScreen(
                    onLogout = onLogout,
                    viewModel = foodViewModel,
                    cartViewModel = cartViewModel
                )
            }

            composable(Screen.Cart.route) {
                // Pass the SAME cartViewModel instance
                CartScreen(
                    viewModel = cartViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    TODO("Not yet implemented")
}