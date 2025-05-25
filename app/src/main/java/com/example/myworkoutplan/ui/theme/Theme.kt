@file:Suppress("DEPRECATION")

package com.example.myworkoutplan.ui.theme

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

@Composable
fun MyWorkoutPlanTheme(
    themeOption: ThemeOptions,
    dynamicColorOption: DynamicColorOption,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isDarkTheme = when (themeOption) {
        ThemeOptions.SYSTEM_DEFAULT -> isSystemInDarkTheme()
        ThemeOptions.LIGHT -> false
        ThemeOptions.DARK -> true
    }

    val supportsDynamic = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val useDynamic = dynamicColorOption == DynamicColorOption.ENABLED && supportsDynamic

    val colorScheme = when {
        useDynamic && isDarkTheme -> dynamicDarkColorScheme(context)
        useDynamic && !isDarkTheme -> dynamicLightColorScheme(context)
        isDarkTheme -> darkColorScheme()
        else -> lightColorScheme()
    }

    // Handle system bar appearance
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // Set status bar color to match your app theme
            window.statusBarColor = colorScheme.surface.toArgb()

            // Set navigation bar color to match your app theme
            window.navigationBarColor = colorScheme.surface.toArgb()

            // Make icons dark/light based on YOUR app theme, not system theme
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !isDarkTheme
                isAppearanceLightNavigationBars = !isDarkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
