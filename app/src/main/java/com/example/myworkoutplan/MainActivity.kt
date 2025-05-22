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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myworkoutplan.ui.LandscapeUI
import com.example.myworkoutplan.ui.PortraitUI
import com.example.myworkoutplan.ui.components.BubblePopAnimation
import com.example.myworkoutplan.ui.data.DataStoreManager
import com.example.myworkoutplan.ui.settings.SettingsViewModel
import com.example.myworkoutplan.ui.settings.SettingsViewModelFactory
import com.example.myworkoutplan.ui.theme.MyWorkoutPlanTheme

class MainActivity : ComponentActivity() {
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
            val isLoaded by remember { derivedStateOf { settingsViewModel.isSettingsLoaded } }

            val configuration = LocalConfiguration.current
            val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            val rootNavController = rememberNavController()
            val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            BubblePopAnimation(visible = isLoaded) {
                MyWorkoutPlanTheme(
                    themeOption = selectedTheme,
                    dynamicColorOption = dynamicColorOption
                ) {
                    if (isPortrait) {
                        PortraitUI(rootNavController,currentRoute)
                    } else {
                        LandscapeUI(rootNavController,currentRoute)
                    }
                }
            }
        }
    }
}