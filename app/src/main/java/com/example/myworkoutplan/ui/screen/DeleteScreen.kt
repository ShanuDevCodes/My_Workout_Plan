package com.example.myworkoutplan.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myworkoutplan.ui.components.DeleteCards

@Composable
fun DeleteScreen(workoutList: List<Pair<String, Int>>, trashCan: Boolean) {

    Box {
        Column {
            LazyColumn {
                item {
                    Text(
                        text = "Delete Workout",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(workoutList) { (item, icon) ->
                    DeleteCards(item, icon, trashCan)
                }
            }
        }
    }
}