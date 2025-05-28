package com.example.myworkoutplan.ui.plans_navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myworkoutplan.R
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutEvent
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutState

@Composable
fun AddWorkoutDialog(
    state: WorkoutState,
    onEvent: (WorkoutEvent) -> Unit,
) {
    val categories = listOf("Push Day", "Pull Day", "Leg Day")
    val categoryImages = mapOf(
        "Push Day" to R.drawable.push_day,
        "Pull Day" to R.drawable.pull_day,
        "Leg Day" to R.drawable.leg_day
    )

    AlertDialog(
        onDismissRequest = {
            onEvent(WorkoutEvent.HideDialog)
        },
        title = {
            Text(
                text = "Add New Workout",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = state.exerciseName,
                    onValueChange = { newName ->
                        onEvent(WorkoutEvent.SetExerciseName(newName))
                    },
                    label = { Text("Workout Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text(
                    text = "Category",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        FilterChip(
                            onClick = {
                                val categoryImage = categoryImages[category] ?: R.drawable.push_day
                                onEvent(WorkoutEvent.SetWorkoutType(category))
                                onEvent(WorkoutEvent.SetWorkoutTypeImage(categoryImage))
                            },
                            label = { Text(category) },
                            selected = state.workoutType == category
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (state.exerciseName.isNotBlank()) {
                        onEvent(WorkoutEvent.SaveWorkout)
                    }
                }
            ) {
                Text("Add Workout")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onEvent(WorkoutEvent.HideDialog)
                }
            ) {
                Text("Cancel")
            }
        }
    )
}