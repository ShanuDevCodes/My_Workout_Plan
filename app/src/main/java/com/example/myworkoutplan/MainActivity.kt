package com.example.myworkoutplan

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myworkoutplan.ui.AdaptiveUI
import com.example.myworkoutplan.ui.components.BubblePopAnimation
import com.example.myworkoutplan.ui.components.legWorkout
import com.example.myworkoutplan.ui.components.pullWorkout
import com.example.myworkoutplan.ui.components.pushWorkout
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutDao
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutDatabase
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutPlan
import com.example.myworkoutplan.ui.data.DataStoreManager
import com.example.myworkoutplan.ui.screen.WelcomeScreen
import com.example.myworkoutplan.ui.settings.SettingsViewModel
import com.example.myworkoutplan.ui.settings.SettingsViewModelFactory
import com.example.myworkoutplan.ui.theme.MyWorkoutPlanTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
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
            val localNavController = rememberNavController()
            val isFirstLaunch by dataStore.isFirstLaunch.collectAsState(initial = true)
            val dao = WorkoutDatabase.getInstance(applicationContext).workoutDao()
            BubblePopAnimation(visible = isLoaded) {
                MyWorkoutPlanTheme(
                    themeOption = selectedTheme,
                    dynamicColorOption = dynamicColorOption
                ) {

                    var isRestoringState by remember { mutableStateOf(true) }

                    LaunchedEffect(Unit) {
                        // Allow animations after initial composition
                        delay(200)
                        isRestoringState = false
                    }
                    if (isFirstLaunch) {
                        NavHost(
                            navController = localNavController,
                            startDestination = "Welcome",
                            enterTransition = {
                                if (isRestoringState) {
                                    EnterTransition.None
                                } else {
                                    slideIntoContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(
                                            600,
                                            easing = CubicBezierEasing(0.1f, 0.1f, 0.25f, 1f)
                                        )
                                    )
                                }
                            },
                            exitTransition = {
                                if (isRestoringState) {
                                    ExitTransition.None
                                } else {
                                    // Background slides slightly left and darkens
                                    slideOutOfContainer(
                                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                        animationSpec = tween(500),
                                        targetOffset = { (it * 0.2f).toInt() } // Only moves 30% of screen width
                                    )
                                }
                            },
                        ) {
                            composable("Welcome") {
                                WelcomeScreen(
                                    onContinueClicked = {
                                        localNavController.navigate("Adaptive")
                                        lifecycleScope.launch {
                                            delay(470)
                                            insertInitialDataIfNeeded(dao, dataStore)
                                        }
                                    }
                                )
                            }
                            composable("Adaptive") {
                                AdaptiveUI()
                            }
                        }
                    } else {
                        AdaptiveUI()
                    }
                }
            }
        }
    }
    private fun insertInitialDataIfNeeded(dao: WorkoutDao, dataStoreManager: DataStoreManager) {
        lifecycleScope.launch {
            val isFirstTime = dataStoreManager.isFirstLaunch.first()
            if (isFirstTime) {
                val push = pushWorkout.map { (name, image) ->
                    WorkoutPlan(name, image, "Push Day", R.drawable.push_day)
                }
                val pull = pullWorkout.map { (name, image) ->
                    WorkoutPlan(name, image, "Pull Day", R.drawable.pull_day)
                }
                val leg = legWorkout.map { (name, image) ->
                    WorkoutPlan(name, image, "Leg Day", R.drawable.leg_day)
                }

                (push + pull + leg).forEach { dao.upsertWorkout(it) }

                dataStoreManager.setFirstLaunchDone()
            }
        }
    }

}