package com.example.foodorder.presentation.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object SignUpScreen : Screen("sign_up_screen")
    object DashboardScreen : Screen("dashboard_screen")
}