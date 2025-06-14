package com.example.myworkoutplan.features.workoutsession.ui

import android.app.Activity
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.Connector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.example.myworkoutplan.features.workoutsession.viewmodel.WorkoutSessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogWorkoutScreen(workoutSessionViewModel: WorkoutSessionViewModel, onConfirm: () -> Unit){
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val completedWorkouts by workoutSessionViewModel.completedWorkouts.collectAsState()
    val exitDialog by workoutSessionViewModel.exitDialog.collectAsState()
    val lazyListState = rememberLazyListState()
    var isScrollingDown by remember { mutableStateOf(false) }

    LaunchedEffect(lazyListState) {
        var previousIndex = 0
        var previousScrollOffset = 0

        snapshotFlow {
            lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
        }.collect { (currentIndex, currentScrollOffset) ->

            isScrollingDown = if (currentIndex != previousIndex) {
                currentIndex > previousIndex
            } else {
                currentScrollOffset > previousScrollOffset
            }

            previousIndex = currentIndex
            previousScrollOffset = currentScrollOffset
        }
    }

    val showExtended by remember {
        derivedStateOf {
            // Always expanded at top, otherwise based on scroll direction
            lazyListState.firstVisibleItemIndex == 0 || !isScrollingDown
        }
    }
    BackHandler {
        workoutSessionViewModel.showExitDialog()
    }
    Surface(modifier = Modifier
        .fillMaxSize()
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets.systemBars,
            bottomBar = {
                if (isPortrait) {
                    BottomAppBar {
                        Button(
                            onClick = {
                                onConfirm()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Submit",
                                modifier = Modifier.size(24.dp) // Increased from 18dp
                            )
                            Spacer(modifier = Modifier.width(12.dp)) // Increased spacing proportionally
                            Text(
                                text = "Submit",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            },
        ) {innerPadding->
            val flattenedList = completedWorkouts.flatMap { (workout, sets) ->
                listOf(workout to null) + (1..sets).map { setNumber -> workout to setNumber }
            }
            Box(modifier = Modifier
                .padding(innerPadding)
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxSize()
            ) {
                LazyColumn(
                    state = lazyListState
                ) {
                    item {
                        Text(
                            text = "Log Completed Workouts",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    items(
                        flattenedList,
                        key = { (workout, setNumber) -> "${workout.id}-${setNumber ?: "title"}" }) { (workout, setNumber) ->
                        if (setNumber == null) {
                            Text(
                                text = workout.exerciseName,
                                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        } else {
                            WorkoutSetCard(setNumber = setNumber)
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 8.dp, bottom = 8.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    if (!isPortrait) {
                        ExtendedFloatingActionButton(
                            onClick = {
                                onConfirm()
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Send",
                                    modifier = Modifier.size(34.dp)
                                )
                            },
                            text = {
                                Text(
                                    text = "Submit",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            containerColor = MaterialTheme.colorScheme.primary,
                            elevation = FloatingActionButtonDefaults.elevation(2.dp),
                            expanded = showExtended
                        )
                    }
                }
            }
        }
        if (exitDialog){
            ExitWarningDialog(
                title = "Workout Log Screen",
                onDismiss = {
                    workoutSessionViewModel.dismissExitDialog()
                },
                onConfirm = {
                    workoutSessionViewModel.dismissExitDialog()
                    (context as? Activity)?.finish()
                }
            )
        }
    }
}