package com.example.myworkoutplan.ui.screen

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.R
import com.example.myworkoutplan.ui.components.legWorkout
import com.example.myworkoutplan.ui.components.pullWorkout
import com.example.myworkoutplan.ui.components.pushWorkout
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(viewModel: HomeScreenViewModel = viewModel()) {
    val dayOfWeek = LocalDate.now().dayOfWeek
    val (title, workout) = when (dayOfWeek) {
        DayOfWeek.MONDAY, DayOfWeek.THURSDAY -> "Push Day" to pushWorkout
        DayOfWeek.TUESDAY, DayOfWeek.FRIDAY -> "Pull Day" to pullWorkout
        DayOfWeek.WEDNESDAY, DayOfWeek.SATURDAY -> "Leg Day" to legWorkout
        else -> "Rest Day" to emptyList()
    }
    Column {
        Text(
            text = "Home",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )
        if (dayOfWeek != DayOfWeek.SUNDAY) {
            val visible = viewModel.visible

            LaunchedEffect(Unit) {
                delay(500L)
                viewModel.show()
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
            ) {
                DayScreen(title, workout)
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