package com.example.myworkoutplan.features.mainapp.ui.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.myworkoutplan.R
import com.example.myworkoutplan.data.local.workout.WorkoutEvent
import com.example.myworkoutplan.data.local.workout.WorkoutState

@Composable
fun AddWorkoutDialog(
    state: WorkoutState,
    workoutCategory: String,
    onEvent: (WorkoutEvent) -> Unit
) {
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
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),// Center align content
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

                if(state.nameAlreadyExists) {
                    Text(
                        text = "Workout Name Already Exists",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Start
                    )
                }
                // Show selected category
                Text(
                    text = "Category: $workoutCategory",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Start
                )
            }
        },
        confirmButton = {
            FilledTonalButton(
                onClick = {
                    onEvent(WorkoutEvent.SetWorkoutType(workoutCategory))
                    onEvent(WorkoutEvent.SetWorkoutTypeImage(categoryImages[workoutCategory] ?: R.drawable.push_day))
                    if (state.exerciseName.isNotBlank()) {
                        onEvent(WorkoutEvent.SaveWorkout)
                    }
                },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                enabled = state.exerciseName.isNotBlank() // Disable if empty
            ) {
                Text(
                    text = "Add Workout",
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onEvent(WorkoutEvent.HideDialog)
                }
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
