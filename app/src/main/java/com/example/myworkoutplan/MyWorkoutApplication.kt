package com.example.myworkoutplan

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log

class MyWorkoutApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        createWorkoutNotificationChannel(this)
    }
    fun createWorkoutNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "workout_channel",
                "Workout Session",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Used for workout tracking session"
            }
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
        Log.d("MyWorkoutApplication", "Workout Notification Channel created")
    }
}