package com.example.myworkoutplan.features.mainapp.ui.workoutactivityscreens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortraitWorkoutScreen() {
    var isRunning by remember { mutableStateOf(false) }
    var timeInMillis by remember { mutableLongStateOf(0L) }
    var lapTimes by remember { mutableStateOf(emptyList<Long>()) }
    var lapCounter by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    BackHandler {
        (context as? Activity)?.finish()
    }

    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    // Timer logic
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(10)
            timeInMillis += 10
        }
    }
    Surface {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = 210.dp,
            sheetContent = {
                PeekBottomSheetContent(
                    isRunning = isRunning,
                    timeInMillis = timeInMillis,
                    lapTimes = lapTimes,
                    lapCounter = lapCounter,
                    onStartPause = { isRunning = !isRunning },
                    onLap = {
                        if (isRunning) {
                            lapTimes = lapTimes + timeInMillis
                            lapCounter++
                        }
                    },
                    onReset = {
                        scope.launch {
                            isRunning = false
                            delay(20)
                            timeInMillis = 0L
                            lapTimes = emptyList()
                            lapCounter = 0
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) { padding ->
            // Main content above bottom sheet
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Workout Session",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            (context as? Activity)?.finish()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.secondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
                // Timer display
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = FormatTime(timeInMillis),
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = FormatTime(timeInMillis),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}

@Composable
fun PeekBottomSheetContent(
    isRunning: Boolean,
    timeInMillis: Long,
    lapTimes: List<Long>,
    lapCounter: Int,
    onStartPause: () -> Unit,
    onLap: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Always visible buttons at the top (peek area)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                modifier = Modifier.weight(1f),
                onClick = onLap,
                enabled = isRunning,
            ) {
                Text("Lap", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = onStartPause
            ) {
                Text(
                    text = when {
                        timeInMillis == 0L -> "Start"
                        isRunning -> "Pause"
                        else -> "Resume"
                    },
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                modifier = Modifier.weight(1f),
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                ),
                enabled = timeInMillis > 0L
            ) {
                Text("Reset", fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text( text = "Current Workout")

        Spacer(modifier = Modifier.height(8.dp))
        // Lap times card - visible when sheet is expanded
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column {
                LapTimeRow(
                    lapNumber = 1,
                    lapTime = 200000,
                    totalTime = 400000
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text( text = "Upcoming Workout")

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(lapTimes) { index, lapTime ->
                        val lapNumber = lapCounter - lapTimes.size + index + 1
                        val previousTime = if (index == 0) 0L else lapTimes[index - 1]
                        val currentLapTime = lapTime - previousTime
                        LapTimeRow(
                            lapNumber = lapNumber,
                            lapTime = currentLapTime,
                            totalTime = lapTime
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text( text = "Completed Workout")

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(lapTimes) { index, lapTime ->
                        val lapNumber = lapCounter - lapTimes.size + index + 1
                        val previousTime = if (index == 0) 0L else lapTimes[index - 1]
                        val currentLapTime = lapTime - previousTime
                        LapTimeRow(
                            lapNumber = lapNumber,
                            lapTime = currentLapTime,
                            totalTime = lapTime
                        )
                    }
                }
            }
        }

        // Bottom padding to ensure content doesn't get cut off
        Spacer(modifier = Modifier.height(8.dp))
    }
}
