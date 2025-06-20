package com.example.myworkoutplan.features.workoutsession.ui

import CongratulationsScreen
import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myworkoutplan.features.workoutsession.viewmodel.WorkoutSessionViewModel
import kotlinx.serialization.Serializable

@Serializable
sealed class  WorkoutSessionDestination {
    @Serializable
    data object WorkoutSession: WorkoutSessionDestination()

    @Serializable
    data object LogWorkout: WorkoutSessionDestination()

    @Serializable
    data object Congratulation:  WorkoutSessionDestination()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutSessionNav(workoutSessionViewModel: WorkoutSessionViewModel) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val navController = rememberNavController()
    Surface {
        NavHost(
            navController = navController,
            startDestination = WorkoutSessionDestination.WorkoutSession,
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
            composable<WorkoutSessionDestination.WorkoutSession> {
                if (isPortrait) {
                    PortraitWorkoutScreen(workoutSessionViewModel, onCompleted = {
                        navController.navigate(WorkoutSessionDestination.LogWorkout) {
                            launchSingleTop
                        }
                    })
                } else {
                    LandscapeWorkoutScreen(workoutSessionViewModel, onCompleted = {
                        navController.navigate(WorkoutSessionDestination.LogWorkout) {
                            launchSingleTop
                        }
                    })
                }
            }
            composable<WorkoutSessionDestination.LogWorkout> {
                LogWorkoutScreen(workoutSessionViewModel, onConfirm = {
                    navController.navigate(WorkoutSessionDestination.Congratulation) {
                        launchSingleTop
                    }
                })
            }
            composable<WorkoutSessionDestination.Congratulation> {
                CongratulationsScreen(
                    onDismiss = {
                        (context as? Activity)?.finish()
                    },
                    workoutSessionViewModel=workoutSessionViewModel
                )
            }
        }
    }
}