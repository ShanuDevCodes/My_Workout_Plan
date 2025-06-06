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
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myworkoutplan.features.workoutsession.viewmodel.WorkoutSessionViewModel
import kotlinx.serialization.Serializable

@Serializable
data object WorkoutSession
@Serializable
data object Congratulation

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutSessionNav(workoutSessionViewModel: WorkoutSessionViewModel) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = WorkoutSession,
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
        }
    ){
        composable<WorkoutSession> {
            if (isPortrait){
                PortraitWorkoutScreen(workoutSessionViewModel, onCompleted = {
                    navController.navigate(Congratulation)
                })
            }else {
                LandscapeWorkoutScreen(workoutSessionViewModel, onCompleted = {
                    navController.navigate(Congratulation)
                })
            }
        }
        composable<Congratulation> {
            CongratulationsScreen(
                workoutSessionViewModel = workoutSessionViewModel,
                onDismiss = {
                    (context as? Activity)?.finish()
                }
            )
        }
    }
}