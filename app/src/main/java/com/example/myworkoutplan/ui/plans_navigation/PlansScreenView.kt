package com.example.myworkoutplan.ui.plans_navigation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.example.myworkoutplan.ui.screen.DayScreen
import kotlinx.coroutines.delay

@Composable
fun PlansScreenView(dayTitle: String, workoutList: List<Pair<String, Int>>) {
    val fabScale = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(300)
        fabScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 100,
                easing = FastOutSlowInEasing
            )
        )
    }
    Box(modifier = Modifier
        .fillMaxSize()){
        DayScreen(
            dayTitle = dayTitle,
            workoutList = workoutList,
        )
        FloatingActionButton(
            onClick = {
                // TODO: handle add/edit workout action
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
                .scale(fabScale.value)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit"
            )
        }
    }
}
