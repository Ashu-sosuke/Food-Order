package com.example.foodorder.presentation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.foodorder.data.FoodCategory
import com.example.foodorder.data.FoodItem
import com.example.foodorder.presentation.viewmodel.AuthViewModel
import com.example.foodorder.sampleCategories
import com.example.foodorder.sampleFoodItems
import com.example.foodorder.ui.theme.*

@Composable
fun DashboardScreen(
    onLogout: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
) {
    // Ideally, get the real name from the ViewModel/Session
    val userName = "Sarah"

    MaterialTheme(colorScheme = FoodAppColorScheme) {
        var searchQuery by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf("All") }

        val filteredItems = remember(selectedCategory, searchQuery) {
            sampleFoodItems.filter {
                val matchesCategory = selectedCategory == "All" ||
                        it.category.equals(selectedCategory, ignoreCase = true)
                val matchesSearch = it.name.contains(searchQuery, ignoreCase = true)
                matchesCategory && matchesSearch
            }
        }

        Scaffold(
            containerColor = SoftBackground,
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                Column(modifier = Modifier.fillMaxSize()) {
                    // ── Header Section ──────────────────────────────────────────
                    TopHeader(
                        userName = userName,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    )

                    FoodSearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Categories ──────────────────────────────────────────────
                    Text(
                        text = "Categories",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                        ),
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    CategoryRow(
                        categories = sampleCategories,
                        selectedLabel = selectedCategory,
                        onCategorySelected = { selectedCategory = it },
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Popular Section ──────────────────────────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Popular Near You",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                            ),
                        )
                        TextButton(onClick = {}) {
                            Text(
                                "See All",
                                style = MaterialTheme.typography.labelMedium.copy(color = DeepOrange),
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // ── Food Grid (Takes remaining space) ────────────────────────
                    FoodGrid(
                        items = filteredItems,
                        onAddToCart = { /* Handle logic */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = 12.dp),
                    )
                }

                // ── Logout Button Overlay ─────────────────────────────────────
                Surface(
                    onClick = {
                        authViewModel.logout()
                        onLogout()
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = DeepOrange,
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 12.dp, end = 16.dp),
                ) {
                    Text(
                        text = "Logout",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun TopHeader(userName: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                text = "Good Morning, $userName! 👋",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary,
                ),
            )
            Text(
                text = "What would you like to eat?",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
            )
        }

        Box {
            Surface(
                shape = CircleShape,
                color = CardBackground,
                shadowElevation = 4.dp,
                modifier = Modifier.size(42.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.padding(10.dp),
                )
            }
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.Red, CircleShape)
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = 2.dp),
            )
        }
    }
}

@Composable
private fun FoodSearchBar(query: String, onQueryChange: (String) -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = CardBackground,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Default.Search, null, tint = TextSecondary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search dishes...", color = TextSecondary.copy(0.7f), fontSize = 14.sp) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
            Surface(shape = CircleShape, color = DeepOrange, modifier = Modifier.size(36.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Text("SR", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun CategoryRow(
    categories: List<FoodCategory>,
    selectedLabel: String,
    onCategorySelected: (String) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        itemsIndexed(categories) { _, category ->
            CategoryChip(
                category = category,
                isSelected = category.label == selectedLabel,
                onClick = { onCategorySelected(category.label) },
            )
        }
    }
}

@Composable
private fun CategoryChip(category: FoodCategory, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor by animateColorAsState(if (isSelected) DeepOrange else CardBackground, label = "")
    val textColor by animateColorAsState(if (isSelected) Color.White else TextPrimary, label = "")

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = if (!isSelected) BorderStroke(1.dp, DeepOrange.copy(0.2f)) else null,
        modifier = Modifier.graphicsLayer(scaleX = if (isSelected) 1.05f else 1f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(category.emoji, fontSize = 18.sp)
            Text(category.label, color = textColor, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FoodGrid(items: List<FoodItem>, onAddToCart: (FoodItem) -> Unit, modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items) { foodItem ->
            FoodCard(item = foodItem, onAddToCart = { onAddToCart(foodItem) })
        }
    }
}

@Composable
private fun FoodCard(item: FoodItem, onAddToCart: () -> Unit) {
    var added by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = Modifier.shadow(4.dp, RoundedCornerShape(16.dp))
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth().height(120.dp)) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
                Surface(
                    shape = RoundedCornerShape(bottomStart = 12.dp),
                    color = DeepOrange,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text("$${item.price}", color = Color.White, modifier = Modifier.padding(6.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(item.name, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, null, tint = StarYellow, modifier = Modifier.size(14.dp))
                    Text(" ${item.rating} • ${item.deliveryTimeMin}m", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                }
                IconButton(
                    onClick = { added = true; onAddToCart() },
                    modifier = Modifier.align(Alignment.End).size(32.dp).background(DeepOrange, CircleShape)
                ) {
                    Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}