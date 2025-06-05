package com.example.myworkoutplan

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.core.AppDatabase
import com.example.myworkoutplan.core.DataStoreManager
import com.example.myworkoutplan.data.local.workout.WorkoutViewModel
import com.example.myworkoutplan.data.local.workout.WorkoutViewModelFactory
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekViewModel
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekViewModelFactory
import com.example.myworkoutplan.features.settings.viewmodel.SettingsViewModel
import com.example.myworkoutplan.features.settings.viewmodel.SettingsViewModelFactory
import com.example.myworkoutplan.features.workoutsession.ui.LandscapeWorkoutScreen
import com.example.myworkoutplan.features.workoutsession.ui.PortraitWorkoutScreen
import com.example.myworkoutplan.features.workoutsession.viewmodel.WorkoutSessionViewModel
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
            val workoutSessionViewModel: WorkoutSessionViewModel = viewModel()
            val db = remember { AppDatabase.getInstance(applicationContext) }
            val workoutWeekDao = db.WorkoutWeekDao()
            val workoutWeekViewModel: WorkoutWeekViewModel = viewModel(
                factory = WorkoutWeekViewModelFactory(dataStore, workoutWeekDao)
            )
            LaunchedEffect(Unit) {
                workoutWeekViewModel.getDay()
            }
            val workoutWeekState by workoutWeekViewModel.state.collectAsState()
            val workoutPlan = workoutWeekState.currentWorkoutDay?.workoutType?:"Rest Day"
            val workoutDao = remember {db.workoutDao()}
            val workoutViewModel: WorkoutViewModel = viewModel(
                factory = WorkoutViewModelFactory(workoutDao)
            )
            val exerciseList by workoutViewModel.getExerciseNameAndImagePairsByType(workoutPlan)
                .collectAsState(initial = emptyList())
            val currentWorkout by workoutSessionViewModel.currentWorkout.collectAsState()
            LaunchedEffect(exerciseList, currentWorkout) {
                if (exerciseList.isNotEmpty() && currentWorkout.isEmpty()) {
                    workoutSessionViewModel.startSession(exerciseList)
                }
            }

            MyWorkoutPlanTheme(
                themeOption = selectedTheme,
                dynamicColorOption = dynamicColorOption
            ){
                if (isPortrait){
                    PortraitWorkoutScreen(workoutSessionViewModel)
                }else {
                    LandscapeWorkoutScreen(workoutSessionViewModel)
                }
            }
        }
    }
}