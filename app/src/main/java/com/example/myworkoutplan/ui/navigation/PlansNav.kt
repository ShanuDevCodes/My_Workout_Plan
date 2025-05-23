package com.example.myworkoutplan.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.myworkoutplan.ui.components.legWorkout
import com.example.myworkoutplan.ui.components.pullWorkout
import com.example.myworkoutplan.ui.components.pushWorkout
import com.example.myworkoutplan.ui.screen.PlansScreen
import com.example.myworkoutplan.ui.screen.PlansScreenView

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlansNavigator() {
    val navController = rememberNavController()
    Column() {
        Text(
            text = "Plans",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )
        NavHost(
            navController = navController,
            startDestination = Plans,
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
            composable<Plans> {
                PlansScreen(navController)
            }

            composable<Day> {
                val args = it.toRoute<Day>()
                val workoutList = when (args.dayTitle) {
                    "Push Day" -> pushWorkout
                    "Pull Day" -> pullWorkout
                    "Leg Day" -> legWorkout
                    else -> emptyList()
                }
                PlansScreenView(args.dayTitle, workoutList)
            }
        }
    }
}