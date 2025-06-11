package com.codewithdipesh.habitized.presentation.homescreen.component

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.data.services.TimerService
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.presentation.homescreen.HomeScreenOption
import com.codewithdipesh.habitized.ui.theme.ndot
import com.codewithdipesh.habitized.ui.theme.regular

@Composable
fun RunningTimer(
    modifier: Modifier = Modifier,
    habitWithProgress: HabitWithProgress,
    onClick : (HabitWithProgress) -> Unit,
    onTimerFinished : ()->Unit
) {

    val context = LocalContext.current
    var timerService: TimerService? = null
    var isBound = false

    var second by remember{ mutableStateOf(0) }
    var minute by remember{ mutableStateOf(0) }
    var hour by remember{ mutableStateOf(0) }


    DisposableEffect(Unit){
        val connection = object : ServiceConnection {
            override fun onServiceConnected(className: ComponentName, service: IBinder) {
                val binder = service as TimerService.TimerBind
                timerService = binder.getService()
                isBound = true

                // Set callback
                timerService?.setTimerCallback(object : TimerService.TimerCallback {
                    override fun onTimerUpdate(h: Int, m: Int, s: Int) {
                        // This runs on the service thread, post to main thread
                        hour = h
                        minute = m
                        second = s
                    }

                    override fun onTimerFinished() {
                        //make sure every element is 0 ( sometimes it doesn't bcz of doze mode)
                        hour = 0
                        minute = 0
                        second = 0
                        onTimerFinished()
                    }
                })
            }

            override fun onServiceDisconnected(arg0: ComponentName) {
                timerService = null
                isBound = false
            }
        }

        Intent(context, TimerService::class.java).also { intent ->
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }

        onDispose {
            if (isBound) {
                context.unbindService(connection)
                isBound = false
            }
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
        .wrapContentHeight()
        .background(MaterialTheme.colorScheme.secondary)
        .clickable{
            onClick(habitWithProgress)
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            //timer in progress
            Text(
                text = "Timer In Progress",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                    fontSize = 12.sp,
                    fontFamily = regular,
                    fontWeight = FontWeight.Light
                )
            )
            Spacer(Modifier.height(2.dp))
            //row 
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
               //title
                Text(
                    text = habitWithProgress.habit.title,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal
                    )
                )
                //timer
                Text(
                    text = "$hour:$minute:$second",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 24.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}