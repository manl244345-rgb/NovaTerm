package com.novaterm.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = NovaBlue,
    onPrimary = Color.White,
    primaryContainer = NovaBlueDim,
    onPrimaryContainer = NovaBlueBright,
    secondary = NovaCyan,
    onSecondary = Color.White,
    secondaryContainer = NovaCyanDim,
    onSecondaryContainer = NovaCyanBright,
    tertiary = NovaPurple,
    onTertiary = Color.White,
    background = NovaBlack,
    onBackground = NovaTextPrimary,
    surface = NovaSurface,
    onSurface = NovaTextPrimary,
    surfaceVariant = NovaSurfaceVariant,
    onSurfaceVariant = NovaTextSecondary,
    error = NovaRed,
    onError = Color.White,
    errorContainer = NovaRedDim,
    onErrorContainer = Color(0xFFFFDAD6),
    outline = NovaTextMuted,
    outlineVariant = Color(0xFF2A2A3A),
    scrim = Color(0x80000000),
    inverseSurface = NovaTextPrimary,
    inverseOnSurface = NovaBlack,
    inversePrimary = NovaBlueDim,
)

private val LightColorScheme = lightColorScheme(
    primary = NovaLightPrimary,
    onPrimary = NovaLightOnPrimary,
    primaryContainer = Color(0xFFDEEAFF),
    onPrimaryContainer = Color(0xFF001A41),
    secondary = Color(0xFF0369A1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBAE6FD),
    onSecondaryContainer = Color(0xFF082F49),
    background = NovaLightBackground,
    onBackground = NovaLightText,
    surface = NovaLightSurface,
    onSurface = NovaLightText,
    surfaceVariant = Color(0xFFE8F0FE),
    onSurfaceVariant = NovaLightTextSecondary,
    error = Color(0xFFDC2626),
    onError = Color.White,
    outline = Color(0xFF94A3B8),
)

@Composable
fun NovaTermTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NovaTypography,
        content = content
    )
}
