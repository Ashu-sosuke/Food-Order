package com.example.foodorder.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector? = null, val label: String = "") {
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("sign_up_screen")

    // Bottom Nav Items
    object Dashboard : Screen("dashboard", Icons.Default.Home, "Home")
    object Cart : Screen("cart", Icons.Default.ShoppingCart, "Cart")
    object Profile : Screen("profile", Icons.Default.Person, "Profile")
}