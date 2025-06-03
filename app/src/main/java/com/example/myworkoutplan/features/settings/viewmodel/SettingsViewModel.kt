package com.example.myworkoutplan.features.settings.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myworkoutplan.core.DataStoreManager
import com.example.myworkoutplan.theme.DynamicColorOption
import com.example.myworkoutplan.theme.ThemeOptions
import kotlinx.coroutines.launch

class SettingsViewModel(private val dataStore: DataStoreManager) : ViewModel() {

    private val _selectedTheme = mutableStateOf(ThemeOptions.SYSTEM_DEFAULT)
    val selectedTheme: ThemeOptions get() = _selectedTheme.value

    private val _dynamicColorOption = mutableStateOf(DynamicColorOption.ENABLED)
    val dynamicColorOption: DynamicColorOption get() = _dynamicColorOption.value

    private val _isSettingsLoaded = mutableStateOf(false)
    val isSettingsLoaded: Boolean get() = _isSettingsLoaded.value

    init {
        viewModelScope.launch {
            dataStore.themeFlow.collect { theme ->
                _selectedTheme.value = ThemeOptions.valueOf(theme)
                checkIfReady()
            }
        }
        viewModelScope.launch {
            dataStore.dynamicColorFlow.collect { dynamic ->
                _dynamicColorOption.value = DynamicColorOption.valueOf(dynamic)
                checkIfReady()
            }
        }
    }

    private fun checkIfReady() {
        if (true) {
            _isSettingsLoaded.value = true
        }
    }

    fun setThemeOption(theme: ThemeOptions) {
        viewModelScope.launch {
            dataStore.saveThemeOption(theme)
        }
    }

    fun updateDynamicColorOption(option: DynamicColorOption) {
        viewModelScope.launch {
            dataStore.saveDynamicColorOption(option)
        }
    }
}