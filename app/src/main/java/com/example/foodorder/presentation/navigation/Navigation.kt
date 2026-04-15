package com.example.foodorder.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.foodorder.presentation.screens.DashboardScreen
import com.example.foodorder.presentation.screens.LoginScreen
import com.example.foodorder.presentation.screens.SignUpScreen
import com.example.foodorder.presentation.screens.SplashScreen

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route,
        modifier = modifier,
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(
                onUserLoggedIn = { navigateToMain(navController) },
                onGetStarted = { navController.navigate(Screen.LoginScreen.route) }
            )
        }

        composable(Screen.LoginScreen.route) {
            LoginScreen(
                onLoginSuccess = { navigateToMain(navController) },
                onNavigateToSignUp = { navController.navigate(Screen.SignUpScreen.route) }
            )
        }

        composable(Screen.SignUpScreen.route) {
            SignUpScreen(
                onSignUpSuccess = { navigateToMain(navController) },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // ── Main App (Authenticated) ──────────────────────────────────────────
        composable("main_container") {
            MainContainer(
                onLogout = {
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo("main_container") { inclusive = true }
                    }
                }
            )
        }
    }
}

// Helper to clean up backstack when entering the app
private fun navigateToMain(navController: NavHostController) {
    navController.navigate("main_container") {
        popUpTo(0) { inclusive = true }
    }
}