package com.example.myworkoutplan.features.workoutsession.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Handler
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
fun PortraitWorkoutScreen(workoutSessionViewModel: WorkoutSessionViewModel, onCompleted:() -> Unit) {
    val timeInMillis by workoutSessionViewModel.timeInMillisState.collectAsState()
    val isResting by workoutSessionViewModel.isRestingState.collectAsState()
    val restTimeInMillis by workoutSessionViewModel.restTimeInMillisState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
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

    Surface {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 210.dp,
            sheetContent = {
                PeekBottomSheetContent(
                    onAddSet = {
                        workoutSessionViewModel.countLimitIncrease()
                    },
                    onCompleteSet = {
                        if (!isResting) {
                            workoutSessionViewModel.workoutSetCompleted()
                        }else{
                            workoutSessionViewModel.skipRest()
                        }
                    },
                    onSkip = {
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

@Composable
fun PeekBottomSheetContent(
    onAddSet: () -> Unit,
    onCompleteSet: () -> Unit,
    onSkip: () -> Unit,
    workoutSessionViewModel: WorkoutSessionViewModel,
    isCompleted: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .heightIn(max = LocalConfiguration.current.screenHeightDp.dp - 16.dp)
            .padding(16.dp)
    ) {
        // Always visible buttons at the top (peek area)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val isResting by workoutSessionViewModel.isRestingState.collectAsState()
            Button(
                modifier = Modifier
                    .weight(1f),
                onClick = onCompleteSet,
                enabled = !isCompleted,
            ) {
                Text(text = when {
                    isResting -> "Skip Rest"
                    else -> "Complete Set"
                    },
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = if (!isResting) 10.sp else 14.sp
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = onSkip,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                enabled = !isCompleted
            ) {
                Text("Skip",)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = onAddSet
            ) {
                Text(
                    text = "Add Set",
                )
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
                val upcomingWorkouts: List<WorkoutPlan> by workoutSessionViewModel.upcomingWorkouts.collectAsState()
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(upcomingWorkouts) {  item ->
                        WorkoutRow(
                            workoutName = item.exerciseName,
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
                val completedWorkouts by workoutSessionViewModel.completedWorkouts.collectAsState()
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(completedWorkouts) {  item ->
                        WorkoutRow(
                            workoutName = item.first.exerciseName,
                            showSetCount = true,
                            setCount = item.second
                        )
                    }
                }
            }
        }

        // Bottom padding to ensure content doesn't get cut off
        Spacer(modifier = Modifier.height(8.dp))
    }
}
