package com.example.myworkoutplan.features.workoutsession.ui

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun FormatTime(timeInMillis: Long): String {
    val hours = timeInMillis / 3600000
    val minutes = (timeInMillis / 60000) % 60
    val seconds = (timeInMillis / 1000) % 60
    val centiSeconds = (timeInMillis / 10) % 100

    return if (hours > 0) {
        String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, centiSeconds)
    } else {
        String.format("%02d:%02d.%02d", minutes, seconds, centiSeconds)
    }
}