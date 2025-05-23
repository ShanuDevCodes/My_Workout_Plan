package com.example.myworkoutplan.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myworkoutplan.ui.components.items
import com.example.myworkoutplan.ui.navigation.NavigationViewModel
import com.example.myworkoutplan.ui.navigation.PlansNavigator
import com.example.myworkoutplan.ui.screen.HomeScreen
import com.example.myworkoutplan.ui.screen.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortraitUI(rootNavController: NavHostController,currentRoute: String?,viewModel: NavigationViewModel = viewModel()){


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
                                rootNavController.navigate(item.title.lowercase()){
                                    popUpTo(rootNavController.graph.findStartDestination().id){
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NavHost(
                    rootNavController,
                    startDestination = "home",
                    enterTransition = {
                        fadeIn(animationSpec = tween(durationMillis = 100))
                    },
                    exitTransition = {
                        fadeOut(animationSpec = tween(durationMillis = 100))
                    },
                    popEnterTransition = {
                        fadeIn(animationSpec = tween(durationMillis = 100))
                    },
                    popExitTransition = {
                        fadeOut(animationSpec = tween(durationMillis = 100))
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
    }
}