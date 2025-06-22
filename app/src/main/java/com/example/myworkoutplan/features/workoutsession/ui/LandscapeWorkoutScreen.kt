package com.example.myworkoutplan.features.workoutsession.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Handler
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.myworkoutplan.WorkoutActivity
import com.example.myworkoutplan.WorkoutForegroundService
import com.example.myworkoutplan.data.local.workout.WorkoutPlan
import com.example.myworkoutplan.features.workoutsession.viewmodel.WorkoutSessionViewModel
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeWorkoutScreen(workoutSessionViewModel:WorkoutSessionViewModel, onCompleted:() -> Unit  ) {
    val context = LocalContext.current
    val timeInMillis by workoutSessionViewModel.timeInMillisState.collectAsState()
    val isResting by workoutSessionViewModel.isRestingState.collectAsState()
    val restTimeInMillis by workoutSessionViewModel.restTimeInMillisState.collectAsState()
    val scope = rememberCoroutineScope()
    val isCompleted by workoutSessionViewModel.isCompleted.collectAsState()
    val exitDialogVisible by workoutSessionViewModel.exitDialog.collectAsState()
    val skippedWorkoutDialogVisible by workoutSessionViewModel.skippedWorkoutDialog.collectAsState()

    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            if (workoutSessionViewModel.completedWorkouts.value.isNotEmpty()) {
                onCompleted()
            } else {
                workoutSessionViewModel.showSkippedWorkoutDialog()
            }
        }
    }
    BackHandler {
        workoutSessionViewModel.showExitDialog()
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
                            text = FormatTime(restTimeInMillis),
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
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            Column {
                                val workoutName by workoutSessionViewModel.currentWorkout.collectAsState()
                                val count by workoutSessionViewModel.countState.collectAsState()
                                val countLimit by workoutSessionViewModel.countLimitState.collectAsState()
                                WorkoutRow(
                                    workoutName = workoutName?.exerciseName?:"",
                                    count = count,
                                    showCircle = !isCompleted,
                                    countLimit = countLimit
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
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            val upcomingWorkouts: List<WorkoutPlan> by workoutSessionViewModel.upcomingWorkouts.collectAsState()
                            if (upcomingWorkouts.isEmpty()){
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        text = "No upcoming Workout",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(upcomingWorkouts) { item ->
                                        WorkoutRow(
                                            workoutName = item.exerciseName,
                                            showCircle = false
                                        )
                                    }
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
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                            )
                        ) {
                            val completedWorkouts by workoutSessionViewModel.completedWorkouts.collectAsState()
                            if (completedWorkouts.isEmpty()){
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ){
                                    Text(
                                        text = "No completed workouts yet",
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }else {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(completedWorkouts) { item ->
                                        WorkoutRow(
                                            workoutName = item.first.exerciseName,
                                            showSetCount = true,
                                            setCount = item.second
                                        )
                                    }
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
                Button(
                    modifier = Modifier
                        .width(300.dp),
                    onClick = {
                        if (!isResting) {
                            workoutSessionViewModel.workoutSetCompleted()
                        }else{
                            workoutSessionViewModel.skipRest()
                        }
                    },
                    enabled = !isCompleted,
                ) {
                    Text(text = when {
                        isResting -> "Skip Rest"
                        else -> "Complete Set"
                    },
                        fontSize = 16.sp
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

                Button(
                    modifier = Modifier
                        .width(300.dp),
                    onClick = {
                        workoutSessionViewModel.countLimitIncrease()
                    }
                ) {
                    Text(
                        text = "Add Set",
                        fontSize = 14.sp
                    )
                }
            }
        }
        if (exitDialogVisible){
            ExitWarningDialog(
                title = "Workout Session",
                onDismiss = {
                    workoutSessionViewModel.dismissExitDialog()
                },
                onConfirm = {
                    workoutSessionViewModel.dismissExitDialog()
                    (context as? Activity)?.finish()
                }
            )
        }
        if (skippedWorkoutDialogVisible){
            SkippedWorkoutDialog(
                onRetryWorkout = {
                    workoutSessionViewModel.hideSkippedWorkoutDialog()
                    val activity = context as? Activity
                    val intent = Intent(context, WorkoutForegroundService::class.java)
                    context.stopService(intent)
                    activity?.finish()
                    Handler(android.os.Looper.getMainLooper()).postDelayed({
                        context.startActivity(Intent(context, WorkoutActivity::class.java))
                    }, 600L)
                },
                onEndSession = {
                    workoutSessionViewModel.hideSkippedWorkoutDialog()
                    (context as? Activity)?.finish()
                }
            )
        }
    }
}