package com.example.myworkoutplan

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
            val rootNavController = rememberNavController()
            val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val localNavController = rememberNavController()
            val isFirstLaunch by dataStore.isFirstLaunch.collectAsState(initial = true)
            val dao = WorkoutDatabase.getInstance(applicationContext).workoutDao()
            BubblePopAnimation(visible = isLoaded) {
                MyWorkoutPlanTheme(
                    themeOption = selectedTheme,
                    dynamicColorOption = dynamicColorOption
                ) {
                    NavHost(
                        navController = localNavController,
                        startDestination = if (isFirstLaunch) "Welcome" else "Adaptive",
                        enterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            ) + fadeIn(initialAlpha = 0.8f)
                        },
                        exitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            ) + fadeOut(targetAlpha = 0.9f)
                        },
                        popEnterTransition = {
                            slideIntoContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            ) + fadeIn(initialAlpha = 0.8f)
                        },
                        popExitTransition = {
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400, easing = FastOutSlowInEasing)
                            ) + fadeOut(targetAlpha = 0.9f)
                        }
                    ) {
                        composable("Welcome") {
                            WelcomeScreen(
                                onContinueClicked = {
                                    lifecycleScope.launch {
                                        insertInitialDataIfNeeded(dao, dataStore)
                                    }
                                }
                            )
                        }
                        composable("Adaptive") {
                            AdaptiveUI(
                                rootNavController = rootNavController, // Pass root controller to AdaptiveUI
                                currentRoute = currentRoute,
                            )
                        }
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