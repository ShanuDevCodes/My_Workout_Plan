package com.example.myworkoutplan.features.workoutsession.ui

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun FormatTime(timeInMillis: Long): String {
    val minutes = (timeInMillis / 60000) % 60
    val seconds = (timeInMillis / 1000) % 60
    val centiseconds = (timeInMillis / 10) % 100

    return String.format("%02d:%02d.%02d", minutes, seconds, centiseconds)
}