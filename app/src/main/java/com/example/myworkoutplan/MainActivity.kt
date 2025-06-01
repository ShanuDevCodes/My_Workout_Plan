package com.example.myworkoutplan

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.ui.AdaptiveUI
import com.example.myworkoutplan.ui.components.legWorkout
import com.example.myworkoutplan.ui.components.pullWorkout
import com.example.myworkoutplan.ui.components.pushWorkout
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutDao
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutDatabase
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutEvent
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutPlan
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutViewModel
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutViewModelFactory
import com.example.myworkoutplan.ui.data.DataStoreManager
import com.example.myworkoutplan.ui.screen.LoginScreen
import com.example.myworkoutplan.ui.screen.SignupScreen
import com.example.myworkoutplan.ui.settings.SettingsViewModel
import com.example.myworkoutplan.ui.settings.SettingsViewModelFactory
import com.example.myworkoutplan.ui.theme.MyWorkoutPlanTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dataStore = DataStoreManager(applicationContext)
            val dao = WorkoutDatabase.getInstance(applicationContext).workoutDao()
            val workoutViewModel: WorkoutViewModel = viewModel(
                factory = WorkoutViewModelFactory(dao)
            )
            var isFirstLaunch by remember { mutableStateOf<Boolean?>(null) }
            LaunchedEffect(Unit) {
                isFirstLaunch = dataStore.isFirstLaunch.first()
                if (isFirstLaunch == true){
                    withContext(Dispatchers.IO) {
                        workoutViewModel.onEvent(WorkoutEvent.ResetWorkoutDB)
                    }
                }
            }
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(dataStore)
            )
            val selectedTheme by remember { derivedStateOf { settingsViewModel.selectedTheme } }
            val dynamicColorOption by remember { derivedStateOf { settingsViewModel.dynamicColorOption } }
            val isLoaded by remember { derivedStateOf { settingsViewModel.isSettingsLoaded } }
            splashScreen.setKeepOnScreenCondition { !isLoaded || isFirstLaunch == null }
            LaunchedEffect(Unit) {
                isFirstLaunch = dataStore.isFirstLaunch.first()
                if (isFirstLaunch == true) {
                    startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
                    finish()
                }
            }
            if (isFirstLaunch == false) {
                MyWorkoutPlanTheme(
                    themeOption = selectedTheme,
                    dynamicColorOption = dynamicColorOption
                ) {
                    AdaptiveUI()
                    //LoginScreen()
                    //SignupScreen()
                }
            }
        }
    }
}