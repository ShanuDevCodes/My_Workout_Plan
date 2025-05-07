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
import com.example.myworkoutplan.ui.components.DayCards
import com.example.myworkoutplan.ui.components.PlansCards
import com.example.myworkoutplan.ui.components.pushWorkout

@Composable
fun HomeScreen(){
    Box(modifier = Modifier
    ) {
        Column (modifier = Modifier
        ){
            LazyColumn {
                item{
                    Text(
                        "Push Day",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(pushWorkout) { (item, icon) ->
                    PlansCards(
                        item,
                        icon
                    )
                }
            }
        }
    }
}