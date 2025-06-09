package com.example.myworkoutplan.features.mainapp.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.myworkoutplan.R
import com.example.myworkoutplan.WorkoutActivity
import com.example.myworkoutplan.core.AppDatabase
import com.example.myworkoutplan.core.DataStoreManager
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekEvent
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekViewModel
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekViewModelFactory
import com.example.myworkoutplan.features.mainapp.data.items
import com.example.myworkoutplan.features.mainapp.ui.homescreen.HomeScreen
import com.example.myworkoutplan.features.mainapp.ui.plans_navigation.PlansScreenView
import com.example.myworkoutplan.features.mainapp.ui.workout.PlansScreen
import com.example.myworkoutplan.features.profile.ui.ProfileScreen
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    data object Home : Destination()

    @Serializable
    data object Plan : Destination()

    @Serializable
    data object Profile : Destination()

    @Serializable
    data object Report : Destination()

    @Serializable
    data object WorkoutList : Destination()

}
@Serializable
sealed class PlanDestination{
    @Serializable
    data class Day(
        val dayTitle: String,
    )
    @Serializable
    object Plans
}

@SuppressLint("UseOfNonLambdaOffsetOverload")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AdaptiveUI(){
    val context = LocalContext.current
    val rootNavController = rememberNavController()
    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val dataStore = remember { DataStoreManager(context) }
    val db = remember { AppDatabase.getInstance(context) }
    val dao = db.WorkoutWeekDao()
    val workoutWeekViewModel: WorkoutWeekViewModel = viewModel(
        factory = WorkoutWeekViewModelFactory(dataStore, dao)
    )
    val workoutWeekState by workoutWeekViewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        workoutWeekViewModel.getDay()
    }
    if (isPortrait) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                contentWindowInsets = WindowInsets.systemBars,
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        items.forEachIndexed { index, item ->
                            val isSelected = currentDestination?.hierarchy?.any { it.route == item.destination::class.qualifiedName } == true
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    rootNavController.navigate(item.destination) {
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
                                },
                                interactionSource = NoRippleInteractionSource
                            )
                        }
                    }
                },
            ) { innerPadding ->
                MainNavigation(innerPadding,rootNavController,workoutWeekViewModel)
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
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(80.dp)
                    ) {
                        // 🔵 FAB layered on top (does not consume layout space)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            val fabScaleMini = remember { Animatable(0f) }
                            val fabOffsetY = remember { Animatable(0f) }
                            val isOnHomeScreen = currentDestination?.route == Destination.Home::class.qualifiedName
                            LaunchedEffect(isOnHomeScreen) {
                                val targetOffset = if (isOnHomeScreen) 66f else 0f
                                val targetScale = if (isOnHomeScreen) 1f else 0f

                                // Animate both in parallel
                                coroutineScope {
                                    launch {
                                        fabOffsetY.animateTo(
                                            targetValue = targetOffset,
                                            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                                        )
                                    }
                                    launch {
                                        fabScaleMini.animateTo(
                                            targetValue = targetScale,
                                            animationSpec = tween(durationMillis = 230, easing = FastOutSlowInEasing)
                                        )
                                    }
                                }
                            }
                            if (workoutWeekState.currentWorkoutDay?.workoutType != "Rest Day") {
                                FloatingActionButton(
                                    onClick = { workoutWeekViewModel.onEvent(WorkoutWeekEvent.ShowSwapDialog) },
                                    modifier = Modifier
                                        .size(48.dp)
                                        .offset(y = fabOffsetY.value.dp)
                                        .scale(fabScaleMini.value),
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                    elevation = FloatingActionButtonDefaults.elevation(2.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.shuffle),
                                        contentDescription = "Swap",
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                FloatingActionButton(
                                    onClick = {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                WorkoutActivity::class.java
                                            )
                                        )
                                    },
                                    modifier = Modifier,
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.start_button),
                                        contentDescription = "Start",
                                        modifier = Modifier.size(34.dp),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }

                        // 🟢 NavigationRail content centered, completely unaware of FAB
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .align(Alignment.Center),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            items.forEach { item ->
                                val isSelected = currentDestination?.hierarchy?.any {
                                    it.route == item.destination::class.qualifiedName
                                } == true

                                NavigationRailItem(
                                    selected = isSelected,
                                    onClick = {
                                        rootNavController.navigate(item.destination) {
                                            popUpTo(rootNavController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                    icon = {
                                        BadgedBox(badge = { }) {
                                            Icon(
                                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                                contentDescription = item.title,
                                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        }
                                    },
                                    interactionSource = NoRippleInteractionSource,
                                )
                            }
                        }
                    }
                }
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentWindowInsets = WindowInsets.systemBars,
                ) { innerPadding ->
                    MainNavigation(innerPadding,rootNavController,workoutWeekViewModel)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainNavigation(
    innerPadding: PaddingValues,
    rootNavController: NavHostController,
    workoutWeekViewModel: WorkoutWeekViewModel
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        NavHost(
            navController = rootNavController,
            startDestination = Destination.Home,
            enterTransition = {
                fadeIn(animationSpec = tween(durationMillis = 1))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(durationMillis = 1))
            },
        ) {
            composable<Destination.Home> {
                HomeScreen(workoutWeekViewModel)
            }
            composable<Destination.WorkoutList> {
                WorkoutListScreen()
            }
            navigation<Destination.Plan>(
                startDestination = PlanDestination.Plans,
                enterTransition = {
                    val from = initialState.destination.route
                    val to = targetState.destination.route
                    if (from?.contains("PlanDestination") == true && to?.contains("PlanDestination") == true) {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ) + fadeIn(initialAlpha = 0.8f)
                    } else {
                        null
                    }
                },
                exitTransition = {
                    val from = initialState.destination.route
                    val to = targetState.destination.route
                    if (from?.contains("PlanDestination") == true && to?.contains("PlanDestination") == true) {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ) + fadeOut(targetAlpha = 0.9f)
                    } else {
                        null
                    }
                },
                popEnterTransition = {
                    val from = initialState.destination.route
                    val to = targetState.destination.route
                    if (from?.contains("PlanDestination") == true && to?.contains("PlanDestination") == true) {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ) + fadeIn(initialAlpha = 0.8f)
                    } else {
                        null
                    }
                },
                popExitTransition = {
                    val from = initialState.destination.route
                    val to = targetState.destination.route
                    if (from?.contains("PlanDestination") == true && to?.contains("PlanDestination") == true) {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(400, easing = FastOutSlowInEasing)
                        ) + fadeOut(targetAlpha = 0.9f)
                    } else {
                        null
                    }
                }
            ) {
                composable<PlanDestination.Plans> {
                    PlansScreen(rootNavController)
                }
                composable<PlanDestination.Day> {
                    val args = it.toRoute<PlanDestination.Day>()
                    PlansScreenView(args.dayTitle)
                }
            }
            composable<Destination.Report> {
                ReportScreen()
            }
            composable<Destination.Profile> {
                ProfileScreen()
            }
        }
    }
}
private object NoRippleInteractionSource : MutableInteractionSource {

    override val interactions: Flow<Interaction> = emptyFlow()

    override suspend fun emit(interaction: Interaction) {}

    override fun tryEmit(interaction: Interaction) = true
}