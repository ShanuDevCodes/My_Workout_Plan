package com.example.myworkoutplan

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.myworkoutplan.features.workoutsession.model.FormatTimeForNotification
import com.example.myworkoutplan.features.workoutsession.model.WorkoutSessionRepository

class WorkoutForegroundService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
    }

    private val channelId = "workout_channel"
    private val handler = Handler(Looper.getMainLooper())
    private val notificationRunnable = object : Runnable {
        override fun run() {
            val manager = getSystemService(NotificationManager::class.java)
            val notification = createNotification()
            manager.notify(NOTIFICATION_ID, notification)

            handler.postDelayed(this, 1000)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("WorkoutService", "onStartCommand received")
        val startTime = System.currentTimeMillis()

        ensureNotificationChannelExists()
        startForeground(NOTIFICATION_ID, createNotification())
        handler.post(notificationRunnable)

        Log.d("WorkoutService", "startForeground took: ${System.currentTimeMillis() - startTime}ms")
        return START_STICKY
    }

    private fun createNotification(): Notification {
        val resultIntent = Intent(this, WorkoutActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val isRunning = WorkoutSessionRepository.isRunning.value
        val timeText = if (isRunning) {
            val elapsedMillis = System.currentTimeMillis() - WorkoutSessionRepository.startTimeInMillis.value
            "Workout Time: ${FormatTimeForNotification(elapsedMillis)}"
        } else {
            "Your session is running in the background."
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Workout In Progress")
            .setContentText(timeText)
            .setSmallIcon(R.drawable.weights_filled)
            .setOngoing(true)
            .setAutoCancel(false)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .build()
    }


    private fun ensureNotificationChannelExists() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            val existingChannel = manager.getNotificationChannel(channelId)
            if (existingChannel == null) {
                Log.d("WorkoutService", "Creating workout notification channel from service (fallback)")
                val channel = NotificationChannel(
                    channelId,
                    "Workout Session",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "Used for workout tracking session"
                }
                manager.createNotificationChannel(channel)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("WorkoutService", "Service created: ${hashCode()}")
    }

    override fun onDestroy() {
        handler.removeCallbacks(notificationRunnable)
        super.onDestroy()
        Log.d("WorkoutService", "Service destroyed: ${hashCode()}")
    }
}
