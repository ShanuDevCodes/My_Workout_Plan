package com.example.myworkoutplan.core

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myworkoutplan.theme.DynamicColorOption
import com.example.myworkoutplan.theme.ThemeOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val Context.dataStore by preferencesDataStore(name = "settings")

@RequiresApi(Build.VERSION_CODES.O)
class DataStoreManager(private val context: Context) {

    companion object {
        val THEME_KEY = stringPreferencesKey("theme_option")
        val DYNAMIC_COLOR_KEY = stringPreferencesKey("dynamic_color_option")
        val FIRST_LAUNCH_KEY = booleanPreferencesKey("first_launch_done")
        val LAST_RESET_DATE_KEY = stringPreferencesKey("last_reset_date")
        val WORKOUT_SPLIT_KEY = stringPreferencesKey("workout_split")
    }

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

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

    val isFirstLaunch: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[FIRST_LAUNCH_KEY] ?: true // true if key not set
    }

    suspend fun setFirstLaunchDone() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH_KEY] = false // Not first launch anymore
        }
    }

    fun getLastResetDate(): Flow<LocalDate?> = context.dataStore.data.map { preferences ->
        preferences[LAST_RESET_DATE_KEY]?.let { LocalDate.parse(it, formatter) }
    }

    suspend fun setLastResetDate(date: LocalDate) {
        context.dataStore.edit { preferences ->
            preferences[LAST_RESET_DATE_KEY] = date.format(formatter)
        }
    }

    val workoutSplitFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[WORKOUT_SPLIT_KEY] ?: "Push,Pull,Legs Split"
    }

    suspend fun setWorkoutSplit(split: String) {
        context.dataStore.edit { preferences ->
            preferences[WORKOUT_SPLIT_KEY] = split
        }
    }
}