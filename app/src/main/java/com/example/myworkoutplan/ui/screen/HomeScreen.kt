package com.example.myworkoutplan.ui.screen

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.R
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutDatabase
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutViewModel
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutViewModelFactory
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = viewModel()) {
    val dayOfWeek = LocalDate.now().dayOfWeek
    val title = when (dayOfWeek) {
        DayOfWeek.MONDAY, DayOfWeek.THURSDAY -> "Push Day"
        DayOfWeek.TUESDAY, DayOfWeek.FRIDAY -> "Pull Day"
        DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY -> "Leg Day"
        else -> "Rest Day"
    }
    var isStarted by remember { mutableStateOf(false) }
    var timer by remember { mutableIntStateOf(0) }
    val minutes = timer / 60
    val seconds = timer % 60
    var isPaused by remember { mutableStateOf(false) }
    val fabScaleMini = remember { Animatable(0f) }
    val fabOffsetY = remember { Animatable(0f) }
    LaunchedEffect(isStarted) {
        if (isStarted) {
            fabOffsetY.animateTo(
                targetValue = -63f,
                animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing)
            )
        } else {
            fabOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing)
            )
        }
    }
    LaunchedEffect(isStarted) {
        if (isStarted) {
            fabScaleMini.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 100, easing = FastOutSlowInEasing)
            )
        } else {
            fabScaleMini.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 180, easing = FastOutSlowInEasing)
            )
        }
    }
    LaunchedEffect(isStarted) {
        while (isStarted) {
            if (!isPaused) {
                delay(1000)
                timer += 1
            } else {
                delay(100) // avoid busy-loop when paused
            }
        }
    }
    LaunchedEffect(!isStarted) {
        timer = 0
    }
    Column {
        Text(
            text = if (!isStarted) "Home" else "%02d:%02d".format(minutes, seconds),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )
        if (dayOfWeek != DayOfWeek.SUNDAY) {
            val visible = viewModel.visible
            val fabScale = remember { Animatable(0f) }
            if (visible) {
                LaunchedEffect(Unit) {
                    delay(150)
                    // Animate both offset and scale in parallel
                    fabScale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = 150, easing = FastOutSlowInEasing)
                    )
                }
            }
            LaunchedEffect(Unit) {
                delay(500L)
                viewModel.show()
            }
            val context = LocalContext.current
            val dao = WorkoutDatabase.getInstance(context).workoutDao()
            val viewModel: WorkoutViewModel = viewModel(
                factory = WorkoutViewModelFactory(dao)
            )
            val exerciseList by viewModel.getExerciseNameAndImagePairsByType(title)
                .collectAsState(initial = emptyList())
            Box(modifier = Modifier
                .fillMaxSize()) {
                this@Column.AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
                ) {
                    DayScreen(title, exerciseList)
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 8.dp, bottom = 8.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        ExtendedFloatingActionButton(
                            onClick = {
                                isPaused = !isPaused
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.pause),
                                    contentDescription = "AI",
                                    modifier = Modifier.size(10.dp)
                                )
                            },
                            text = {
                                Text(
                                    "Pause",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            },
                            modifier = Modifier
                                .width(92.dp)
                                .height(44.dp)
                                .scale(fabScaleMini.value)
                                .offset(y = fabOffsetY.value.dp),
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            elevation = FloatingActionButtonDefaults.elevation(2.dp)
                        )
                        ExtendedFloatingActionButton(
                            onClick = {
                                isStarted = !isStarted
                            },
                            icon = {
                                Icon(
                                    imageVector = if (!isStarted) Icons.Filled.PlayArrow else Icons.Filled.Done,
                                    contentDescription = if (!isStarted) "Start" else "Done"
                                )
                            },
                            text = { Text(if (!isStarted) "Start" else "Done") },
                            modifier = Modifier
                                .scale(fabScale.value)
                                .width(107.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            elevation = FloatingActionButtonDefaults.elevation(2.dp)
                        )
                    }
                }
            }
        } else {
            val configuration = LocalConfiguration.current
            val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
            if (isPortrait) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val visible = viewModel.visible

                    LaunchedEffect(Unit) {
                        delay(300L)
                        viewModel.show()
                    }

                    this@Column.AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.rest),
                                    contentDescription = "Rest Day",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .padding(16.dp),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                                )
                            }
                            Text(
                                text = "It's Rest Day",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.padding(top = 24.dp)
                            )
                            Text(
                                text = "Take time to recover and recharge!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }else{
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val visible = viewModel.visible

                    LaunchedEffect(Unit) {
                        delay(300L)
                        viewModel.show()
                    }

                    this@Column.AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.rest),
                                    contentDescription = "Rest Day",
                                    modifier = Modifier
                                        .size(120.dp)
                                        .padding(16.dp),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                                )
                            }
                            Spacer(modifier = Modifier.width(28.dp))
                            Column {
                                Text(
                                    text = "It's Rest Day",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(top = 24.dp)
                                )
                                Text(
                                    text = "Take time to recover and recharge!",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}