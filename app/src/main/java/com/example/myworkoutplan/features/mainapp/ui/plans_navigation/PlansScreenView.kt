package com.example.myworkoutplan.features.mainapp.ui.plans_navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myworkoutplan.R
import com.example.myworkoutplan.data.local.workout.WorkoutDatabase
import com.example.myworkoutplan.features.mainapp.data.WorkoutEvent
import com.example.myworkoutplan.features.mainapp.ui.workout.AddWorkoutDialog
import com.example.myworkoutplan.features.mainapp.viewmodel.PlansScreenViewModel
import com.example.myworkoutplan.features.mainapp.viewmodel.WorkoutViewModel
import com.example.myworkoutplan.features.mainapp.viewmodel.WorkoutViewModelFactory
import com.example.myworkoutplan.features.mainapp.ui.workout.DayScreen
import com.example.myworkoutplan.features.mainapp.ui.workout.DeleteScreen
import kotlinx.coroutines.delay

@Composable
fun PlansScreenView(dayTitle: String,viewModel: PlansScreenViewModel = viewModel()) {
    // Animation for scale and vertical offset
    val fabScale = remember { Animatable(0f) }
    val fabScaleMini = remember { Animatable(0f) }
    val fabOffsetY = remember { Animatable(0f) }
    val addFabOffsetX = remember { Animatable(0f) }
    val deleteFabOffsetX = remember { Animatable(0f) }
    val visible = viewModel.visible
    val navController = rememberNavController()
    // Database and ViewModel setup
    val context = LocalContext.current
    val dao = remember { WorkoutDatabase.getInstance(context).workoutDao() }
    val workoutViewModel: WorkoutViewModel = viewModel(
        factory = WorkoutViewModelFactory(dao)
    )
    val workoutState by workoutViewModel.state.collectAsState()
    var isExpanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "fab_rotation"
    )
    LaunchedEffect(Unit) {
        delay(300)
        viewModel.show()
    }
    LaunchedEffect(Unit) {
        delay(350)
        // Animate both offset and scale in parallel
        fabScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
        )
    }
    LaunchedEffect(Unit) {
        delay(600)
        // Animate both offset and scale in parallel
        fabScaleMini.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 230, easing = FastOutSlowInEasing)
        )
    }
    LaunchedEffect(Unit) {
        delay(600)
        fabOffsetY.animateTo(
            targetValue = -55f,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        )
    }
    LaunchedEffect(isExpanded) {
        addFabOffsetX.animateTo(
            targetValue = if (isExpanded) -60f else 0f,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        )
    }
    LaunchedEffect(isExpanded) {
        deleteFabOffsetX.animateTo(
            targetValue = if (isExpanded) -115f else 0f,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        )
    }

    NavHost(
        navController = navController,
        startDestination = "PlanScreenView",
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
        composable("PlanScreenView") {
            Box(modifier = Modifier.fillMaxSize()) {
                DayScreen(
                    visible = visible,
                    dayTitle = dayTitle,
                    workoutViewModel = workoutViewModel
                )
                if (isExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    isExpanded = !isExpanded
                                }
                            )
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 8.dp, bottom = 8.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        FloatingActionButton(
                            onClick = { /* Secondary FAB action */ },
                            modifier = Modifier
                                .size(40.dp)
                                .offset(y = fabOffsetY.value.dp)
                                .scale(fabScaleMini.value),
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            elevation = FloatingActionButtonDefaults.elevation(2.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ai),
                                contentDescription = "AI",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        // Main FAB (Add)
                        FloatingActionButton(
                            onClick = {
                                workoutViewModel.onEvent(WorkoutEvent.ShowDialog)
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .scale(fabScaleMini.value)
                                .offset(x = addFabOffsetX.value.dp),
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            elevation = FloatingActionButtonDefaults.elevation(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                            )
                        }
                        FloatingActionButton(
                            onClick = {
                                navController.navigate("Delete"){
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .scale(fabScaleMini.value)
                                .offset(x = deleteFabOffsetX.value.dp),
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            elevation = FloatingActionButtonDefaults.elevation(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                            )
                        }
                        FloatingActionButton(
                            onClick = {
                                isExpanded = !isExpanded
                            },
                            modifier = Modifier
                                .size(56.dp)
                                .scale(fabScale.value),
                            containerColor = MaterialTheme.colorScheme.primary,
                            elevation = FloatingActionButtonDefaults.elevation(2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                modifier = Modifier
                                    .rotate(rotation)
                            )
                        }
                    }
                }
                if (workoutState.isAddingWorkout) {
                    AddWorkoutDialog(
                        state = workoutState,
                        workoutCategory = dayTitle,
                        onEvent = workoutViewModel::onEvent
                    )
                }
            }
        }
        composable("Delete") {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                DeleteScreen(
                    dayTitle = dayTitle,
                    workoutViewModel = workoutViewModel
                )
            }
        }
    }
}