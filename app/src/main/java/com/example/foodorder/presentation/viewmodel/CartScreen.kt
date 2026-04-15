package com.example.foodorder.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.foodorder.data.CartItem
import com.example.foodorder.data.CartUiState
import com.example.foodorder.data.FoodItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    fun addToCart(foodItem: FoodItem) {
        _uiState.update { currentState ->
            val existingItem = currentState.items.find { it.foodItem.id == foodItem.id }
            val newItems = if (existingItem != null) {
                currentState.items.map {
                    if (it.foodItem.id == foodItem.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentState.items + CartItem(foodItem)
            }
            currentState.copy(items = newItems, totalBill = calculateTotal(newItems))
        }
    }

    fun updateQuantity(itemId: Int, increment: Boolean) {
        _uiState.update { currentState ->
            val newItems = currentState.items.mapNotNull {
                if (it.foodItem.id == itemId) {
                    val newQty = if (increment) it.quantity + 1 else it.quantity - 1
                    if (newQty > 0) it.copy(quantity = newQty) else null // Remove if 0
                } else it
            }
            currentState.copy(items = newItems, totalBill = calculateTotal(newItems))
        }
    }

    fun removeItem(itemId: Int) {
        _uiState.update { currentState ->
            val newItems = currentState.items.filter { it.foodItem.id != itemId }
            currentState.copy(items = newItems, totalBill = calculateTotal(newItems))
        }
    }

    private fun calculateTotal(items: List<CartItem>): Double {
        return items.sumOf { it.foodItem.price * it.quantity }
    }
}