package com.example.myworkoutplan

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.core.AppDatabase
import com.example.myworkoutplan.core.DataStoreManager
import com.example.myworkoutplan.data.local.workout.WorkoutEvent
import com.example.myworkoutplan.data.local.workout.WorkoutViewModel
import com.example.myworkoutplan.data.local.workout.WorkoutViewModelFactory
import com.example.myworkoutplan.features.mainapp.ui.AdaptiveUI
import com.example.myworkoutplan.features.settings.viewmodel.SettingsViewModel
import com.example.myworkoutplan.features.settings.viewmodel.SettingsViewModelFactory
import com.example.myworkoutplan.theme.MyWorkoutPlanTheme
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        requestedOrientation = if (!isTablet()) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        enableEdgeToEdge()
        setContent {
            val dataStore = DataStoreManager(applicationContext)
            val db = AppDatabase.getInstance(applicationContext)
            val dao = db.WorkoutDao()
            val workoutViewModel: WorkoutViewModel = viewModel(
                factory = WorkoutViewModelFactory(dao)
            )
            val workoutState by workoutViewModel.state.collectAsState()
            var isFirstLaunch by remember { mutableStateOf<Boolean?>(null) }
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(dataStore)
            )
            val selectedTheme by remember { derivedStateOf { settingsViewModel.selectedTheme } }
            val dynamicColorOption by remember { derivedStateOf { settingsViewModel.dynamicColorOption } }
            val isLoaded by remember { derivedStateOf { settingsViewModel.isSettingsLoaded } }
            val todayDayNumber: Int = LocalDate.now().dayOfWeek.value
            val today = LocalDate.now().toString()
            // 1. Handle first launch and onboarding
            splashScreen.setKeepOnScreenCondition { !isLoaded || isFirstLaunch == null }
            LaunchedEffect(Unit) {
                isFirstLaunch = dataStore.isFirstLaunch.first()
                if (isFirstLaunch == true) {
                    workoutViewModel.initialiseDB()
                    startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
                    finish()
                }
            }

            // 2. Always trigger the fetch event for today's split day
            LaunchedEffect(Unit) {
                val storedSplit = dataStore.workoutSplitFlow.first()
                workoutViewModel.onEvent(
                    WorkoutEvent.GetSplitDayForSplitAndWeekDay(storedSplit, todayDayNumber)
                )
            }

            // 3. Only set the workout day in DataStore after splitDay is loaded and date is new
            LaunchedEffect(workoutState.splitDay) {
                val storedDate = dataStore.currentDateFlow.first()
                if (workoutState.splitDay != null && (storedDate == null || storedDate != today)) {
                    dataStore.setWorkoutDay(workoutState.splitDay?.splitDayName?:"Push Day")
                    dataStore.setCurrentDate(today)
                }
            }
            if (isFirstLaunch == false) {
                MyWorkoutPlanTheme(
                    themeOption = selectedTheme,
                    dynamicColorOption = dynamicColorOption
                ) {
                    AdaptiveUI()
                }
            }
        }
    }

    private fun isTablet(): Boolean {
        return resources.configuration.smallestScreenWidthDp >= 600
    }
}
