package com.example.myworkoutplan.ui.data

import com.example.myworkoutplan.ui.theme.DynamicColorOption
import com.example.myworkoutplan.ui.theme.ThemeOptions

data class AppSettings(
    val dynamicColor: DynamicColorOption = DynamicColorOption.ENABLED,
    val themeOption: ThemeOptions = ThemeOptions.SYSTEM_DEFAULT
)