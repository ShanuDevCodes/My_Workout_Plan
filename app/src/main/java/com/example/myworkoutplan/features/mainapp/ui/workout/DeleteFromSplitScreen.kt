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
fun DeleteFromSplitScreen(splitDayId : Int, workoutViewModel: WorkoutViewModel) {
    val workoutState by workoutViewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        workoutViewModel.onEvent(WorkoutEvent.GetWorkoutBySplitDay(splitDayId))
        workoutViewModel.onEvent(WorkoutEvent.GetSplitDay(splitDayId))
    }
    Box {
        Column {
            LazyColumn {
                item {
                    Text(
                        text = "Delete Workout From Split",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(workoutState.workoutsInSplitDay) { item ->
                    DeleteFromSplitCards(workoutState.splitDay,item,workoutViewModel)
                }
            }
        }
    }
}