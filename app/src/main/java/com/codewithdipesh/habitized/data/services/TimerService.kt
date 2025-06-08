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

        startForeground(1,createNotification("Starting...",title,durationSeconds,0))
        timerJob?.cancel()
        timerJob = scope.launch {
            for (elapsed in 0..durationSeconds){
                val remainingSeconds = durationSeconds - elapsed
                val hour = remainingSeconds/3600
                val minute = (remainingSeconds % 3600) / 60
                val second = remainingSeconds % 60

                //update notification
                val timerString = String.format("%02d:%02d:%02d",hour,minute,second)
                val notification = createNotification("Time Left : $timerString",title,durationSeconds,elapsed)
                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1,notification)

                //call callback
                timerCallback?.onTimerUpdate(hour,minute,second)

                delay(1000)
            }

            timerCallback?.onTimerFinished()
            stopSelf()
        }
    }

    private fun createNotification(content : String,title : String,maxProgress:Int,currentProgress:Int) : Notification {
        return NotificationCompat.Builder(this,"TIMER_CHANNEL")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setProgress(maxProgress,currentProgress,false)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
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