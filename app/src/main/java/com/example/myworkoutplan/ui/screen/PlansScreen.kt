package com.example.myworkoutplan.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myworkoutplan.R
import com.example.myworkoutplan.ui.components.DayCards

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