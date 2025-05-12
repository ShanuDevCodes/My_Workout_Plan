package com.example.myworkoutplan

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.ui.LandscapeUI
import com.example.myworkoutplan.ui.PortraitUI
import com.example.myworkoutplan.ui.components.BubblePopAnimation
import com.example.myworkoutplan.ui.data.DataStoreManager
import com.example.myworkoutplan.ui.settings.SettingsViewModel
import com.example.myworkoutplan.ui.settings.SettingsViewModelFactory
import com.example.myworkoutplan.ui.theme.MyWorkoutPlanTheme

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,

)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dataStore = DataStoreManager(applicationContext)
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(dataStore)
            )
            val configuration = LocalConfiguration.current
            val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            BubblePopAnimation(visible = settingsViewModel.isSettingsLoaded) {
                MyWorkoutPlanTheme(
                    themeOption = settingsViewModel.selectedTheme,
                    dynamicColorOption = settingsViewModel.dynamicColorOption
                ) {
                    if (isPortrait) {
                        PortraitUI()
                    }else{
                        LandscapeUI()
                    }
                }
            }
        }
    }
}