package com.example.myworkoutplan.ui.plans_navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.R
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutDatabase
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutViewModel
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutViewModelFactory
import com.example.myworkoutplan.ui.screen.DayScreen
import kotlinx.coroutines.delay

@Composable
fun PlansScreenView(dayTitle: String, workoutList: List<Pair<String, Int>>) {
    // Animation for scale and vertical offset
    val fabScale = remember { Animatable(0f) }
    val fabScaleMini = remember { Animatable(0f) }
    val fabOffsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(300)
        // Animate both offset and scale in parallel
        fabScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
        )
    }
    LaunchedEffect(Unit) {
        delay(600)
        // Animate both offset and scale in parallel
        fabScaleMini.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 230, easing = FastOutSlowInEasing)
        )
    }
    LaunchedEffect(Unit) {
        delay(600)
        fabOffsetY.animateTo(
            targetValue = -63f,
            animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
        )
    }
    val context = LocalContext.current
    val dao = WorkoutDatabase.getInstance(context).workoutDao()
    val viewModel: WorkoutViewModel = viewModel(
        factory = WorkoutViewModelFactory(dao)
    )
    val exerciseList by viewModel.getExerciseNameAndImagePairsByType(dayTitle)
        .collectAsState(initial = emptyList())
    Box(modifier = Modifier.fillMaxSize()) {
        DayScreen(
            dayTitle = dayTitle,
            workoutList = exerciseList,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(end = 8.dp, bottom = 8.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(contentAlignment = Alignment.BottomCenter) {
                FloatingActionButton(
                    onClick = { /* Secondary FAB action */ },
                    modifier = Modifier
                        .size(40.dp)
                        .offset(y = fabOffsetY.value.dp)
                        .scale(fabScaleMini.value),
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                    elevation = FloatingActionButtonDefaults.elevation(2.dp)
                ) {
                Icon(
                    painter = painterResource(id = R.drawable.ai),
                    contentDescription = "AI",
                    modifier = Modifier.size(24.dp)
                )
            }
                // Main FAB (Add)
                FloatingActionButton(
                    onClick = { /* Main FAB action */ },
                    modifier = Modifier
                        .size(56.dp)
                        .scale(fabScale.value),
                    containerColor = MaterialTheme.colorScheme.primary,
                    elevation = FloatingActionButtonDefaults.elevation(2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                }
            }
        }
    }
}