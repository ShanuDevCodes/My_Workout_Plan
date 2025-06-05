package com.example.myworkoutplan.features.workoutsession.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WorkoutRow(
    workoutName: String,
    showCircle:Boolean = true,
    count:Int = 0,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = workoutName,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        if (showCircle) {
            Icon(
                if (count>0) {
                    Icons.Filled.Circle
                } else {
                    Icons.Outlined.Circle
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
            Icon(
                if (count>1) {
                    Icons.Filled.Circle
                } else {
                    Icons.Outlined.Circle
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
            Icon(
                if (count>2) {
                    Icons.Filled.Circle
                } else {
                    Icons.Outlined.Circle
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}