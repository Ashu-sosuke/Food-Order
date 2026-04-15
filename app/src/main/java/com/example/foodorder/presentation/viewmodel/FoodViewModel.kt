package com.example.foodorder.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodorder.data.FoodUiState
import com.example.foodorder.data.MealResponse
import com.example.foodorder.data.toFoodItem
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FoodViewModel : ViewModel() {

    private val client = HttpClient(io.ktor.client.engine.cio.CIO) {
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json(kotlinx.serialization.json.Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val _uiState = MutableStateFlow(FoodUiState())
    val uiState: StateFlow<FoodUiState> = _uiState.asStateFlow()

    init {
        // Load default category on start
        loadMealsByCategory("Chicken")
    }

    // Inside FoodViewModel.kt
    fun loadMealsByCategory(category: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val url = "https://www.themealdb.com/api/json/v1/1/filter.php?c=$category"
                val response: MealResponse = client.get(url).body()

                // Map the meals, or return an empty list if response.meals is null
                val foodItems = response.meals?.map { it.toFoodItem(category) } ?: emptyList()

                _uiState.update { it.copy(isLoading = false, meals = foodItems) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, meals = emptyList(), errorMessage = e.message) }
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        client.close() // Close client to prevent memory leaks
    }
}