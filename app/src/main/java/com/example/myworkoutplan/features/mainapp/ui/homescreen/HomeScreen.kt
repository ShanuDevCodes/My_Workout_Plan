package com.example.myworkoutplan.features.mainapp.ui.homescreen

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.R
import com.example.myworkoutplan.WorkoutActivity
import com.example.myworkoutplan.core.AppDatabase
import com.example.myworkoutplan.data.local.workout.WorkoutViewModel
import com.example.myworkoutplan.data.local.workout.WorkoutViewModelFactory
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekEvent
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekState
import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekViewModel
import com.example.myworkoutplan.data.remote.firebaseauth.FirebaseViewModel
import com.example.myworkoutplan.features.mainapp.ui.workout.DayScreen
import com.example.myworkoutplan.features.mainapp.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.delay
import java.time.DayOfWeek

@SuppressLint("UseOfNonLambdaOffsetOverload")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(workoutWeekViewModel: WorkoutWeekViewModel, viewModel: HomeScreenViewModel = viewModel()) {
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val firebaseViewModel: FirebaseViewModel = viewModel()
    val userName = firebaseViewModel.currentUser.value?.displayName ?: "Guest"
    val greeting = viewModel.greeting
    val visible = viewModel.visible
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val workoutWeekState by workoutWeekViewModel.state.collectAsState()
    val workoutDao = remember {db.workoutDao()}
    val workoutViewModel: WorkoutViewModel = viewModel(
        factory = WorkoutViewModelFactory(workoutDao)
    )
    val title = workoutWeekState.currentWorkoutDay?.workoutType?:"Rest Day"
    Column {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp, end = 16.dp)
        ) {
            // Main greeting: prominent and friendly
            Text(
                text = "$greeting $userName!",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            if (workoutWeekState.currentWorkoutDay?.workoutType != "Rest Day" ) {
                Spacer(modifier = Modifier.height(2.dp))
                // Subtitle: smaller, lighter, secondary emphasis
                Text(
                    text = "Today's Workout",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            )
        }
        if (workoutWeekState.currentWorkoutDay?.workoutType != "Rest Day") {
            val fabScaleMini = remember { Animatable(0f) }
            val fabOffsetY = remember { Animatable(0f) }
            val fabScale = remember { Animatable(0f) }
            if (visible) {
                LaunchedEffect(Unit) {
                    delay(150)
                    // Animate both offset and scale in parallel
                    fabScale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
                    )
                }
                LaunchedEffect(Unit) {
                    delay(300)
                    // Animate both offset and scale in parallel
                    fabScaleMini.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 230, easing = FastOutSlowInEasing)
                    )
                }
                LaunchedEffect(Unit) {
                    delay(300)
                    fabOffsetY.animateTo(
                        targetValue = -66f,
                        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                    )
                }
            }
            LaunchedEffect(Unit) {
                delay(500L)
                viewModel.show()
            }
            Box(modifier = Modifier
                .fillMaxSize()) {
                DayScreen(visible,title, workoutViewModel)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 8.dp, bottom = 8.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        if (isPortrait) {
                            FloatingActionButton(
                                onClick = { workoutWeekViewModel.onEvent(WorkoutWeekEvent.ShowSwapDialog) },
                                modifier = Modifier
                                    .size(48.dp)
                                    .offset(y = fabOffsetY.value.dp)
                                    .scale(fabScaleMini.value),
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                                elevation = FloatingActionButtonDefaults.elevation(2.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.shuffle),
                                    contentDescription = "Swap",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            ExtendedFloatingActionButton(
                                onClick = {
                                    context.startActivity(
                                        Intent(
                                            context,
                                            WorkoutActivity::class.java
                                        )
                                    )
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.start_button),
                                        contentDescription = "Start",
                                        modifier = Modifier.size(34.dp),
                                        tint = MaterialTheme.colorScheme.onPrimary
                                    )
                                },
                                text = { Text("Start") },
                                modifier = Modifier
                                    .scale(fabScale.value),
                                containerColor = MaterialTheme.colorScheme.primary,
                                elevation = FloatingActionButtonDefaults.elevation(2.dp)
                            )
                        }
                    }
                }
            }
        } else {
            Box( modifier = Modifier
                .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd) {
                if (isPortrait) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        LaunchedEffect(Unit) {
                            delay(300L)
                            viewModel.show()
                        }

                        this@Column.AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn()
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(160.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.rest),
                                        contentDescription = "Rest Day",
                                        modifier = Modifier
                                            .size(120.dp)
                                            .padding(16.dp),
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                                    )
                                }
                                Text(
                                    text = "It's Your Rest Day",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(top = 24.dp)
                                )
                                Text(
                                    text = "Take time to recover and recharge!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LaunchedEffect(Unit) {
                            delay(300L)
                            viewModel.show()
                        }

                        this@Column.AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(160.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.rest),
                                        contentDescription = "Rest Day",
                                        modifier = Modifier
                                            .size(120.dp)
                                            .padding(16.dp),
                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                                    )
                                }
                                Spacer(modifier = Modifier.width(28.dp))
                                Column {
                                    Text(
                                        text = "It's Your Rest Day",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(top = 24.dp)
                                    )
                                    Text(
                                        text = "Take time to recover and recharge!",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                Box(modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)) {
                    val fabScale = remember { Animatable(0f) }
                    if (visible) {
                        LaunchedEffect(Unit) {
                            delay(150)
                            // Animate both offset and scale in parallel
                            fabScale.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(
                                    durationMillis = 150,
                                    easing = FastOutSlowInEasing
                                )
                            )
                        }
                    }
                    ExtendedFloatingActionButton(
                        onClick = {
                            workoutWeekViewModel.onEvent(WorkoutWeekEvent.ShowSwapDialog)
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.shuffle),
                                contentDescription = "Swap",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        text = { Text("Swap") },
                        containerColor = MaterialTheme.colorScheme.primary,
                        elevation = FloatingActionButtonDefaults.elevation(2.dp),
                        modifier = Modifier
                            .scale(fabScale.value),
                    )

                }
            }
        }
    }
    if (workoutWeekState.isSwapping) {
        SwapWorkoutWeekDialog(workoutWeekState, workoutWeekViewModel)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SwapWorkoutWeekDialog(
    state: WorkoutWeekState,
    viewModel: WorkoutWeekViewModel,
){
    val workoutDays = state.availableSwapDays
    var selectedDay by remember { mutableIntStateOf(0) }

    AlertDialog(
        onDismissRequest = { viewModel.onEvent(WorkoutWeekEvent.HideSwapDialog) },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Swap Today's Workout With",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f) // Text takes up all available space
                )
                IconButton(
                    onClick = { /* handle click */ },
                    modifier = Modifier // No weight; aligns to end of Row
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings"
                    )
                }
            }
        },
        text = {
            Column {
                workoutDays.forEach { day ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        TextButton(
                            onClick = { selectedDay = day.dayOfWeek },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = "${DayOfWeek.of(day.dayOfWeek).name.lowercase().replaceFirstChar { it.uppercase() }} - ${day.workoutType}",
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.secondary,
                                textAlign = TextAlign.Start
                            )
                        }
                        RadioButton(
                            selected = selectedDay == day.dayOfWeek,
                            onClick = { selectedDay = day.dayOfWeek },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = MaterialTheme.colorScheme.primary,
                                unselectedColor = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    viewModel.onEvent(WorkoutWeekEvent.SwapWorkoutWeek(selectedDay))
                    viewModel.onEvent(WorkoutWeekEvent.HideSwapDialog)
                },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                enabled = selectedDay != 0
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.onEvent(WorkoutWeekEvent.HideSwapDialog) }) {
                Text("Cancel")
            }
        }
    )
}