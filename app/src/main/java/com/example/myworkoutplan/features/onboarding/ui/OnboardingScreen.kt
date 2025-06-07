package com.example.myworkoutplan.features.onboarding.ui

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myworkoutplan.R
import com.example.myworkoutplan.features.auth.ui.LoginScreen
import com.example.myworkoutplan.features.auth.ui.SignupScreen
import kotlinx.serialization.Serializable


@Serializable
object Welcome
@Serializable
object LogIn
@Serializable
object SignUp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnboardingScreen() {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (isPortrait) {
            NavHost(
                startDestination = Welcome,
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
            ) {
                composable<Welcome> {
                    Scaffold(
                        floatingActionButton = {
                            ExtendedFloatingActionButton(
                                onClick = {
                                    navController.navigate(LogIn){
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Get Started"
                                    )
                                },
                                text = { Text("Get Started") },
                                containerColor = MaterialTheme.colorScheme.primary,
                                elevation = FloatingActionButtonDefaults.elevation(2.dp)
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 32.dp)
                                    .padding(bottom = 120.dp), // leave space for the bottom card
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.tracker), // replace with your drawable
                                    contentDescription = "Workout Icon",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .padding(bottom = 24.dp)
                                )

                                Text(
                                    text = "Welcome to My Workout Planner!",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Plan your workouts, track your progress, and stay fit every day.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
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
        } else {
            Box(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.systemBars)
            ) {
                Row {
                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.tracker),
                                contentDescription = "Workout Icon",
                                modifier = Modifier
                                    .size(120.dp)
                                    .padding(bottom = 24.dp)
                            )

                            Text(
                                text = "Welcome to My Workout Planner!",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Plan your workouts, track your progress, and stay fit every day.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.5f)
                            .fillMaxHeight()
                    ) {
                        Card(
                            shape = RoundedCornerShape(
                                topStart = 32.dp,
                                topEnd = 0.dp,
                                bottomEnd = 0.dp,
                                bottomStart = 32.dp
                            ),
                            modifier = Modifier
                                .fillMaxSize(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                        ) {
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
                            ) {
                                composable<LogIn> {
                                    LoginScreen(
                                        onLoginClicked = {
                                            navController.navigate(SignUp){
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                                composable<SignUp> {
                                    SignupScreen(
                                        onLoginClicked = {
                                            navController.navigate(LogIn){
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
}