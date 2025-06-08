package com.codewithdipesh.habitized.data.services

import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.currentRecomposeScope
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import com.codewithdipesh.habitized.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.timer

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
        val title = intent?.getStringExtra("habit") ?: "Habit Timer"
        startForegroundService(durationSeconds = totalSeconds,title = title)
        return START_STICKY
    }

    private fun startForegroundService(durationSeconds :Int,title : String){

        startTime = System.currentTimeMillis()
        totalDurationMs = durationSeconds * 1000L

        startForeground(1,createNotification("Starting...",title,durationSeconds,0))

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        timerJob?.cancel()
        timerJob = scope.launch {
            while(true){
                val currentTime = System.currentTimeMillis()
                val elapsedMs = currentTime - startTime
                val remainingMs = totalDurationMs - elapsedMs

                if(remainingMs <= 0){
                    //timer finished
                    timerCallback?.onTimerFinished()

                    val notification = createNotification("Timer Finished! ⏰",title,durationSeconds,null)
                    notificationManager.notify(1,notification)

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
                    (elapsedMs/1000).toInt()
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

    private fun createNotification(content : String,title : String,maxProgress:Int,currentProgress:Int?) : Notification {
        val notification =  NotificationCompat.Builder(this,"TIMER_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
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


}