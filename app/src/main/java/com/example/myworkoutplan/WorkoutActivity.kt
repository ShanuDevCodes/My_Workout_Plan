package com.example.myworkoutplan

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
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.core.AppDatabase
import com.example.myworkoutplan.core.DataStoreManager
import com.example.myworkoutplan.data.local.workout.WorkoutEvent
import com.example.myworkoutplan.data.local.workout.WorkoutViewModel
import com.example.myworkoutplan.data.local.workout.WorkoutViewModelFactory
import com.example.myworkoutplan.features.mainapp.viewmodel.HomeScreenViewModel
import com.example.myworkoutplan.features.mainapp.viewmodel.HomeScreenViewModelFactory
import com.example.myworkoutplan.features.settings.viewmodel.SettingsViewModel
import com.example.myworkoutplan.features.settings.viewmodel.SettingsViewModelFactory
import com.example.myworkoutplan.features.workoutsession.ui.WorkoutSessionNav
import com.example.myworkoutplan.features.workoutsession.viewmodel.WorkoutSessionViewModel
import com.example.myworkoutplan.theme.MyWorkoutPlanTheme

class WorkoutActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = if (!isTablet()) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }else{
            ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
        enableEdgeToEdge()
        setContent {
            val dataStore = DataStoreManager(applicationContext)
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(dataStore)
            )
            val selectedTheme by remember { derivedStateOf { settingsViewModel.selectedTheme } }
            val dynamicColorOption by remember { derivedStateOf { settingsViewModel.dynamicColorOption } }
            val workoutSessionViewModel: WorkoutSessionViewModel = viewModel()
            val db = remember { AppDatabase.getInstance(applicationContext) }
            val workoutDao = remember {db.WorkoutDao()}
            val workoutViewModel: WorkoutViewModel = viewModel(
                factory = WorkoutViewModelFactory(workoutDao)
            )
            val homeScreenViewModel: HomeScreenViewModel = viewModel(
                factory = HomeScreenViewModelFactory(dataStore)
            )
            val dayOfWeek = homeScreenViewModel.dayOfWeek.collectAsState()
            val workoutSplit by homeScreenViewModel.workoutSplitFlow.collectAsState(initial = "Push,Pull,Legs Split")
            val workoutState by workoutViewModel.state.collectAsState()
            LaunchedEffect(Unit) {
                workoutViewModel.onEvent(WorkoutEvent.GetSplitDayForSplitAndWeekDay(workoutSplit,dayOfWeek.value))
                workoutViewModel.onEvent(WorkoutEvent.GetWorkoutPlansForSplitAndWeekDay(workoutSplit,dayOfWeek.value))
            }
            val currentWorkout by workoutSessionViewModel.currentWorkout.collectAsState()
            LaunchedEffect(workoutState.workouts, currentWorkout) {
                if (workoutState.workouts.isNotEmpty() && currentWorkout.isEmpty()) {
                    workoutSessionViewModel.startSession(workoutState.workouts)
                }
            }

            MyWorkoutPlanTheme(
                themeOption = selectedTheme,
                dynamicColorOption = dynamicColorOption
            ){
                WorkoutSessionNav(workoutSessionViewModel)
            }
        }
    }
    private fun isTablet(): Boolean {
        return resources.configuration.smallestScreenWidthDp >= 600
    }
}