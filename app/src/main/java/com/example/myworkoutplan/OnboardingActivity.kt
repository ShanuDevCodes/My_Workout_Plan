package com.example.myworkoutplan

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myworkoutplan.ui.components.legWorkout
import com.example.myworkoutplan.ui.components.pullWorkout
import com.example.myworkoutplan.ui.components.pushWorkout
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutDao
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutDatabase
import com.example.myworkoutplan.ui.components.workoutDB.WorkoutPlan
import com.example.myworkoutplan.ui.data.DataStoreManager
import com.example.myworkoutplan.ui.screen.OnboardingScreen
import com.example.myworkoutplan.ui.settings.SettingsViewModel
import com.example.myworkoutplan.ui.settings.SettingsViewModelFactory
import com.example.myworkoutplan.ui.theme.MyWorkoutPlanTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OnboardingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dataStore = DataStoreManager(applicationContext)
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = SettingsViewModelFactory(dataStore)
            )
            val selectedTheme by remember { derivedStateOf { settingsViewModel.selectedTheme } }
            val dynamicColorOption by remember { derivedStateOf { settingsViewModel.dynamicColorOption } }
            val dao = WorkoutDatabase.getInstance(applicationContext).workoutDao()
            MyWorkoutPlanTheme(
                themeOption = selectedTheme,
                dynamicColorOption = dynamicColorOption
            ){
                OnboardingScreen {

                    lifecycleScope.launch {
                        insertInitialDataIfNeeded(dao, dataStore)
                        startActivity(Intent(this@OnboardingActivity, MainActivity::class.java))
                        finish()
                    }

                }
            }
        }
    }
    private suspend fun insertInitialDataIfNeeded(dao: WorkoutDao, dataStoreManager: DataStoreManager) {
            val isFirstTime = dataStoreManager.isFirstLaunch.first()
            if (isFirstTime) {
                val push = pushWorkout.map { (name, image) ->
                    WorkoutPlan(name, image, "Push Day", R.drawable.push_day)
                }
                val pull = pullWorkout.map { (name, image) ->
                    WorkoutPlan(name, image, "Pull Day", R.drawable.pull_day)
                }
                val leg = legWorkout.map { (name, image) ->
                    WorkoutPlan(name, image, "Leg Day", R.drawable.leg_day)
                }

                (push + pull + leg).forEach { dao.upsertWorkout(it) }

                dataStoreManager.setFirstLaunchDone()
            }
    }
}