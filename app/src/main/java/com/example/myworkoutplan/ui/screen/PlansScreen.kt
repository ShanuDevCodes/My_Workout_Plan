package com.example.myworkoutplan.ui.screen

import androidx.compose.foundation.layout.Box
import com.example.myworkoutplan.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myworkoutplan.ui.components.DayCards
import com.example.myworkoutplan.ui.components.PlansCards
import com.example.myworkoutplan.ui.components.legWorkout
import com.example.myworkoutplan.ui.components.pullWorkout
import com.example.myworkoutplan.ui.components.pushWorkout
import kotlin.collections.set

@Composable
fun PlansScreen(){
    val plans = listOf(
        "Push Day" to R.drawable.push_day,
        "Pull Day" to R.drawable.pull_day,
        "Leg Day" to R.drawable.leg_day,
    )
    Box(modifier = Modifier) {
        LazyColumn {
            items(plans) { (item , icon) ->
                DayCards(
                    item,
                    icon
                )
            }
        }
    }
}