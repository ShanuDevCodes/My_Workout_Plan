package com.example.myworkoutplan.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.myworkoutplan.ui.screen.DayScreen
import com.example.myworkoutplan.ui.screen.HomeScreen
import com.example.myworkoutplan.ui.screen.PlansScreen
import com.example.myworkoutplan.ui.screen.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home>(
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
            HomeScreen()
        }
        composable<Plans>(
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
            PlansScreen()
        }
        composable<Day>(
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
            val args = it.toRoute<Day>()
            DayScreen(args.dayTitle, args.workoutList)
        }
    }
}