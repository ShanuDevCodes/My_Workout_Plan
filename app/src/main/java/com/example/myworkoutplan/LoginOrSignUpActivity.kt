package com.example.myworkoutplan

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myworkoutplan.core.DataStoreManager
import com.example.myworkoutplan.features.auth.ui.LoginScreen
import com.example.myworkoutplan.features.auth.ui.SignupScreen
import com.example.myworkoutplan.features.onboarding.ui.LogIn
import com.example.myworkoutplan.features.onboarding.ui.SignUp
import com.example.myworkoutplan.features.settings.viewmodel.SettingsViewModel
import com.example.myworkoutplan.features.settings.viewmodel.SettingsViewModelFactory
import com.example.myworkoutplan.theme.MyWorkoutPlanTheme


class LoginOrSignUpActivity : ComponentActivity() {
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
            val navController = rememberNavController()
            MyWorkoutPlanTheme(
                themeOption = selectedTheme,
                dynamicColorOption = dynamicColorOption
            ){
                Surface {
                    NavHost(
                        startDestination = LogIn,
                        navController = navController,
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
                    ){
                        composable<LogIn> {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainerLow
                            ) {
                                LoginScreen(
                                    onLoginClicked = {
                                        navController.navigate(SignUp) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                        composable<SignUp> {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainerLow
                            ) {
                                SignupScreen(
                                    onLoginClicked = {
                                        navController.navigate(LogIn) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}