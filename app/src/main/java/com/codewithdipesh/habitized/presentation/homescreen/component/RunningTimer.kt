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
import androidx.compose.runtime.collectAsState
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
import com.codewithdipesh.habitized.data.services.TimerServiceManager
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.presentation.homescreen.HomeScreenOption
import com.codewithdipesh.habitized.ui.theme.ndot
import com.codewithdipesh.habitized.ui.theme.regular
import kotlinx.coroutines.delay

@Composable
fun RunningTimer(
    modifier: Modifier = Modifier,
    habitWithProgress: HabitWithProgress,
    onClick : (HabitWithProgress) -> Unit,
    onTimerFinished : ()->Unit
) {

    val context = LocalContext.current
    val manager = remember { TimerServiceManager(context) }
    val timerState by manager.timerState.collectAsState()

    LaunchedEffect(Unit) {
        manager.bindService()
    }

    LaunchedEffect(timerState) {
        Log.d("TimerState",timerState.toString())
        if (timerState.isFinished) {
            onTimerFinished()
        }
    }

    LaunchedEffect(Unit) {
        delay(1500)
        if(timerState.hour == 0 && timerState.minute == 0 && timerState.second == 0){
            onTimerFinished()
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
        .wrapContentHeight()
        .background(MaterialTheme.colorScheme.background)
        .clickable{
            onClick(habitWithProgress)
        }
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                text = "${timerState.hour}:${timerState.minute}:${timerState.second}",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 32.sp,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}