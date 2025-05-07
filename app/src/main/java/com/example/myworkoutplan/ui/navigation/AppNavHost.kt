package com.example.myworkoutplan.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myworkoutplan.ui.Day
import com.example.myworkoutplan.ui.Home
import com.example.myworkoutplan.ui.Plans
import com.example.myworkoutplan.ui.screen.DayScreen
import com.example.myworkoutplan.ui.screen.HomeScreen
import com.example.myworkoutplan.ui.screen.PlansScreen

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable < Home >(
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
        composable < Plans >(
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
        composable < Day >(
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
            DayScreen()
        }
}
}