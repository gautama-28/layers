package com.shopease.app.ui.theme

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColors = lightColorScheme(
    primary = Orange40,
    onPrimary = Neutral99,
    primaryContainer = OrangeContainerLight,
    onPrimaryContainer = Neutral10,
    secondary = Teal40,
    onSecondary = Neutral99,
    background = Neutral99,
    onBackground = Neutral10,
    surface = Neutral95,
    onSurface = Neutral10,
    error = Error40
)

private val DarkColors = darkColorScheme(
    primary = Orange80,
    onPrimary = Neutral10,
    primaryContainer = Orange40,
    onPrimaryContainer = Neutral99,
    secondary = Teal80,
    onSecondary = Neutral10,
    background = Neutral10,
    onBackground = Neutral90,
    surface = Neutral20,
    onSurface = Neutral90,
    error = Error80
)

@Composable
fun ShopEaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color available on Android 12+; off by default so brand colors stay consistent.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ShopEaseTypography,
        content = content
    )
}
