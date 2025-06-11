package com.example.myworkoutplan.features.mainapp.ui.workout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myworkoutplan.data.local.workout.WorkoutEvent
import com.example.myworkoutplan.data.local.workout.WorkoutViewModel

@Composable
fun AddToSplitScreen(splitDayId : Int, workoutViewModel: WorkoutViewModel) {
    val workoutState by workoutViewModel.state.collectAsState()
    LaunchedEffect(splitDayId) {
        workoutViewModel.onEvent(WorkoutEvent.GetWorkoutBySplitDay(splitDayId))
        workoutViewModel.onEvent(WorkoutEvent.GetSplitDay(splitDayId))
        workoutViewModel.onEvent(WorkoutEvent.GetAllWorkouts)
    }

    LaunchedEffect(workoutState.splitDay?.splitId) {
        workoutState.splitDay?.splitId?.let { splitId ->
            workoutViewModel.onEvent(WorkoutEvent.GetSplit(splitId))
        }
    }

    if (workoutState.splitDay != null && workoutState.split != null) {
        Box {
            Column {
                LazyColumn {
                    item {
                        Text(
                            text = "Add Workout To ${workoutState.splitDay!!.splitDayName} of ${workoutState.split!!.splitName}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    items(workoutState.workouts) { item ->
                        AddToSplitCards(workoutState.splitDay, item, workoutViewModel)
                    }
                }
            }
        }
    }
}