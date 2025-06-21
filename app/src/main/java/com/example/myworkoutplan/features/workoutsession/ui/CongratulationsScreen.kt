package com.example.myworkoutplan.features.workoutsession.ui

import android.content.res.Configuration
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myworkoutplan.R
import com.example.myworkoutplan.features.workoutsession.viewmodel.WorkoutSessionViewModel
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CongratulationsScreen(
    onDismiss: () -> Unit,
    workoutSessionViewModel: WorkoutSessionViewModel
) {
    val lazyListState = rememberLazyListState()
    var isScrollingDown by remember { mutableStateOf(false) }
    val timeInMillis = workoutSessionViewModel.timeInMillisState.collectAsState()
    val today: LocalDate = LocalDate.now()
    val workoutSplit = workoutSessionViewModel.workoutSplit.collectAsState()
    val workoutDay = workoutSessionViewModel.workoutDay.collectAsState()
    val workoutLog = workoutSessionViewModel.workoutLog.collectAsState()
    val configuration = LocalConfiguration.current
    val isPortrait = configuration.orientation == Configuration.ORIENTATION_PORTRAIT
    val showExtended by remember {
        derivedStateOf {
            // Always expanded at top, otherwise based on scroll direction
            lazyListState.firstVisibleItemIndex == 0 || !isScrollingDown
        }
    }


    LaunchedEffect(lazyListState) {
        var previousIndex = 0
        var previousScrollOffset = 0

        snapshotFlow {
            lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
        }.collect { (currentIndex, currentScrollOffset) ->

            isScrollingDown = if (currentIndex != previousIndex) {
                currentIndex > previousIndex
            } else {
                currentScrollOffset > previousScrollOffset
            }

            previousIndex = currentIndex
            previousScrollOffset = currentScrollOffset
        }
    }
    BackHandler {
        onDismiss()
    }
    Surface {
        Scaffold(
            contentWindowInsets = WindowInsets.systemBars,
            bottomBar = {
                if (isPortrait) {
                    BottomAppBar {
                        Button(
                            onClick = {
                                onDismiss()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 8.dp, end = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back To Home",
                                modifier = Modifier.size(24.dp) // Increased from 18dp
                            )
                            Spacer(modifier = Modifier.width(12.dp)) // Increased spacing proportionally
                            Text(
                                text = "Back To Home",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.padding(innerPadding)
            ){
                if (!isPortrait) {
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Workout Completed",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Well Done!",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Row {
                            if (!isPortrait) {
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxWidth(),
                                ) {
                                    Column {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Targeted Muscle Group:",
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.secondary,
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Box(
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.muscle_full_front), // your PNG resource
                                                        contentDescription = "full muscle front",
                                                        contentScale = ContentScale.Fit, // or Crop for full fill
                                                        modifier = Modifier.fillMaxSize(),
                                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                                    )
                                                }
                                                Box(
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.muscle_full_back), // your PNG resource
                                                        contentDescription = "full muscle front",
                                                        contentScale = ContentScale.Fit, // or Crop for full fill
                                                        modifier = Modifier.fillMaxSize(),
                                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    state = lazyListState
                                ) {
                                    if (isPortrait) {
                                        item {
                                            Text(
                                                text = "Workout Completed",
                                                style = MaterialTheme.typography.headlineLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = MaterialTheme.colorScheme.primary,
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "Well Done!",
                                                style = MaterialTheme.typography.titleLarge,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                            Spacer(modifier = Modifier.height(16.dp))
                                            HorizontalDivider(
                                                thickness = 1.dp,
                                                color = MaterialTheme.colorScheme.outlineVariant.copy(
                                                    alpha = 0.4f
                                                ),
                                                modifier = Modifier.padding(bottom = 16.dp)
                                            )
                                        }
                                    }
                                    item {
                                        Text(
                                            text = "Workout Session Details:",
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.secondary,
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(16.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier.padding(16.dp)
                                            ) {
                                                WorkoutDetailItems(
                                                    string1 = "Total Workout Duration:",
                                                    string2 = FormatTime(timeInMillis.value)
                                                )
                                                Spacer(Modifier.height(8.dp))
                                                WorkoutDetailItems(
                                                    string1 = "Today's Date:",
                                                    string2 = today.toString()
                                                )
                                                Spacer(Modifier.height(8.dp))
                                                WorkoutDetailItems(
                                                    string1 = "Today's Workout Split:",
                                                    string2 = workoutSplit.value
                                                )
                                                Spacer(Modifier.height(8.dp))
                                                WorkoutDetailItems(
                                                    string1 = "Today's Workout Day:",
                                                    string2 = workoutDay.value
                                                )
                                            }
                                        }
                                    }
                                    if (isPortrait) {
                                        item {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "Targeted Muscle Group:",
                                                style = MaterialTheme.typography.headlineSmall.copy(
                                                    fontWeight = FontWeight.Bold
                                                ),
                                                color = MaterialTheme.colorScheme.secondary,
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                        item {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Center
                                            ) {
                                                Box(modifier = Modifier.weight(1f)) {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.muscle_full_front), // your PNG resource
                                                        contentDescription = "full muscle front",
                                                        contentScale = ContentScale.Fit, // or Crop for full fill
                                                        modifier = Modifier.fillMaxSize(),
                                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                                    )
                                                }
                                                Box(modifier = Modifier.weight(1f)) {
                                                    Image(
                                                        painter = painterResource(id = R.drawable.muscle_full_back), // your PNG resource
                                                        contentDescription = "full muscle front",
                                                        contentScale = ContentScale.Fit, // or Crop for full fill
                                                        modifier = Modifier.fillMaxSize(),
                                                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    item {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "Completed Workouts:",
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.Bold
                                            ),
                                            color = MaterialTheme.colorScheme.secondary,
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    workoutLog.value.entries.forEach { (plan, setLogs) ->
                                        item {
                                            Text(
                                                text = plan.exerciseName,
                                                style = MaterialTheme.typography.titleMedium,
                                                modifier = Modifier.padding(8.dp),
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                            Card(
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(16.dp),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                                                )
                                            ) {
                                                Column(
                                                    modifier = Modifier.padding(
                                                        start = 16.dp,
                                                        end = 16.dp,
                                                        top = 8.dp,
                                                        bottom = 8.dp
                                                    )
                                                ) {
                                                    setLogs.forEachIndexed { index, it ->
                                                        WorkoutDetailItems(
                                                            string1 = "Set ${index + 1}:",
                                                            string2 = "${it.reps} x ${it.weight} Kg"
                                                        )
                                                    }
                                                }
                                            }
                                            Spacer(Modifier.height(8.dp))
                                        }
                                    }
                                    if (!isPortrait) {
                                        item {
                                            Spacer(modifier = Modifier.height(64.dp))
                                        }
                                    }
                                }
                                if (!isPortrait) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(end = 8.dp, bottom = 8.dp),
                                        contentAlignment = Alignment.BottomEnd
                                    ) {
                                        ExtendedFloatingActionButton(
                                            onClick = {
                                                onDismiss()
                                            },
                                            icon = {
                                                Icon(
                                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                    contentDescription = "Back to home",
                                                    modifier = Modifier.size(34.dp)
                                                )
                                            },
                                            text = {
                                                Text(
                                                    text = "Home",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            },
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            elevation = FloatingActionButtonDefaults.elevation(2.dp),
                                            expanded = showExtended
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutDetailItems(
    string1 : String,
    string2 : String
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = string1,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            text = string2,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), // smaller than headlineMedium
            color = MaterialTheme.colorScheme.primary
        )
    }
}