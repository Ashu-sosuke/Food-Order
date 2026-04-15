package com.example.foodorder.data

import kotlinx.serialization.Serializable

data class FoodItem(
    val id: Int,
    val name: String,
    val price: Double,
    val rating: Double,
    val prepTimeMin: Int,
    val deliveryTimeMin: Int,
    val imageUrl: String,
    val category: String,
)

data class FoodCategory(
    val label: String,
    val emoji: String,
)

@Serializable
data class MealResponse(val meals: List<MealDto>)


@Serializable
data class MealDto(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)

fun MealDto.toFoodItem(category: String): FoodItem {
    return FoodItem(
        id = idMeal.toIntOrNull() ?: 0,
        name = strMeal,
        category = category,
        price = (300..400).random().toDouble(),
        rating = (35..50).random().toDouble() / 10.0,
        prepTimeMin = (10..25).random(),
        deliveryTimeMin = (15..45).random(),
        imageUrl = strMealThumb,
    )
}


data class FoodUiState(
    val isLoading: Boolean = false,
    val meals: List<FoodItem> = emptyList(),
    val errorMessage: String? = null
)

data class CartItem(
    val foodItem: FoodItem,
    val quantity: Int = 1
)

// UI State for the Cart
data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val totalBill: Double = 0.0
)