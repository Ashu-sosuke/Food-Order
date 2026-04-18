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
data class UserProfile(val name: String = "", val email: String = "")
// ─────────────────────────────────────────────────────────────────────────────
//  ViewModel
// ─────────────────────────────────────────────────────────────────────────────

class AuthViewModel : ViewModel() {

    private val auth      = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    private val _userProfile = MutableStateFlow<UserProfile?>(null)
    val userProfile: StateFlow<UserProfile?> = _userProfile.asStateFlow()

    val currentUser: FirebaseUser?
        get() = auth.currentUser


    init {
        fetchUserProfile()
    }

    fun fetchUserProfile() {
        // Get current user directly from Auth
        val user = auth.currentUser
        if (user == null) {
            _userProfile.value = UserProfile("Guest", "")
            return
        }

        viewModelScope.launch {
            try {
                val snapshot = firestore.collection("users")
                    .document(user.uid)
                    .get()
                    .await()

                if (snapshot.exists()) {
                    val name = snapshot.getString("name") ?: "User"
                    val email = snapshot.getString("email") ?: ""
                    _userProfile.value = UserProfile(name, email)
                }
            } catch (e: Exception) {
                _userProfile.value = UserProfile("User", user.email ?: "")
            }
        }
    }


    // ── Sign Up ───────────────────────────────────────────────────────────────

    fun signUp(name: String, email: String, password: String) {
        // ... validation logic ...
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = auth.createUserWithEmailAndPassword(email.trim(), password).await()
                val uid = result.user?.uid ?: throw IllegalStateException("UID is null")

                val profileData = mapOf(
                    "uid" to uid,
                    "name" to name.trim(),
                    "email" to email.trim(),
                    "createdAt" to System.currentTimeMillis(),
                )
                firestore.collection("users").document(uid).set(profileData).await()

                // FIX: Manually update the state immediately so the UI doesn't have to wait for a fetch
                _userProfile.value = UserProfile(name.trim(), email.trim())
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Sign-up failed")
            }
        }
    }

    fun login(email: String, password: String) {
        // ... validation logic ...
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                auth.signInWithEmailAndPassword(email.trim(), password).await()

                // FIX: Trigger fetch immediately on successful login
                fetchUserProfile()

                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.localizedMessage ?: "Login failed")
            }
        }
    }

    // ── Logout ────────────────────────────────────────────────────────────────

    fun logout() {
        auth.signOut()
        _userProfile.value = null
    }

    // ── Reset state (e.g. after navigating away from error) ───────────────────

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}