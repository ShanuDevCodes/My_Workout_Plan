package com.example.myworkoutplan.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myworkoutplan.R
import com.example.myworkoutplan.ui.components.DayCards
import com.example.myworkoutplan.ui.components.legWorkout
import com.example.myworkoutplan.ui.components.pullWorkout
import com.example.myworkoutplan.ui.components.pushWorkout
import com.example.myworkoutplan.ui.navigation.AppNavHost
import com.example.myworkoutplan.ui.navigation.Day

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlansScreen() {
    val plans = listOf(
        "Push Day" to R.drawable.push_day,
        "Pull Day" to R.drawable.pull_day,
        "Leg Day" to R.drawable.leg_day,
    )
    Box(modifier = Modifier) {
        LazyColumn {
            items(plans) { (item, icon) ->
                val workoutList = when (item) {
                    "Push Day" -> pushWorkout
                    "Pull Day" -> pullWorkout
                    "Leg Day" -> legWorkout
                    else -> emptyList()
                }
                DayCards(
                    workout = item,
                    icon = icon
                )
            }
        }
    }
}