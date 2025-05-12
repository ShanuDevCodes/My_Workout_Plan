package com.example.myworkoutplan.ui.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myworkoutplan.ui.theme.ThemeOptions
import com.example.myworkoutplan.ui.theme.DynamicColorOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(private val context: Context) {

    companion object {
        val THEME_KEY = stringPreferencesKey("theme_option")
        val DYNAMIC_COLOR_KEY = stringPreferencesKey("dynamic_color_option")
    }

    val themeFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: ThemeOptions.SYSTEM_DEFAULT.name
    }

    val dynamicColorFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[DYNAMIC_COLOR_KEY] ?: DynamicColorOption.ENABLED.name
    }

    suspend fun saveThemeOption(theme: ThemeOptions) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    suspend fun saveDynamicColorOption(option: DynamicColorOption) {
        context.dataStore.edit { preferences ->
            preferences[DYNAMIC_COLOR_KEY] = option.name
        }
    }
}