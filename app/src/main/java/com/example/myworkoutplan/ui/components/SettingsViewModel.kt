package com.example.myworkoutplan.ui.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStore: DataStoreManager) : ViewModel() {

    var selectedTheme by mutableStateOf(ThemeOptions.SYSTEM_DEFAULT)
        private set

    var dynamicColorOption by mutableStateOf(DynamicColorOption.ENABLED)
        private set

    init {
        viewModelScope.launch {
            dataStore.themeFlow.collect { savedTheme ->
                selectedTheme = ThemeOptions.valueOf(savedTheme)
            }
        }
        viewModelScope.launch {
            dataStore.dynamicColorFlow.collect { savedOption ->
                dynamicColorOption = DynamicColorOption.valueOf(savedOption)
            }
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
