package com.novaterm.app.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.novaterm.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        // Animate in
        launch {
            alpha.animateTo(1f, animationSpec = tween(800, easing = EaseOut))
        }
        launch {
            scale.animateTo(1f, animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy))
        }
        delay(2200)
        // Navigate to onboarding for first launch, home otherwise
        // For now always go to onboarding (would check SharedPreferences in real app)
        onNavigateToOnboarding()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NovaBlack),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .alpha(alpha.value)
                .scale(scale.value)
        ) {
            // Logo text
            Text(
                text = "N",
                color = NovaBlue,
                fontSize = 96.sp,
                fontWeight = FontWeight.Black,
                fontFamily = MonoFontFamily
            )
            Text(
                text = "NovaTerm",
                color = NovaTextPrimary,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = MonoFontFamily
            )
            Text(
                text = "Linux Workstation for Android",
                color = NovaTextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Version at bottom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 48.dp)
                .alpha(alpha.value),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "v1.0.0",
                color = NovaTextMuted,
                style = MaterialTheme.typography.labelSmall,
                fontFamily = MonoFontFamily
            )
        }
    }
}
