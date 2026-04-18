package com.example.foodorder.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.foodorder.data.AppNavDestination
import com.example.foodorder.presentation.screens.*
import com.example.foodorder.presentation.viewmodel.*
import com.example.foodorder.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContainer(
    onLogout: () -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val cartViewModel: CartViewModel = viewModel()
    val foodViewModel: FoodViewModel = viewModel()

    val cartUiState by cartViewModel.uiState.collectAsState()
    val totalItemsInCart = cartUiState.items.sumOf { it.quantity }

    // Use your custom AppNavDestination instead of the internal NavigationItem
    val bottomNavItems = listOf(
        AppNavDestination(Screen.Dashboard.route, "Home", Icons.Default.Home),
        AppNavDestination(Screen.Cart.route, "Cart", Icons.Default.ShoppingCart),
        AppNavDestination(Screen.Profile.route, "Profile", Icons.Default.Person)
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = CardBackground,
                tonalElevation = 8.dp
            ) {
                bottomNavItems.forEach { item ->
                    val isSelected = currentRoute == item.route

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            if (item.route == Screen.Cart.route && totalItemsInCart > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = Color.Red,
                                            contentColor = Color.White
                                        ) {
                                            Text(text = totalItemsInCart.toString())
                                        }
                                    }
                                ) {
                                    Icon(item.icon, contentDescription = item.label)
                                }
                            } else {
                                Icon(item.icon, contentDescription = item.label)
                            }
                        },
                        label = { Text(item.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepOrange,
                            selectedTextColor = DeepOrange,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
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
                DashboardScreen(
                    onLogout = onLogout,
                    authViewModel = authViewModel,
                    viewModel = foodViewModel,
                    cartViewModel = cartViewModel
                )
            }

            composable(Screen.Cart.route) {
                CartScreen(
                    viewModel = cartViewModel,
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.Profile.route) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    onLogout = onLogout
                )
            }
        }
    }
}

@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel,
    onLogout: () -> Unit
) {
    val userProfile by authViewModel.userProfile.collectAsState()

    Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = DeepOrange.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = userProfile?.name?.take(1)?.uppercase() ?: "U",
                        style = MaterialTheme.typography.displayMedium,
                        color = DeepOrange,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(userProfile?.name ?: "User", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold)
            Text(userProfile?.email ?: "", color = TextSecondary)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepOrange)
            ) {
                Text("Logout", fontWeight = FontWeight.Bold)
            }
        }
    }
}