package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.data.services.timerService.TimerServiceManager
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.ui.theme.regular

@Composable
fun RunningTimer(
    modifier: Modifier = Modifier,
    habitWithProgress: HabitWithProgress,
    onClick : (HabitWithProgress) -> Unit,
    hour : Int,
    minute : Int,
    second : Int,
    onUpdateTimer : (Int,Int,Int) -> Unit,
    onTimerFinished : ()->Unit
) {

    val context = LocalContext.current
    val manager = remember { TimerServiceManager(context) }
    val timerState by manager.timerState.collectAsState()

    DisposableEffect(Unit) {
        manager.bindService()

        onDispose {
            manager.unbindService()
        }
    }

    LaunchedEffect(timerState) {
        if (timerState.isFinished) {
            onTimerFinished()
        }else{
            onUpdateTimer(timerState.hour,timerState.minute,timerState.second)
        }
    }

    Box(
        modifier = modifier.fillMaxWidth()
        .wrapContentHeight()
        .background(MaterialTheme.colorScheme.background)
        .clickable{
            if(hour == 0 && minute == 0 && second == 0){
                onTimerFinished()
            }else{
                onClick(habitWithProgress)
            }
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
                text = if(hour == 0 && minute == 0 && second == 0) "Click to finish"
                    else "${hour}:${minute}:${second}",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = if(hour == 0 && minute == 0 && second == 0) 16.sp
                               else 32.sp,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}