package com.example.myworkoutplan.features.mainapp.ui.workout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myworkoutplan.features.mainapp.ui.PlanDestination
import com.example.myworkoutplan.features.mainapp.viewmodel.PlansViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlansScreen(navController: NavController, viewModel: PlansViewModel = viewModel()) {
    val plans by viewModel.plans.collectAsState()

    Box(modifier = Modifier) {
        LazyColumn {
            items(plans) { (item, icon) ->
                DayCards(
                    workout = item,
                    icon = icon,
                    onClick = {
                        navController.navigate(PlanDestination.Day(dayTitle = item)) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}