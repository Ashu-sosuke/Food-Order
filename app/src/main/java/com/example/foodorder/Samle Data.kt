package com.example.foodorder

import com.example.foodorder.data.FoodCategory
import com.example.foodorder.data.FoodItem


 val sampleCategories = listOf(
    FoodCategory("Pizza", "🍕"),
    FoodCategory("Burger",  "🍔"),
    FoodCategory("Sushi",   "🍱"),
    FoodCategory("Pasta",   "🍝"),
    FoodCategory("Salads",  "🥗"),
    FoodCategory("Tacos",   "🌮"),
)

 val sampleFoodItems = listOf(
    FoodItem(
        id = 1,
        name = "Margherita Pizza",
        price = 14.99,
        rating = 4.8f,
        prepTimeMin = 25,
        deliveryTimeMin = 25,
        imageUrl = "https://images.unsplash.com/photo-1574071318508-1cdbab80d002?w=400",
        category = "Pizza",
    ),
    FoodItem(
        id = 2,
        name = "The Beef Burger",
        price = 16.50,
        rating = 4.7f,
        prepTimeMin = 30,
        deliveryTimeMin = 30,
        imageUrl = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=400",
        category = "Burger",
    ),
    FoodItem(
        id = 3,
        name = "Chicken Salad",
        price = 15.00,
        rating = 4.5f,
        prepTimeMin = 15,
        deliveryTimeMin = 18,
        imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400",
        category = "Salads",
    ),
    FoodItem(
        id = 4,
        name = "Chocolate Lava Cake",
        price = 9.99,
        rating = 4.9f,
        prepTimeMin = 4,
        deliveryTimeMin = 15,
        imageUrl = "https://images.unsplash.com/photo-1606313564200-e75d5e30476c?w=400",
        category = "Desserts",
    ),
    FoodItem(
        id = 5,
        name = "Dragon Roll",
        price = 18.00,
        rating = 4.6f,
        prepTimeMin = 20,
        deliveryTimeMin = 25,
        imageUrl = "https://images.unsplash.com/photo-1617196034183-421b4040ed20?w=400",
        category = "Sushi",
    ),
    FoodItem(
        id = 6,
        name = "Spaghetti Carbonara",
        price = 13.50,
        rating = 4.4f,
        prepTimeMin = 20,
        deliveryTimeMin = 30,
        imageUrl = "https://images.unsplash.com/photo-1612874742237-6526221588e3?w=400",
        category = "Pasta",
    ),
)
