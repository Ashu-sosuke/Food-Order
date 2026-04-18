# 🍔 FoodOrder App

A modern, native Android food ordering application built with **Jetpack Compose** and **Clean Architecture**. The app provides a seamless experience for browsing meals, managing a real-time cart, and secure user authentication.

## 🚀 Key Features
- **Live Meal Data**: Integration with *TheMealDB API* using Ktor for real-time menu updates.
- **Smart Auth**: User registration and login powered by *Firebase Authentication*.
- **Profile Sync**: User profiles persisted and synced via *Firestore* (registered names visible on dashboard).
- **Dynamic Cart**: Full CRUD operations for cart management with real-time bill calculation.
- **Modern UI**: Fully responsive layouts with Jetpack Compose, including custom category chips, badges, and interactive food cards.

## 🛠 Tech Stack
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Networking**: Ktor Client
- **Backend**: Firebase (Auth & Firestore)
- **Image Loading**: Coil
- **Architecture**: MVVM with StateFlow and Coroutines

## 🏗 Project Architecture
The project follows the **MVVM (Model-View-ViewModel)** pattern:
- **Data**: API Models (DTOs) and Repository logic.
- **ViewModel**: Handles business logic and UI state management.
- **UI**: Composable functions for screens (Dashboard, Cart, Profile, Auth).

## 📝 Setup
1. Clone the repository.
2. Add your `google-services.json` to the `/app` folder.
3. Build and Run on an Android Emulator or Device.
