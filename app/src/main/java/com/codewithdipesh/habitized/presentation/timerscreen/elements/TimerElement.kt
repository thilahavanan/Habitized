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
    onStart : (Int)->Unit = {},
    onPause : () ->Unit= {},
    onFinish : () ->Unit = {},
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
                        hour = h
                        minute = m
                        second = s
                        count++
                    }

                    override fun onTimerFinished() {
                        //
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
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = ndot,
                            fontSize = 64.sp
                        )
                    )
                    Text(
                        text = "hours",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
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
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = ndot,
                            fontSize = 64.sp
                        )
                    )
                    Text(
                        text = "minutes",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
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
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = ndot,
                        fontSize = 64.sp
                    )
                )
                Text(
                    text = "seconds",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
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
            progress = count.toFloat(),
            total = secondTimes.toFloat(),
            modifier = Modifier.padding(horizontal = 40.dp)
        )
        Spacer(Modifier.height(16.dp))
        //pause retry
        Box(
            modifier = Modifier
                .size(111.dp,34.dp)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.onPrimary)
                .clickable{
//                    resumed = !resumed
//                    if(!start) start = true
//                    onPause()
                    if(!start){
                        onStart(secondTimes)
                    }
                },
            contentAlignment = Alignment.Center
        ){
            Row(verticalAlignment = Alignment.CenterVertically){
                //icon
                if(start){
                    if(resumed){
                        Icon(
                            painter = painterResource(R.drawable.resumed_icon),
                            contentDescription = "pause",
                            tint = MaterialTheme.colorScheme.inverseOnSurface,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }else{
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "resume",
                            tint = MaterialTheme.colorScheme.inverseOnSurface
                        )
                    }
                }
                //text
                Text(
                    text = if(!start) "start"
                           else {
                               if(resumed) "pause"
                               else "resume"
                           }
                    ,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        fontFamily = regular,
                        fontSize = 16.sp
                    )
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        //retry
        AnimatedVisibility(
            visible = start
        ) {
            Row(
                modifier = Modifier.clickable{
//                second = duration.second
//                minute = duration.minute
//                hour = duration.hour
//                count = 0
                },
                verticalAlignment = Alignment.CenterVertically
            ){
                //icon
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "retry",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                //text
                Text(
                    text = "Retry",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}