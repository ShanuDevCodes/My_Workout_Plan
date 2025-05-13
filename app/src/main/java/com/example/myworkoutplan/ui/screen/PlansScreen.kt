package com.example.myworkoutplan.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.myworkoutplan.R
import com.example.myworkoutplan.ui.components.DayCards
import com.example.myworkoutplan.ui.navigation.Day

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlansScreen(navController: NavController) {
    val plans = listOf(
        "Push Day" to R.drawable.push_day,
        "Pull Day" to R.drawable.pull_day,
        "Leg Day" to R.drawable.leg_day,
    )
    Box(modifier = Modifier) {
        LazyColumn {
            items(plans) { (item, icon) ->
                DayCards(
                    workout = item,
                    icon = icon,
                    onClick = {
                        navController.navigate(Day(dayTitle = item)
                        )
                    }
                )
            }
        }
    }
}