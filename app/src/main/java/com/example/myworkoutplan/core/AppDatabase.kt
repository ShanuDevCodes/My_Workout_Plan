//package com.example.myworkoutplan.core
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//import com.example.myworkoutplan.data.local.workout.WorkoutDao
//import com.example.myworkoutplan.data.local.workout.WorkoutDatabase
//import com.example.myworkoutplan.data.local.workout.WorkoutPlan
//import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeek
//import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekDao
//import com.example.myworkoutplan.data.local.workoutweek.WorkoutWeekDatabase
//
//@Database(
//    entities = [WorkoutPlan::class, WorkoutWeek::class],
//    version = 1
//)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun WorkoutWeekDao(): WorkoutWeekDao
//    companion object {
//        @Volatile
//        private var INSTANCE: WorkoutWeekDatabase? = null
//
//        fun getInstance(context: Context): WorkoutWeekDatabase {
//            return INSTANCE ?: synchronized(this) {
//                INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    WorkoutWeekDatabase::class.java,
//                    "workoutWeek.db"
//                ).build().also { INSTANCE = it }
//            }
//        }
//    }
//    abstract fun workoutDao(): WorkoutDao
//    companion object {
//        @Volatile
//        private var INSTANCE: WorkoutDatabase? = null
//
//        fun getInstance(context: Context): WorkoutDatabase {
//            return INSTANCE ?: synchronized(this) {
//                INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    WorkoutDatabase::class.java,
//                    "workout.db"
//                ).build().also { INSTANCE = it }
//            }
//        }
//    }
//}