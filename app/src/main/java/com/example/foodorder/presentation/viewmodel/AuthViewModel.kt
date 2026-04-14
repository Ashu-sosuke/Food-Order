package com.example.foodorder.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

// ─────────────────────────────────────────────────────────────────────────────
//  Auth State
// ─────────────────────────────────────────────────────────────────────────────

sealed class AuthState {
    object Idle       : AuthState()
    object Loading    : AuthState()
    object Success    : AuthState()
    data class Error(val message: String) : AuthState()
}

// ─────────────────────────────────────────────────────────────────────────────
//  ViewModel
// ─────────────────────────────────────────────────────────────────────────────

class AuthViewModel : ViewModel() {

    private val auth      = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    /** Non-null when a user session already exists (persisted by Firebase). */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    // ── Sign Up ───────────────────────────────────────────────────────────────

    fun signUp(
        name: String,
        email: String,
        password: String,
    ) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("All fields are required.")
            return
        }
        if (password.length < 6) {
            _authState.value = AuthState.Error("Password must be at least 6 characters.")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = auth
                    .createUserWithEmailAndPassword(email.trim(), password)
                    .await()

                val uid = result.user?.uid
                    ?: throw IllegalStateException("UID is null after sign-up")

                // Persist extra user data in Firestore
                firestore.collection("users").document(uid).set(
                    mapOf(
                        "uid"       to uid,
                        "name"      to name.trim(),
                        "email"     to email.trim(),
                        "createdAt" to System.currentTimeMillis(),
                    )
                ).await()

                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.localizedMessage ?: "Sign-up failed. Please try again."
                )
            }
        }
    }

    // ── Login ─────────────────────────────────────────────────────────────────

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState.Error("Email and password are required.")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                auth.signInWithEmailAndPassword(email.trim(), password).await()
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(
                    e.localizedMessage ?: "Login failed. Check your credentials."
                )
            }
        }
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Idle
    }

    // ── Reset state (e.g. after navigating away from error) ───────────────────

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}