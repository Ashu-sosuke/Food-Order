package com.example.foodorder.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.foodorder.data.FoodCategory
import com.example.foodorder.data.FoodItem
import com.example.foodorder.presentation.viewmodel.AuthViewModel
import com.example.foodorder.presentation.viewmodel.CartViewModel
import com.example.foodorder.presentation.viewmodel.FoodViewModel
import com.example.foodorder.sampleCategories
import com.example.foodorder.ui.theme.*

@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    authViewModel: AuthViewModel,
    viewModel: FoodViewModel,
    cartViewModel: CartViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val userProfile by authViewModel.userProfile.collectAsState()
    val displayName = remember(userProfile) {
        userProfile?.name ?: "User"
    }
    var selectedCategory by remember { mutableStateOf("Chicken") }
    var searchQuery by remember { mutableStateOf("") }

    val displayItems = remember(uiState.meals, searchQuery) {
        uiState.meals.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    LaunchedEffect(key1 = authViewModel.currentUser) {
        if (userProfile == null && authViewModel.currentUser != null) {
            authViewModel.fetchUserProfile()
        }
    }


    Scaffold(containerColor = SoftBackground) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                TopHeader(userName = displayName, modifier = Modifier.padding(16.dp))

                FoodSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    userName = displayName,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                CategoryRow(
                    categories = sampleCategories,
                    selectedLabel = selectedCategory,
                    onCategorySelected = { label ->
                        selectedCategory = label
                        viewModel.loadMealsByCategory(label)
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                if (uiState.isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DeepOrange)
                    }
                } else {
                    FoodGrid(
                        items = displayItems,
                        onAddToCart = { cartViewModel.addToCart(it) },
                        modifier = Modifier.weight(1f).padding(horizontal = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TopHeader(userName: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Column {
            Text("Good Morning, $userName! 👋", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("What would you like to eat?", color = TextSecondary)
        }
        Surface(shape = CircleShape, color = CardBackground, shadowElevation = 4.dp, modifier = Modifier.size(42.dp)) {
            Icon(Icons.Default.Notifications, null, modifier = Modifier.padding(10.dp))
        }
    }
}

@Composable
private fun FoodSearchBar(query: String, onQueryChange: (String) -> Unit, userName: String, modifier: Modifier = Modifier) {
    val initial = if (userName.isNotEmpty()) userName.take(1).uppercase() else "U"
    Surface(modifier = modifier.shadow(4.dp, RoundedCornerShape(16.dp)), shape = RoundedCornerShape(16.dp), color = CardBackground) {
        Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, null, tint = TextSecondary)
            TextField(
                value = query, onValueChange = onQueryChange,
                placeholder = { Text("Search dishes...") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(focusedContainerColor = Color.Transparent, unfocusedContainerColor = Color.Transparent)
            )
            Surface(shape = CircleShape, color = DeepOrange, modifier = Modifier.size(36.dp)) {
                Box(contentAlignment = Alignment.Center) { Text(initial, color = Color.White, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun FoodGrid(items: List<FoodItem>, onAddToCart: (FoodItem) -> Unit, modifier: Modifier = Modifier) {
    LazyVerticalGrid(columns = GridCells.Fixed(2), modifier = modifier, verticalArrangement = Arrangement.spacedBy(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(items) { FoodCard(item = it, onAddToCart = { onAddToCart(it) }) }
    }
}

@Composable
private fun FoodCard(item: FoodItem, onAddToCart: () -> Unit) {
    Card(shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = CardBackground)) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                AsyncImage(model = item.imageUrl, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                Surface(shape = RoundedCornerShape(bottomStart = 12.dp), color = DeepOrange, modifier = Modifier.align(Alignment.TopEnd)) {
                    Text("Rs ${item.price.toInt()}", color = Color.White, modifier = Modifier.padding(6.dp), fontWeight = FontWeight.Bold)
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(item.name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                IconButton(onClick = onAddToCart, modifier = Modifier.align(Alignment.End).background(DeepOrange, CircleShape).size(32.dp)) {
                    Icon(Icons.Default.Add, null, tint = Color.White)
                }
            }
        }
    }
}

@Composable
private fun CategoryRow(categories: List<FoodCategory>, selectedLabel: String, onCategorySelected: (String) -> Unit) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        itemsIndexed(categories) { _, cat ->
            val isSelected = cat.label == selectedLabel
            val bgColor by animateColorAsState(if (isSelected) DeepOrange else CardBackground)
            Surface(onClick = { onCategorySelected(cat.label) }, shape = RoundedCornerShape(12.dp), color = bgColor) {
                Row(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                    Text(cat.emoji)
                    Spacer(Modifier.width(6.dp))
                    Text(cat.label, color = if (isSelected) Color.White else TextPrimary)
                }
            }
        }
    }
}