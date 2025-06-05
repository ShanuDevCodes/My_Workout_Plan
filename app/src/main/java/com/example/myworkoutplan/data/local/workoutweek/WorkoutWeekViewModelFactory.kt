package com.example.myworkoutplan.data.local.workoutweek

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myworkoutplan.core.DataStoreManager

@RequiresApi(Build.VERSION_CODES.O)
class WorkoutWeekViewModelFactory(
    private val dataStoreManager: DataStoreManager,
    private val dao: WorkoutWeekDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutWeekViewModel::class.java)) {
            return WorkoutWeekViewModel(dataStoreManager,dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}