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
        // ── Splash ────────────────────────────────────────────────────────────
        composable(Screen.SplashScreen.route) {
            SplashScreen(
                onUserLoggedIn = {
                    navController.navigate(Screen.DashboardScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                },
                onGetStarted = {
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                },
            )
        }

        // ── Login ─────────────────────────────────────────────────────────────
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.DashboardScreen.route) {
                        // Clears the login screen from history
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Screen.SignUpScreen.route)
                },
            )
        }

        // ── Sign Up ───────────────────────────────────────────────────────────
        composable(Screen.SignUpScreen.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.DashboardScreen.route) {
                        // Clears the signup screen from history
                        popUpTo(Screen.SignUpScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    // Better to pop than navigate to avoid duplicate login screens
                    navController.popBackStack()
                },
            )
        }

        // ── Dashboard ─────────────────────────────────────────────────────────
        composable(Screen.DashboardScreen.route) {
            DashboardScreen(
                onLogout = {
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.DashboardScreen.route) { inclusive = true }
                    }
                },
            )
        }
    }
}