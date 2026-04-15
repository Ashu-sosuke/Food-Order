package com.example.foodorder.presentation.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodorder.presentation.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

// ─── Colors ──────────────────────────────────────────────────────────────────

private val OrangeStart  = Color(0xFFFF6B1A)
private val OrangeEnd    = Color(0xFFFF9A3C)
private val OrangeDark   = Color(0xFFE85500)
private val White        = Color.White
private val WhiteFaded   = Color.White.copy(alpha = 0.15f)

// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun SplashScreen(
    onUserLoggedIn: () -> Unit,
    onGetStarted: () -> Unit,
    authViewModel: AuthViewModel = viewModel(),
) {
    LaunchedEffect(Unit) {
        delay(600)
        if (authViewModel.currentUser != null) {
            onUserLoggedIn()
        }
    }

    // Only show "Get Started" content when no user is logged in
    val showContent = authViewModel.currentUser == null

    // ── Entrance animations ───────────────────────────────────────────────────
    val logoScale by animateFloatAsState(
        targetValue = if (showContent) 1f else 0.5f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness    = Spring.StiffnessLow,
        ),
        label = "logoScale",
    )

    var textAlpha by remember { mutableFloatStateOf(0f) }
    var buttonAlpha by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(showContent) {
        if (showContent) {
            delay(300)
            textAlpha = 1f
            delay(250)
            buttonAlpha = 1f
        }
    }

    val textAlphaAnim by animateFloatAsState(
        targetValue = textAlpha,
        animationSpec = tween(600),
        label = "textAlpha",
    )
    val buttonAlphaAnim by animateFloatAsState(
        targetValue = buttonAlpha,
        animationSpec = tween(700),
        label = "buttonAlpha",
    )

    // ── Pulsing ring around logo ──────────────────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val ringScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue  = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "ringScale",
    )
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue  = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "ringAlpha",
    )

    // ─────────────────────────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(OrangeStart, OrangeEnd, OrangeDark)
                )
            ),
        contentAlignment = Alignment.Center,
    ) {

        // ── Background decorative circles ─────────────────────────────────────
        Box(
            modifier = Modifier
                .size(320.dp)
                .offset(x = 100.dp, y = (-180).dp)
                .background(WhiteFaded, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(200.dp)
                .offset(x = (-90).dp, y = 200.dp)
                .background(WhiteFaded, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(x = 130.dp, y = 240.dp)
                .background(Color.White.copy(alpha = 0.08f), CircleShape)
        )

        // ── Main content ──────────────────────────────────────────────────────
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
        ) {

            Spacer(modifier = Modifier.weight(1f))

            // Pulsing ring
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .scale(ringScale)
                        .alpha(ringAlpha)
                        .background(Color.White.copy(alpha = 0.3f), CircleShape)
                )

                // Logo circle
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .scale(logoScale)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = "🍔",
                        fontSize = 52.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // App name
            Text(
                text = "FoodDash",
                color = White,
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.alpha(textAlphaAnim),
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Delicious food,\ndelivered fast 🚀",
                color = White.copy(alpha = 0.90f),
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.alpha(textAlphaAnim),
            )

            Spacer(modifier = Modifier.weight(1f))

            // Feature pills
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.alpha(textAlphaAnim),
            ) {
                FeaturePill(emoji = "⚡", label = "Fast")
                FeaturePill(emoji = "🍕", label = "Tasty")
                FeaturePill(emoji = "💳", label = "Easy Pay")
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Get Started button
            Button(
                onClick = onGetStarted,
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = White,
                    contentColor   = OrangeStart,
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 2.dp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .alpha(buttonAlphaAnim),
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.5.sp,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Already have account link
            TextButton(
                onClick = onGetStarted,
                modifier = Modifier.alpha(buttonAlphaAnim),
            ) {
                Text(
                    text = "Already have an account? Log in",
                    color = White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun FeaturePill(emoji: String, label: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White.copy(alpha = 0.20f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(text = emoji, fontSize = 14.sp)
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SplashPreview() {
    SplashScreen(onUserLoggedIn = {}, onGetStarted = {})
}