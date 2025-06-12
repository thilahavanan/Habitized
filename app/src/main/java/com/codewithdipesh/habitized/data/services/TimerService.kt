package com.codewithdipesh.habitized.data.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.res.painterResource
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.codewithdipesh.habitized.MainActivity
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import androidx.core.graphics.createBitmap

class TimerService : Service() {

    private var timerJob : Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    private val binder = TimerBind()
    private var timerCallback : TimerCallback? =  null

    // Store the actual start time instead of relying on delays
    private var startTime: Long = 0
    private var totalDurationMs: Long = 0
    private var isPaused: Boolean = false
    private var pausedTimeMs: Long = 0

    interface TimerCallback {
        fun onTimerUpdate(h :Int,m:Int,s:Int)
        fun onTimerFinished()
    }
    inner class  TimerBind : Binder(){
        fun getService(): TimerService = this@TimerService
    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val totalSeconds = intent?.getIntExtra("duration_seconds", 0) ?: 0
        val id = intent?.getStringExtra("id") ?: ""
        val title = intent?.getStringExtra("habit") ?: "Habit Timer"
        val color = intent?.getStringExtra("color") ?: "yellow"
        startForegroundService(durationSeconds = totalSeconds,title = title,id = id, color = color)
        return START_STICKY
    }

    private fun startForegroundService(id:String,title:String,durationSeconds:Int,color:String){

        startTime = System.currentTimeMillis()
        totalDurationMs = durationSeconds * 1000L

        //intent for deeplinking
        val intent = Intent(
            Intent.ACTION_VIEW,
            "com.codewithdipesh.habitized://duration/$id/$title/$durationSeconds/$color".toUri()
        )
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        startForeground(1,createNotification("Starting...",title,durationSeconds,0,pendingIntent))

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        timerJob?.cancel()
        timerJob = scope.launch {
            while(true){
                val currentTime = System.currentTimeMillis()
                val elapsedMs = currentTime - startTime
                val remainingMs = totalDurationMs - elapsedMs

                if(remainingMs <= 0){
                    //timer finished
                    notificationManager.notify(1,showAlarmNotification())
                    timerCallback?.onTimerFinished()
                    stopSelf()
                    break
                }

                val remainingSeconds = (remainingMs / 1000).toInt()
                val hour = remainingSeconds/3600
                val minute = (remainingSeconds % 3600) / 60
                val second = remainingSeconds % 60


                //update notification
                val timerString = String.format("%02d:%02d:%02d",hour,minute,second)
                val notification = createNotification(
                    "Time Left : $timerString",
                    title,
                    durationSeconds,
                    (elapsedMs/1000).toInt(),
                    pendingIntent
                )
                //call callback
                timerCallback?.onTimerUpdate(hour,minute,second)
                notificationManager.notify(1,notification)

                // Use more precise delay calculation
                val nextUpdateTime = startTime  + ((elapsedMs / 1000 + 1) * 1000)
                val delayMs = nextUpdateTime - System.currentTimeMillis()

                if (delayMs > 0) {
                    delay(delayMs)
                } else {
                    delay(100) // Minimum delay to prevent tight loop
                }

            }
        }
    }

    private fun createNotification(content : String,title : String,maxProgress:Int,currentProgress:Int?,pendingIntent : PendingIntent) : Notification {
        val notification =  NotificationCompat.Builder(this,"TIMER_CHANNEL")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.ic_stat_name))
            .setContentTitle(title)
            .setContentText(content)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_STOPWATCH)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        if(currentProgress != null){
            notification.setProgress(maxProgress,currentProgress,false)
        }

        return notification.build()
    }
    override fun onDestroy() {
         timerJob?.cancel()
        timerCallback = null
        super.onDestroy()
    }

    fun setTimerCallback(callback: TimerCallback?) {
        this.timerCallback = callback
        Log.d("TimerService", "Timer callback set: ${callback != null}")
    }

    private fun showAlarmNotification() : Notification{

        val intent = Intent(
            applicationContext,
            MainActivity::class.java
        )
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, "TIMER_CHANNEL")
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("⏰ Time's Up!")
            .setContentText("Your timer finished.")
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(false)
            .setContentIntent(pendingIntent)
            .build()

    }



}