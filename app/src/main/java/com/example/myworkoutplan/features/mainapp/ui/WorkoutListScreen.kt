package com.example.myworkoutplan.features.mainapp.ui

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.core.AppDatabase
import com.example.myworkoutplan.data.local.workout.WorkoutViewModel
import com.example.myworkoutplan.data.local.workout.WorkoutViewModelFactory
import com.example.myworkoutplan.features.mainapp.ui.workout.DayCards
import com.example.myworkoutplan.features.mainapp.ui.workout.DayScreen
import com.example.myworkoutplan.features.mainapp.viewmodel.WorkoutListScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun WorkoutListScreen(workoutListScreenViewModel: WorkoutListScreenViewModel = viewModel()){
    val visible = workoutListScreenViewModel.visible
    val context = LocalContext.current
    val db = remember { AppDatabase.getInstance(context)}
    val dao = remember { db.WorkoutDao() }
    val workoutViewModel: WorkoutViewModel = viewModel(
        factory = WorkoutViewModelFactory(dao)
    )
    LaunchedEffect(Unit) {
        delay(300)
        workoutListScreenViewModel.show()
    }
    Column {
            Text(
                text = "All Workout List",
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


            DayScreen(visible = visible, dayTitle = "All", workoutViewModel = workoutViewModel)

    }
}