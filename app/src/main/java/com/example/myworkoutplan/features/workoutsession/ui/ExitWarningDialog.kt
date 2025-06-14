package com.example.myworkoutplan.features.workoutsession.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ExitWarningDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit

){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Exit $title") },
        text = { Text(text = "Are you sure you want to exit the $title. Any unsaved data will be lost.") },
        confirmButton = {
            FilledTonalButton (
                onClick = onConfirm,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Color(0xFFB32727),
                    contentColor = Color.White
                )
            ) {
                Text("Exit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}