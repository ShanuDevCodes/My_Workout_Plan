package com.example.myworkoutplan.features.mainapp.ui.workout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myworkoutplan.data.local.workout.WorkoutViewModel

@Composable
fun DayScreen(visible: Boolean, dayTitle: String, workoutViewModel: WorkoutViewModel) {
    val exerciseList by workoutViewModel.getExerciseNameAndImagePairsByType(dayTitle)
        .collectAsState(initial = emptyList())
    Box {
        Column {
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                LazyColumn {
                    item {
                        Text(
                            dayTitle,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    items(exerciseList) { (item, icon) ->
                        PlansCards(item, icon)
                    }
                }
            }
        }
    }
}