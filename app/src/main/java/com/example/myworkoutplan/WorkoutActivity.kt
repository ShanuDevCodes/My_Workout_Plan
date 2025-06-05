package com.example.myworkoutplan

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.core.DataStoreManager
import com.example.myworkoutplan.features.settings.viewmodel.SettingsViewModel
import com.example.myworkoutplan.features.settings.viewmodel.SettingsViewModelFactory
import com.example.myworkoutplan.features.workoutsession.ui.LandscapeWorkoutScreen
import com.example.myworkoutplan.features.workoutsession.ui.PortraitWorkoutScreen
import com.example.myworkoutplan.theme.MyWorkoutPlanTheme

class WorkoutActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dataStore = DataStoreManager(applicationContext)
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(dataStore)
            )
            val selectedTheme by remember { derivedStateOf { settingsViewModel.selectedTheme } }
            val dynamicColorOption by remember { derivedStateOf { settingsViewModel.dynamicColorOption } }
            val configuration = LocalConfiguration.current
            val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            MyWorkoutPlanTheme(
                themeOption = selectedTheme,
                dynamicColorOption = dynamicColorOption
            ){
                if (isPortrait){
                    PortraitWorkoutScreen()
                }else {
                    LandscapeWorkoutScreen()
                }
            }
        }
    }
}