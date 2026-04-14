package com.example.foodorder.presentation.screens

import androidx.compose.ui.res.painterResource
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodorder.presentation.viewmodel.AuthState
import com.example.foodorder.presentation.viewmodel.AuthViewModel
import com.example.foodorder.R

// ─── Colors ──────────────────────────────────────────────────────────────────
private val BrandOrange      = Color(0xFFFF6B1A)
private val BrandOrangeLight = Color(0xFFFF9A3C)
private val PageBg           = Color(0xFFF5F5F0)
private val CardWhite        = Color(0xFFFFFFFF)
private val TextDark         = Color(0xFF1A1A1A)
private val TextGrey         = Color(0xFF9E9E9E)
private val DividerGrey      = Color(0xFFE0E0E0)
private val GoogleBlue       = Color(0xFF4285F4)
private val ErrorRed         = Color(0xFFD32F2F)

// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
) {
    val authState by authViewModel.authState.collectAsState()
    val focusManager = LocalFocusManager.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { contentVisible = true }

    // Navigate on success
    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            authViewModel.resetState()
            onLoginSuccess()
        }
    }

    val isLoading = authState is AuthState.Loading
    val errorMsg  = (authState as? AuthState.Error)?.message

    // Button press scale
    var loginPressed by remember { mutableStateOf(false) }
    val loginScale by animateFloatAsState(
        targetValue = if (loginPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "loginScale",
        finishedListener = { loginPressed = false },
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg),
    ) {
        // Decorative blobs
        Box(
            modifier = Modifier
                .size(220.dp)
                .offset(x = 160.dp, y = (-50).dp)
                .background(
                    Brush.radialGradient(listOf(BrandOrange.copy(0.12f), Color.Transparent)),
                    CircleShape,
                )
        )
        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(x = (-40).dp, y = 340.dp)
                .background(
                    Brush.radialGradient(listOf(BrandOrangeLight.copy(0.10f), Color.Transparent)),
                    CircleShape,
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(52.dp))

            // Top row
            AnimatedVisibility(visible = contentVisible, enter = fadeIn() + slideInVertically { -30 }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(shape = RoundedCornerShape(14.dp), color = CardWhite, shadowElevation = 4.dp, modifier = Modifier.size(48.dp)) {
                        Box(contentAlignment = Alignment.Center) { Text("🍔", fontSize = 26.sp) }
                    }
                    Surface(shape = CircleShape, color = BrandOrange, modifier = Modifier.size(44.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("SR", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Headline
            AnimatedVisibility(visible = contentVisible, enter = fadeIn() + slideInVertically { 40 }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Welcome Back", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold, color = TextDark, fontSize = 30.sp))
                    Spacer(Modifier.height(6.dp))
                    Text("Login to order your favorite meals.", style = MaterialTheme.typography.bodyMedium.copy(color = TextGrey), textAlign = TextAlign.Center)
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Error banner
            AnimatedVisibility(visible = errorMsg != null) {
                Surface(shape = RoundedCornerShape(12.dp), color = ErrorRed.copy(alpha = 0.10f), modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = errorMsg ?: "",
                        color = ErrorRed,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    )
                }
            }
            if (errorMsg != null) Spacer(Modifier.height(12.dp))

            // Email
            AnimatedVisibility(visible = contentVisible, enter = fadeIn() + slideInVertically { 60 }) {
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (errorMsg != null) authViewModel.resetState()
                    },
                    placeholder = { Text("Email Address", color = TextGrey, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Default.Email, null, tint = TextGrey, modifier = Modifier.size(20.dp)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardWhite, unfocusedContainerColor = CardWhite,
                        focusedBorderColor = BrandOrange, unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = TextDark, unfocusedTextColor = TextDark, cursorColor = BrandOrange,
                    ),
                    modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(0.06f)),
                )
            }

            Spacer(Modifier.height(14.dp))

            // Password
            AnimatedVisibility(visible = contentVisible, enter = fadeIn() + slideInVertically { 80 }) {
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (errorMsg != null) authViewModel.resetState()
                    },
                    placeholder = { Text("Password", color = TextGrey, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = TextGrey, modifier = Modifier.size(20.dp)) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible)
                                        R.drawable.outline_visibility_24
                                    else
                                        R.drawable.outline_visibility_off_24
                                ),
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = TextGrey,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = CardWhite, unfocusedContainerColor = CardWhite,
                        focusedBorderColor = BrandOrange, unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = TextDark, unfocusedTextColor = TextDark, cursorColor = BrandOrange,
                    ),
                    modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(0.06f)),
                )
            }

            Spacer(Modifier.height(10.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                Text(
                    "Forgot Password?",
                    color = BrandOrange, fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.clickable(MutableInteractionSource(), null) { /* TODO */ },
                )
            }

            Spacer(Modifier.height(28.dp))

            // LOGIN button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .graphicsLayer(scaleX = loginScale, scaleY = loginScale)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Brush.horizontalGradient(listOf(BrandOrange, BrandOrangeLight)))
                    .clickable(MutableInteractionSource(), null, enabled = !isLoading) {
                        loginPressed = true
                        focusManager.clearFocus()
                        authViewModel.login(email, password)
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp)
                } else {
                    Text("LOGIN", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 2.sp)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Divider
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Divider(modifier = Modifier.weight(1f), color = DividerGrey)
                Text("  or  ", color = TextGrey, fontSize = 12.sp)
                Divider(modifier = Modifier.weight(1f), color = DividerGrey)
            }

            Spacer(Modifier.height(20.dp))

            // Google
            Surface(
                onClick = { /* TODO: Google Sign-In */ },
                shape = RoundedCornerShape(28.dp),
                color = CardWhite,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth().height(52.dp),
            ) {
                Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text("G", color = GoogleBlue, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
                    Spacer(Modifier.width(10.dp))
                    Text("Login with Google", color = TextDark, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Sign up
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextGrey, fontSize = 13.sp)) { append("Don't have an account? ") }
                    withStyle(SpanStyle(color = BrandOrange, fontWeight = FontWeight.Bold, fontSize = 13.sp)) { append("Sign Up") }
                },
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .clickable(MutableInteractionSource(), null, onClick = onNavigateToSignUp),
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LoginPreview() {
    MaterialTheme { LoginScreen(onLoginSuccess = {}, onNavigateToSignUp = {}) }
}