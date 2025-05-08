package com.example.myworkoutplan.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStore: DataStoreManager) : ViewModel() {

    var selectedTheme by mutableStateOf(ThemeOptions.SYSTEM_DEFAULT)
        private set

    var dynamicColorOption by mutableStateOf(DynamicColorOption.ENABLED)
        private set

    var isSettingsLoaded by mutableStateOf(false)
        private set

    fun initSettings(theme: ThemeOptions, dynamic: DynamicColorOption) {
        selectedTheme = theme
        dynamicColorOption = dynamic
        isSettingsLoaded = true
    }
    init {
        viewModelScope.launch {
            val theme = dataStore.themeFlow.first()
            val dynamic = dataStore.dynamicColorFlow.first()
            initSettings(
                theme = ThemeOptions.valueOf(theme),
                dynamic = DynamicColorOption.valueOf(dynamic)
            )
        }
    }
    fun setThemeOption(theme: ThemeOptions) {
        selectedTheme = theme
        viewModelScope.launch {
            dataStore.saveThemeOption(theme)
        }
    }

    fun updateDynamicColorOption(option: DynamicColorOption) {
        dynamicColorOption = option
        viewModelScope.launch {
            dataStore.saveDynamicColorOption(option)
        }
    }
}
