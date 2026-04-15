package com.example.foodorder.presentation.screens

import com.example.foodorder.R
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.foodorder.data.CartItem
import com.example.foodorder.presentation.viewmodel.CartViewModel
import com.example.foodorder.ui.theme.CardBackground
import com.example.foodorder.ui.theme.DeepOrange
import com.example.foodorder.ui.theme.SoftBackground
import com.example.foodorder.ui.theme.TextPrimary
import com.example.foodorder.ui.theme.TextSecondary

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Text(
                "My Cart",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
        },
        bottomBar = {
            if (uiState.items.isNotEmpty()) {
                CheckoutBar(total = uiState.totalBill)
            }
        },
        containerColor = SoftBackground
    ) { padding ->
        if (uiState.items.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your cart is empty! 🥣", color = TextSecondary)
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).padding(horizontal = 16.dp)) {
                items(uiState.items) { item ->
                    CartItemRow(
                        item = item,
                        onIncrement = { viewModel.updateQuantity(item.foodItem.id, true) },
                        onDecrement = { viewModel.updateQuantity(item.foodItem.id, false) },
                        onRemove = { viewModel.removeItem(item.foodItem.id) }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = item.foodItem.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(item.foodItem.name, fontWeight = FontWeight.Bold, maxLines = 1)
                Text("Rs ${item.foodItem.price.toInt()}", color = DeepOrange, fontWeight = FontWeight.SemiBold)
            }

            // Quantity Controls
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onDecrement) { Icon(painterResource(R.drawable.baseline_remove_24), null) }
                Text("${item.quantity}", fontWeight = FontWeight.Bold)
                IconButton(onClick = onIncrement) { Icon(Icons.Default.Add, null) }
                IconButton(onClick = onRemove) { Icon(Icons.Default.Delete, null, tint = Color.Red) }
            }
        }
    }
}

@Composable
fun CheckoutBar(total: Double) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardBackground,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp).navigationBarsPadding(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Total Bill", color = TextSecondary, style = MaterialTheme.typography.labelMedium)
                Text("Rs ${total.toInt()}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = TextPrimary)
            }
            Button(
                onClick = { /* Checkout Logic */ },
                colors = ButtonDefaults.buttonColors(containerColor = DeepOrange),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(50.dp).width(140.dp)
            ) {
                Text("Checkout", fontWeight = FontWeight.Bold)
            }
        }
    }
}