package com.example.foodorder.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.foodorder.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
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

// ─── Colors (same palette) ────────────────────────────────────────────────────
private val BrandOrange      = Color(0xFFFF6B1A)
private val BrandOrangeLight = Color(0xFFFF9A3C)
private val PageBg           = Color(0xFFF5F5F0)
private val CardWhite        = Color(0xFFFFFFFF)
private val TextDark         = Color(0xFF1A1A1A)
private val TextGrey         = Color(0xFF9E9E9E)
private val ErrorRed         = Color(0xFFD32F2F)
private val SuccessGreen     = Color(0xFF2E7D32)

// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun SignUpScreen(
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
) {
    val authState by authViewModel.authState.collectAsState()
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { contentVisible = true }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            authViewModel.resetState()
            onSignUpSuccess()
        }
    }

    val isLoading = authState is AuthState.Loading
    val errorMsg  = (authState as? AuthState.Error)?.message

    // Local validation
    val passwordsMatch = confirmPassword.isEmpty() || password == confirmPassword
    val localError = when {
        confirmPassword.isNotEmpty() && !passwordsMatch -> "Passwords do not match."
        else -> null
    }
    val displayError = localError ?: errorMsg

    var btnPressed by remember { mutableStateOf(false) }
    val btnScale by animateFloatAsState(
        targetValue = if (btnPressed) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "btnScale",
        finishedListener = { btnPressed = false },
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBg),
    ) {
        // Blobs
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = 170.dp, y = (-40).dp)
                .background(Brush.radialGradient(listOf(BrandOrange.copy(0.12f), Color.Transparent)), CircleShape)
        )
        Box(
            modifier = Modifier
                .size(160.dp)
                .offset(x = (-50).dp, y = 420.dp)
                .background(Brush.radialGradient(listOf(BrandOrangeLight.copy(0.10f), Color.Transparent)), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(52.dp))

            // Top logo
            AnimatedVisibility(visible = contentVisible, enter = fadeIn() + slideInVertically { -30 }) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Surface(shape = RoundedCornerShape(14.dp), color = CardWhite, shadowElevation = 4.dp, modifier = Modifier.size(48.dp)) {
                        Box(contentAlignment = Alignment.Center) { Text("🍔", fontSize = 26.sp) }
                    }
                }
            }

            Spacer(Modifier.height(36.dp))

            // Headline
            AnimatedVisibility(visible = contentVisible, enter = fadeIn() + slideInVertically { 40 }) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Create Account", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold, color = TextDark, fontSize = 30.sp))
                    Spacer(Modifier.height(6.dp))
                    Text("Sign up to start ordering delicious food!", style = MaterialTheme.typography.bodyMedium.copy(color = TextGrey), textAlign = TextAlign.Center)
                }
            }

            Spacer(Modifier.height(32.dp))

            // Error banner
            AnimatedVisibility(visible = displayError != null) {
                Surface(shape = RoundedCornerShape(12.dp), color = ErrorRed.copy(0.10f), modifier = Modifier.fillMaxWidth()) {
                    Text(displayError ?: "", color = ErrorRed, fontSize = 13.sp, modifier = Modifier.padding(14.dp, 10.dp))
                }
            }
            if (displayError != null) Spacer(Modifier.height(12.dp))

            // ── Name ──────────────────────────────────────────────────────────
            FoodTextField(
                value = name,
                onValueChange = { name = it; if (errorMsg != null) authViewModel.resetState() },
                placeholder = "Full Name",
                leadingIcon = { Icon(Icons.Default.Person, null, tint = TextGrey, modifier = Modifier.size(20.dp)) },
                imeAction = ImeAction.Next,
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                visible = contentVisible,
                slideOffset = 50,
            )

            Spacer(Modifier.height(14.dp))

            // ── Email ─────────────────────────────────────────────────────────
            FoodTextField(
                value = email,
                onValueChange = { email = it; if (errorMsg != null) authViewModel.resetState() },
                placeholder = "Email Address",
                leadingIcon = { Icon(Icons.Default.Email, null, tint = TextGrey, modifier = Modifier.size(20.dp)) },
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                visible = contentVisible,
                slideOffset = 65,
            )

            Spacer(Modifier.height(14.dp))

            // ── Password ──────────────────────────────────────────────────────
            FoodTextField(
                value = password,
                onValueChange = { password = it; if (errorMsg != null) authViewModel.resetState() },
                placeholder = "Password",
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
                        )                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                visible = contentVisible,
                slideOffset = 80,
            )

            Spacer(Modifier.height(14.dp))

            // ── Confirm Password ──────────────────────────────────────────────
            FoodTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "Confirm Password",
                leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = TextGrey, modifier = Modifier.size(20.dp)) },
                trailingIcon = {
                    IconButton(onClick = { confirmVisible = !confirmVisible }) {
                        Icon(
                            painter = painterResource(
                                id = if (passwordVisible)
                                    com.example.foodorder.R.drawable.outline_visibility_24
                                else
                                    R.drawable.outline_visibility_off_24
                            ),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = TextGrey,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                onDone = { focusManager.clearFocus() },
                isError = !passwordsMatch && confirmPassword.isNotEmpty(),
                visible = contentVisible,
                slideOffset = 95,
            )

            // Password match indicator
            if (confirmPassword.isNotEmpty()) {
                Spacer(Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (passwordsMatch) "✓ Passwords match" else "✗ Passwords do not match",
                        color = if (passwordsMatch) SuccessGreen else ErrorRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // SIGN UP button
            AnimatedVisibility(visible = contentVisible, enter = fadeIn() + slideInVertically { 110 }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .graphicsLayer(scaleX = btnScale, scaleY = btnScale)
                        .clip(RoundedCornerShape(28.dp))
                        .background(Brush.horizontalGradient(listOf(BrandOrange, BrandOrangeLight)))
                        .clickable(
                            MutableInteractionSource(), null,
                            enabled = !isLoading && passwordsMatch,
                        ) {
                            btnPressed = true
                            focusManager.clearFocus()
                            authViewModel.signUp(name, email, password)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp)
                    } else {
                        Text("CREATE ACCOUNT", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.5.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Log in link
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextGrey, fontSize = 13.sp)) { append("Already have an account? ") }
                    withStyle(SpanStyle(color = BrandOrange, fontWeight = FontWeight.Bold, fontSize = 13.sp)) { append("Log In") }
                },
                modifier = Modifier
                    .padding(bottom = 36.dp)
                    .clickable(MutableInteractionSource(), null, onClick = onNavigateToLogin),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
//  Reusable themed text field
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FoodTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onNext: (() -> Unit)? = null,
    onDone: (() -> Unit)? = null,
    isError: Boolean = false,
    visible: Boolean = true,
    slideOffset: Int = 60,
) {
    val brandOrange = Color(0xFFFF6B1A)
    val cardWhite   = Color(0xFFFFFFFF)
    val textDark    = Color(0xFF1A1A1A)
    val textGrey    = Color(0xFF9E9E9E)
    val errorRed    = Color(0xFFD32F2F)

    AnimatedVisibility(visible = visible, enter = fadeIn() + slideInVertically { slideOffset }) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = textGrey, fontSize = 14.sp) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            isError = isError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onNext = { onNext?.invoke() },
                onDone = { onDone?.invoke() },
            ),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor   = cardWhite,
                unfocusedContainerColor = cardWhite,
                focusedBorderColor      = if (isError) errorRed else brandOrange,
                unfocusedBorderColor    = if (isError) errorRed.copy(0.5f) else Color.Transparent,
                focusedTextColor        = textDark,
                unfocusedTextColor      = textDark,
                cursorColor             = brandOrange,
                errorBorderColor        = errorRed,
                errorCursorColor        = errorRed,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(4.dp, RoundedCornerShape(16.dp), ambientColor = Color.Black.copy(0.06f)),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SignUpPreview() {
    MaterialTheme { SignUpScreen(onSignUpSuccess = {}, onNavigateToLogin = {}) }
}
