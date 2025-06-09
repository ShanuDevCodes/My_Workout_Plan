package com.example.myworkoutplan.features.mainapp.ui.workout

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myworkoutplan.features.mainapp.ui.PlanDestination
import com.example.myworkoutplan.features.mainapp.viewmodel.PlansViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlansScreen(navController: NavController, viewModel: PlansViewModel = viewModel()) {
    val pplPlans by viewModel.pplPlans.collectAsState()
    val fullBodyPlans by viewModel.fullBodyPlans.collectAsState()
    val upperLowerSplit by viewModel.upperLowerSplit.collectAsState()
    val broSplit by viewModel.broSplit.collectAsState()
    val arnoldSplit by viewModel.arnoldSplit.collectAsState()
    Box(modifier = Modifier) {
        LazyColumn {
            item {
                Text(
                    text = "List Of Plans",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)
                )
            }
            item {
                Text(
                    text = "Push, Pull, Legs",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(pplPlans) { (item, icon) ->
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
            item {
                Text(
                    text = "Full Body Split",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(fullBodyPlans) { (item, icon) ->
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
            item {
                Text(
                    text = "Upper, Lower Split",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(upperLowerSplit) { (item, icon) ->
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
            item {
                Text(
                    text = "Bro Split",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(broSplit) { (item, icon) ->
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
            item {
                Text(
                    text = "Arnold Split",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(16.dp)
                )
            }
            items(arnoldSplit) { (item, icon) ->
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