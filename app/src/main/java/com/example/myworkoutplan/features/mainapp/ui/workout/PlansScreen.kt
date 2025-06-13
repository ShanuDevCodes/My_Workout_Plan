package com.example.myworkoutplan.features.mainapp.ui.workout

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myworkoutplan.core.AppDatabase
import com.example.myworkoutplan.data.local.workout.SplitDay
import com.example.myworkoutplan.data.local.workout.WorkoutEvent
import com.example.myworkoutplan.data.local.workout.WorkoutSplit
import com.example.myworkoutplan.data.local.workout.WorkoutViewModel
import com.example.myworkoutplan.data.local.workout.WorkoutViewModelFactory
import com.example.myworkoutplan.features.mainapp.ui.PlanDestination
import com.example.myworkoutplan.features.mainapp.viewmodel.PlansViewModel
import com.google.firebase.BuildConfig

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PlansScreen(navController: NavController, viewModel: PlansViewModel = viewModel()) {
    val recomposeCount = remember { mutableIntStateOf(0) }
    recomposeCount.intValue++

    if (BuildConfig.DEBUG) {
        Log.d("Performance", "Screen recomposed ${recomposeCount.intValue} times")
    }
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context) }
    val dao = remember { db.WorkoutDao() }
    val workoutViewModel: WorkoutViewModel = viewModel(
        factory = WorkoutViewModelFactory(dao)
    )
    val workoutState by workoutViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        workoutViewModel.onEvent(WorkoutEvent.GetAllWorkoutSplits)
        workoutViewModel.onEvent(WorkoutEvent.GetAllSplitDays)
    }

    val workoutSplits = workoutState.workoutSplits
    val splitDaysBySplitId = remember(workoutState.splitDays) {
        workoutState.splitDays.groupBy { it.splitId }
    }

    // Now using the correct state
    val splitVisibilityMap by viewModel.splitVisibilityMap.collectAsState()

    LaunchedEffect(workoutSplits.size) {
        if (workoutSplits.isNotEmpty()) {
            viewModel.animateSplitsOnce(workoutSplits)
        }
    }

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

        items(
            items = workoutSplits,
            key = { split -> split.id }
        ) { split ->
            // Direct access to visibility state - no derivedStateOf needed
            val isVisible = splitVisibilityMap[split.id] ?: false

            AnimatedVisibility(
                visible = isVisible,
                enter = fadeIn(animationSpec = tween(300)) +
                        slideInVertically(
                            initialOffsetY = { it / 3 },
                            animationSpec = tween(400)
                        )
            ) {
                WorkoutSplitItem(
                    split = split,
                    splitDays = splitDaysBySplitId[split.id].orEmpty(),
                    navController = navController
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WorkoutSplitItem(
    split: WorkoutSplit,
    splitDays: List<SplitDay>,
    navController: NavController
) {
    Column {
        Text(
            text = split.splitName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(16.dp)
        )

        // Optimize image loading for filtered days
        val activeSplitDays = remember(splitDays) {
            splitDays.filter { it.splitDayName != "Rest Day" }
        }

        activeSplitDays.forEach { splitDay ->
            key(splitDay.id) {
                DayCards(
                    workout = splitDay.splitDayName,
                    icon = splitDay.splitDayImage,
                    onClick = {
                        navController.navigate(PlanDestination.Day(splitDayId = splitDay.id)) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
