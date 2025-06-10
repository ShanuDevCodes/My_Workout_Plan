package com.example.myworkoutplan.features.mainapp.ui.workout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myworkoutplan.core.AppDatabase
import com.example.myworkoutplan.data.local.workout.WorkoutEvent
import com.example.myworkoutplan.data.local.workout.WorkoutViewModel
import com.example.myworkoutplan.data.local.workout.WorkoutViewModelFactory
import com.example.myworkoutplan.features.mainapp.ui.PlanDestination
import com.example.myworkoutplan.features.mainapp.viewmodel.PlansViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlansScreen(navController: NavController, viewModel: PlansViewModel = viewModel()) {
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val dao = remember { db.WorkoutDao() }
    val workoutViewModel: WorkoutViewModel = viewModel(
        factory = WorkoutViewModelFactory(dao)
    )
    val workoutState by workoutViewModel.state.collectAsState()

    // Fetch data once when the screen loads
    LaunchedEffect(Unit) {
        workoutViewModel.onEvent(WorkoutEvent.GetAllWorkoutSplits)
        workoutViewModel.onEvent(WorkoutEvent.GetAllSplitDays)
    }

    val workoutSplits = workoutState.workoutSplits
    val splitDaysBySplitId = workoutState.splitDays.groupBy { it.splitId }
    val visibleSplits by viewModel.visibleSplits.collectAsState()

    LaunchedEffect(workoutSplits) {
        if (workoutSplits.isNotEmpty()) {
            viewModel.animateSplitsOnce(workoutSplits)
        }
    }

    LazyColumn {
        item {
            Text(
                text = "List Of Plans",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
            )
        }
        items(workoutSplits) { split ->
            AnimatedVisibility(
                visible = visibleSplits.contains(split.id),
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Column {
                    Text(
                        text = split.splitName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(16.dp)
                    )
                    val splitDays = splitDaysBySplitId[split.id].orEmpty()
                    splitDays.forEach { splitDay ->
                        DayCards(
                            workout = splitDay.splitDayName,
                            icon = splitDay.splitDayImage,
                            onClick = {
                                navController.navigate(PlanDestination.Day(splitDayId = splitDay.id)) {
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}