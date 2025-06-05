package com.example.myworkoutplan.features.workoutsession.ui

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
fun PortraitWorkoutScreen(workoutSessionViewModel: WorkoutSessionViewModel) {
    val isRunning by workoutSessionViewModel.isRunningState.collectAsState()
    val timeInMillis by workoutSessionViewModel.timeInMillisState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val isCompleted by workoutSessionViewModel.isCompleted.collectAsState()

    Surface {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 210.dp,
            sheetContent = {
                PeekBottomSheetContent(
                    isRunning = isRunning,
                    timeInMillis = timeInMillis,
                    onStartPause = {
                        if(isRunning) {
                            workoutSessionViewModel.pauseWorkout()
                        }else{
                            workoutSessionViewModel.resumeWorkout()
                        }
                    },
                    onLap = {
                        workoutSessionViewModel.workoutSetCompleted()
                    },
                    onReset = {
                        scope.launch {
                            workoutSessionViewModel.skipWorkout()
                        }
                    },
                    workoutSessionViewModel = workoutSessionViewModel,
                    isCompleted
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) { padding ->
            // Main content above bottom sheet
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
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
                // Timer display
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
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
            }
        }
    }
}

@Composable
fun PeekBottomSheetContent(
    isRunning: Boolean,
    timeInMillis: Long,
    onStartPause: () -> Unit,
    onLap: () -> Unit,
    onReset: () -> Unit,
    workoutSessionViewModel: WorkoutSessionViewModel,
    isCompleted: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Always visible buttons at the top (peek area)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val count = workoutSessionViewModel.countState.collectAsState()
            Button(
                modifier = Modifier.weight(1f),
                onClick = onLap,
                enabled = !isCompleted,
            ) {
                Text(text = if (count.value <= 2)"+1 Set" else "Complete", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = onStartPause
            ) {
                Text(
                    text = when {
                        isRunning -> "Pause"
                        else -> "Resume"
                    },
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                enabled = !isCompleted
            ) {
                Text("Skip", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Current Workout",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))
        // Lap times card - visible when sheet is expanded
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column {
                val workoutName by workoutSessionViewModel.currentWorkout.collectAsState()
                val count by workoutSessionViewModel.countState.collectAsState()
                WorkoutRow(
                    workoutName = workoutName,
                    count = count
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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
            Column {
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
        }

        Spacer(modifier = Modifier.height(12.dp))

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
            Column {
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
        }

        // Bottom padding to ensure content doesn't get cut off
        Spacer(modifier = Modifier.height(8.dp))
    }
}
