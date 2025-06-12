package com.codewithdipesh.habitized.presentation.timerscreen.elements

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.data.services.TimerService
import com.codewithdipesh.habitized.ui.theme.ndot
import com.codewithdipesh.habitized.ui.theme.regular
import java.time.LocalTime

@Composable
fun TimerElement(
    duration : LocalTime,
    start : Boolean = false,
    finished : Boolean = false,
    onPrimary : Color,
    inverse : Color,
    onStart : (Int)->Unit = {},
    onPause : () ->Unit= {},
    onFinish : () ->Unit = {},
    onTimerFinished : () -> Unit = {},
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    var timerService: TimerService? = null
    var isBound = false

    var second by remember{ mutableStateOf(duration.second) }
    var minute by remember{ mutableStateOf(duration.minute) }
    var hour by remember{ mutableStateOf(duration.hour) }

    var count by remember { mutableStateOf(0) }

    var secondTimes = (duration.hour * 3600) + (duration.minute *60) + duration.second
    var resumed by remember { mutableStateOf(false) }

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
                        Log.e("TimerElement", "$h,$m,$s")
                        hour = h
                        minute = m
                        second = s
                        count = (secondTimes - (hour * 3600) - (minute * 60) - second)
                        Log.e("TimerElement", "$count")
                    }

                    override fun onTimerFinished() {
                        //make sure every element is 0 ( sometimes it doesnt bcz of doze mode)
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


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        //timer with label
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ){
            if(duration.hour > 0){
                Column(verticalArrangement = Arrangement.Center){
                    Text(
                        text = hour.toString().padStart(2, '0') + " : ",
                        style = TextStyle(
                            color = onPrimary,
                            fontFamily = ndot,
                            fontSize = 64.sp
                        )
                    )
                    Text(
                        text = "hours",
                        style = TextStyle(
                            color = onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    )
                }
            }
            if(duration.minute > 0){
                Column(verticalArrangement = Arrangement.Center){
                    Text(
                        text = minute.toString().padStart(2, '0') + " : ",
                        style = TextStyle(
                            color = onPrimary,
                            fontFamily = ndot,
                            fontSize = 64.sp
                        )
                    )
                    Text(
                        text = "minutes",
                        style = TextStyle(
                            color = onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    )
                }
            }
            Column(verticalArrangement = Arrangement.Center){
                Text(
                    text = second.toString().padStart(2, '0'),
                    style = TextStyle(
                        color = onPrimary,
                        fontFamily = ndot,
                        fontSize = 64.sp
                    )
                )
                Text(
                    text = "seconds",
                    style = TextStyle(
                        color = onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )
                )
            }

        }
        //progress
        Spacer(Modifier.height(24.dp))
        TimerProgressBar(
            progress = count,
            total = secondTimes,
            modifier = Modifier.padding(horizontal = 40.dp)
        )
        Spacer(Modifier.height(16.dp))
        //pause retry
        Box(
            modifier = Modifier
                .size(126.dp,40.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(onPrimary)
                .clickable{
                    if(!start && !finished){
                        onStart(secondTimes)
                    }
                    if(finished){
                        onFinish()
                    }
                },
            contentAlignment = Alignment.Center
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                //icon
                if(start && !finished){
                    if(resumed){
                        Icon(
                            painter = painterResource(R.drawable.resumed_icon),
                            contentDescription = "pause",
                            tint = inverse,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }else{
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "resume",
                            tint = inverse
                        )
                    }
                }
                //text
                Text(
                    text = if(!start && !finished) "start"
                           else {
                               if(resumed) "pause"
                               else if(finished) "Finish"
                               else "resume"
                           }
                    ,
                    style = TextStyle(
                        color = inverse,
                        fontFamily = regular,
                        fontSize = 16.sp
                    )
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        //retry
        AnimatedVisibility(
            visible = start && !finished
        ) {
            Row(
                modifier = Modifier.clickable{
                    onStart(secondTimes)
                },
                verticalAlignment = Alignment.CenterVertically
            ){
                //icon
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "retry",
                    tint = onPrimary
                )
                //text
                Text(
                    text = "Retry",
                    style = TextStyle(
                        color = onPrimary,
                        fontFamily = regular,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}