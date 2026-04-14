package com.example.foodorder.data

data class FoodItem(
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Float,
    val prepTimeMin: Int,
    val deliveryTimeMin: Int,
    val imageUrl: String,
    val category: String,
)

data class FoodCategory(
    val label: String,
    val emoji: String,
)