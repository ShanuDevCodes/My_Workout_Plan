package com.example.myworkoutplan.ui.screen.WorkoutActivityScreens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun LandscapeWorkoutScreen() {
    val context = LocalContext.current
    var isRunning by remember { mutableStateOf(false) }
    var timeInMillis by remember { mutableStateOf(0L) }
    var lapTimes by remember { mutableStateOf(listOf<Long>()) }
    var lapCounter by remember { mutableStateOf(55) } // Starting from lap 55

    // Timer logic
    LaunchedEffect(isRunning) {
        while (isRunning) {
            delay(10)
            timeInMillis += 10
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {

                // Top-start Back Button
                IconButton(
                    onClick = { (context as? Activity)?.finish() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Main Row: Timer and Lap list
                Row(modifier = Modifier.fillMaxSize()) {

                    // Left side - Timer display
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = formatTime(timeInMillis),
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = formatTime(timeInMillis),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.secondary,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    // Right side - Lap list (unchanged)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(start = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Lap", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                Text("Lap Time", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                Text("Total", fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)

                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                reverseLayout = true
                            ) {
                                if (isRunning && lapTimes.isNotEmpty()) {
                                    item {
                                        val currentLapTime = timeInMillis - lapTimes.last()
                                        LapTimeRow(
                                            lapNumber = lapCounter,
                                            lapTime = currentLapTime,
                                            totalTime = timeInMillis,
                                            isCurrentLap = true
                                        )
                                    }
                                }

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
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Bottom buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    modifier = Modifier
                        .width(300.dp),
                    onClick = {
                        if (isRunning) {
                            lapTimes = lapTimes + timeInMillis
                            lapCounter++
                        }
                    },
                    enabled = isRunning,
                ) {
                    Text("Lap", fontSize = 16.sp)
                }

                Button(
                    modifier = Modifier
                        .width(300.dp),
                    onClick = { isRunning = !isRunning }
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

                Button(
                    modifier = Modifier
                        .width(300.dp),
                    onClick = {
                        isRunning = false
                        timeInMillis = 0L
                        lapTimes = emptyList()
                        lapCounter = 55
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                    enabled = timeInMillis > 0L
                ) {
                    Text("Reset", fontSize = 14.sp)
                }
            }
        }
    }
}


@Composable
fun LapTimeRow(
    lapNumber: Int,
    lapTime: Long,
    totalTime: Long,
    isCurrentLap: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = lapNumber.toString(),
            color = if (isCurrentLap) Color.Red else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isCurrentLap) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = formatTime(lapTime),
            color = if (isCurrentLap) Color.Red else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isCurrentLap) FontWeight.Bold else FontWeight.Normal,
        )
        Text(
            text = formatTime(totalTime),
            color = if (isCurrentLap) Color.Red else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isCurrentLap) FontWeight.Bold else FontWeight.Normal,
        )
    }
}

fun formatTime(timeInMillis: Long): String {
    val minutes = (timeInMillis / 60000) % 60
    val seconds = (timeInMillis / 1000) % 60
    val centiseconds = (timeInMillis / 10) % 100

    return String.format("%02d:%02d.%02d", minutes, seconds, centiseconds)
}
