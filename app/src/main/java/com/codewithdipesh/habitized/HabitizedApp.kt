package com.codewithdipesh.habitized

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.codewithdipesh.habitized.data.services.TimerService
import com.codewithdipesh.habitized.data.services.TimerServiceManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HabitizedApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //foreground service
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "TIMER_CHANNEL",
                "Timer Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}