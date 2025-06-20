@file:Suppress("DEPRECATION")

package com.example.myworkoutplan.theme

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

@Composable
fun MyWorkoutPlanTheme(
    themeOption: ThemeOptions,
    dynamicColorOption: DynamicColorOption,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val customLightColorScheme = lightColorScheme(
        primary = LightPrimary,
        onPrimary = LightOnPrimary,
        primaryContainer = LightPrimaryContainer,
        onPrimaryContainer = BlueDark,

        secondary = LightSecondary,
        onSecondary = Color.White,
        secondaryContainer = LightSecondaryContainer,
        onSecondaryContainer = LightPrimaryContainer,

        tertiary = BlueGray600,
        onTertiary = Color.White,
        tertiaryContainer = BlueGray200,
        onTertiaryContainer = BlueDark,

        background = SurfaceContainerLowestLightBlue,
        onBackground = BlueDark,

        surface = SurfaceContainerLightBlue,
        onSurface = BlueDark,
        surfaceVariant = SurfaceContainerHighLightBlue,
        onSurfaceVariant = BlueDark,
        surfaceTint = BluePrimary,

        error = Red,
        onError = Color.White,
        errorContainer = LightRed,
        onErrorContainer = BlueDark,

        outline = BlueGray600,
        outlineVariant = BlueGray400,

        inverseSurface = BlueDark,
        inverseOnSurface = Color.White,
        inversePrimary = BlueSecondary,

        scrim = ScrimColor,
        surfaceDim = SurfaceDimLightBlue,
        surfaceBright = SurfaceBrightLightBlue,
        surfaceContainerLowest = SurfaceContainerLowestLightBlue,
        surfaceContainerLow = SurfaceContainerLowLightBlue,
        surfaceContainer = SurfaceContainerLightBlue,
        surfaceContainerHigh = SurfaceContainerHighLightBlue,
        surfaceContainerHighest = SurfaceContainerHighestLightBlue
    )

    val customDarkColorScheme = darkColorScheme(
        primary = DarkPrimary,
        onPrimary = DarkOnPrimary,
        primaryContainer = DarkPrimaryContainer,
        onPrimaryContainer = Color.White,

        secondary = DarkSecondary,
        onSecondary = Color.White,
        secondaryContainer = DarkSecondaryContainer,
        onSecondaryContainer = Color(0xFFB3C9FF),

        tertiary = BlueGray400,
        onTertiary = BlueDark,
        tertiaryContainer = BlueGray600,
        onTertiaryContainer = Color.White,

        background = SurfaceContainerLowestDarkBlue,
        onBackground = Color.White,

        surface = SurfaceContainerDarkBlue,
        onSurface = Color(0xFFB7C2DE),

        surfaceVariant = SurfaceContainerHighDarkBlue,
        onSurfaceVariant = Color.White,

        surfaceTint = BluePrimary,

        error = LightRed,
        onError = BlueDark,
        errorContainer = Red,
        onErrorContainer = Color.White,

        outline = BlueGray400,
        outlineVariant = BlueGray600,

        inverseSurface = BlueGray100,
        inverseOnSurface = BlueDark,
        inversePrimary = BluePrimary,

        scrim = ScrimColor,

        surfaceDim = SurfaceDimDarkBlue,
        surfaceBright = SurfaceBrightDarkBlue,
        surfaceContainerLowest = SurfaceContainerLowestDarkBlue,
        surfaceContainerLow = SurfaceContainerLowDarkBlue,
        surfaceContainer = SurfaceContainerDarkBlue,
        surfaceContainerHigh = SurfaceContainerHighDarkBlue,
        surfaceContainerHighest = SurfaceContainerHighestDarkBlue
    )





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
        isDarkTheme -> customDarkColorScheme
        else -> customLightColorScheme
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
