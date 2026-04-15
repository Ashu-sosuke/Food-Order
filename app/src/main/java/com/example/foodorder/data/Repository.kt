package com.example.foodorder.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class FoodRepository(private val client: HttpClient) {
    suspend fun getMealsByCategory(category: String): List<FoodItem> {
        return try {
            val response: MealResponse = client.get("https://www.themealdb.com/api/json/v1/1/filter.php?c=$category").body()
            response.meals.map { it.toFoodItem(category) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}