package com.example.myworkoutplan.features.mainapp.ui.workout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myworkoutplan.data.local.workout.SplitDay
import com.example.myworkoutplan.data.local.workout.WorkoutEvent
import com.example.myworkoutplan.data.local.workout.WorkoutPlan
import com.example.myworkoutplan.data.local.workout.WorkoutViewModel

@Composable
fun DeleteFromSplitCards(
    splitDay: SplitDay?,
    workoutPlan: WorkoutPlan,
    workoutViewModel: WorkoutViewModel
) {
    val workoutState by workoutViewModel.state.collectAsState()
    LaunchedEffect(Unit) {
        workoutViewModel.onEvent(WorkoutEvent.GetSplit(splitDay!!.splitId))
    }
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = workoutPlan.imageResource),
                contentDescription = "${workoutPlan.exerciseName} icon",
                modifier = Modifier.size(40.dp),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = workoutPlan.exerciseName,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start, // Changed from Center to Start
                modifier = Modifier.weight(1f) // Takes remaining space
            )
            var showDeleteDialog by remember { mutableStateOf(false) }
            IconButton(
                onClick = {
                    showDeleteDialog = true
                },
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete ${workoutPlan.exerciseName}",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            if (showDeleteDialog) {
                DeleteConfirmationDialog(
                    splitDayName = splitDay!!.splitDayName,
                    split = workoutState.split!!.splitName,
                    workoutName = workoutPlan.exerciseName,
                    onConfirm = {
                        workoutViewModel.onEvent(WorkoutEvent.DeleteWorkoutFromSplitDay(splitDayId = splitDay.id, workoutId = workoutPlan.id))
                        showDeleteDialog = false
                    },
                    onDismiss = { showDeleteDialog = false }
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    splitDayName: String,
    split: String,
    workoutName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
        ,
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFFB32727)
            )
        },
        title = {
            Text(
                text = "Delete Exercise",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = buildAnnotatedString {
                    append("Are you sure you want to delete ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary // Primary color
                        )
                    ) {
                        append(workoutName)
                    }
                    append(" From ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(splitDayName)
                    }
                    append(" of ")
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append(split)
                    }
                    append("?")
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            FilledTonalButton( // More prominent for destructive action
                onClick = onConfirm,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFFB32727), // Material Red 700
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Delete",
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    )
}
