package com.example.myworkoutplan.ui

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myworkoutplan.ui.components.items
import com.example.myworkoutplan.ui.plans_navigation.PlansNavigator
import com.example.myworkoutplan.ui.screen.HomeScreen
import com.example.myworkoutplan.ui.screen.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdaptiveUI(rootNavController: NavHostController,currentRoute: String?){
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    if (isPortrait) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                contentWindowInsets = WindowInsets.systemBars,
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ) {
                        items.forEachIndexed { index, item ->
                            val isSelected = item.title.lowercase() == currentRoute
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    rootNavController.navigate(item.title.lowercase()) {
                                        popUpTo(rootNavController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                label = {
                                    Text(
                                        text = item.title,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                },
                                icon = {
                                    BadgedBox(
                                        badge = {}
                                    ) {
                                        Icon(
                                            imageVector = if (isSelected) {
                                                item.selectedIcon
                                            } else item.unselectedIcon,
                                            contentDescription = item.title,
                                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }
                            )
                        }
                    }
                },
            ) { innerPadding ->
                MainNavigation(innerPadding,rootNavController)
            }
        }
    }else{
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Row {
                NavigationRail(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                ) {
                    items.forEachIndexed { index, item ->
                        val isSelected = item.title.lowercase() == currentRoute
                        NavigationRailItem(
                            selected = isSelected,
                            onClick = {
                                rootNavController.navigate(item.title.lowercase()){
                                    popUpTo(rootNavController.graph.findStartDestination().id){
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = {
                                BadgedBox(badge = { }) {
                                    Icon(
                                        imageVector = if (isSelected) {
                                            item.selectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title,
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        )
                    }
                }
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    contentWindowInsets = WindowInsets.systemBars,
                ) { innerPadding ->
                     MainNavigation(innerPadding,rootNavController)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    innerPadding: PaddingValues,
    rootNavController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        NavHost(
            navController = rootNavController,
            startDestination = "home",
            enterTransition = {
                fadeIn(animationSpec = tween(durationMillis = 1))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(durationMillis = 1))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(durationMillis = 1))
            },
            popExitTransition = {
                fadeOut(animationSpec = tween(durationMillis = 1))
            }
        ) {
            composable("home") {
                HomeScreen()
            }
            composable("plans") {
                PlansNavigator()
            }
            composable("settings") {
                SettingsScreen()
            }
        }
    }
}