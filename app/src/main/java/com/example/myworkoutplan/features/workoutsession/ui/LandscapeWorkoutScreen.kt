package com.example.myworkoutplan.features.workoutsession.ui

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myworkoutplan.features.workoutsession.viewmodel.WorkoutSessionViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeWorkoutScreen(workoutSessionViewModel:WorkoutSessionViewModel) {
    val context = LocalContext.current
    val isRunning by workoutSessionViewModel.isRunningState.collectAsState()
    val timeInMillis by workoutSessionViewModel.timeInMillisState.collectAsState()
    val scope = rememberCoroutineScope()
    val isCompleted by workoutSessionViewModel.isCompleted.collectAsState()

    BackHandler {
        (context as? Activity)?.finish()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {

                // Top-start Back Button
                TopAppBar(
                    title = {
                        Text(
                            text = "Workout Session",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            (context as? Activity)?.finish()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )

                // Main Row: Timer and Lap list
                Row(modifier = Modifier.fillMaxSize()) {

                    // Left side - Timer display
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = FormatTime(timeInMillis),
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = FormatTime(timeInMillis),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.secondary,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Right side - Lap list (unchanged)
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(start = 16.dp)
                    ) {
                        // Current Workout Section
                        Text(
                            text = "Current Workout",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            Column {
                                val workoutName by workoutSessionViewModel.currentWorkout.collectAsState()
                                val count by workoutSessionViewModel.countState.collectAsState()
                                WorkoutRow(
                                    workoutName = workoutName,
                                    count = count,
                                    showCircle = !isCompleted
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Upcoming Workout Section
                        Text(
                            text = "Upcoming Workout",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            val upcomingWorkouts: List<String> by workoutSessionViewModel.upcomingWorkouts.collectAsState()
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(upcomingWorkouts) {  item ->
                                    WorkoutRow(
                                        workoutName = item,
                                        showCircle = false
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Completed Workout Section
                        Text(
                            text = "Completed Workout",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                        ) {
                            val completedWorkouts: List<String> by workoutSessionViewModel.completedWorkouts.collectAsState()
                            LazyColumn(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(completedWorkouts) {  item ->
                                    WorkoutRow(
                                        workoutName = item,
                                        showCircle = false
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Bottom buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val count = workoutSessionViewModel.countState.collectAsState()
                Button(
                    modifier = Modifier
                        .width(300.dp),
                    onClick = {
                        workoutSessionViewModel.workoutSetCompleted()
                    },
                    enabled = !isCompleted,
                ) {
                    Text(text = if (count.value <= 2)"+1 Set" else "Complete", fontSize = 16.sp)
                }

                Button(
                    modifier = Modifier
                        .width(300.dp),
                    onClick = {
                        if(isRunning) {
                            workoutSessionViewModel.pauseWorkout()
                        }else{
                            workoutSessionViewModel.resumeWorkout()
                        }
                    }
                ) {
                    Text(
                        text = when {
                            isRunning -> "Pause"
                            else -> "Resume"
                        },
                        fontSize = 14.sp
                    )
                }

                Button(
                    modifier = Modifier
                        .width(300.dp),
                    onClick = {
                        scope.launch {
                            workoutSessionViewModel.skipWorkout()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    enabled = !isCompleted
                ) {
                    Text("Skip", fontSize = 14.sp)
                }
            }
        }
    }
}