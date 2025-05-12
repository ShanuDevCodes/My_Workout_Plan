package com.example.myworkoutplan.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.myworkoutplan.ui.theme.ThemeOptions


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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

