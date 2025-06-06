package com.codewithdipesh.habitized.presentation.timerscreen.durationScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.presentation.addscreen.component.AddScreenTopBar
import com.codewithdipesh.habitized.presentation.timerscreen.elements.TimerElement
import com.codewithdipesh.habitized.presentation.util.getColorFromKey
import com.codewithdipesh.habitized.presentation.util.toWord
import com.codewithdipesh.habitized.ui.theme.regular
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.UUID

@Composable
fun DurationScreen(
    modifier: Modifier = Modifier,
    habitProgressId : UUID,
    title : String,
    targetDurationValue : LocalTime,
    colorKey : String,
    viewmodel: DurationViewModel,
    navController: NavController
){
    val state by viewmodel.state.collectAsState()
    val height = LocalConfiguration.current.screenHeightDp

    LaunchedEffect(Unit) {
        viewmodel.init(habitProgressId)
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AddScreenTopBar(
                onBackClick = {navController.navigateUp()},
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "back",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                title = title
            )
        }
    ) { innerPadding ->

        Column(modifier = Modifier
            .fillMaxSize()
            .background(getColorFromKey(colorKey))
            .background(brush = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.inverseOnSurface.copy(0.5f),
                    MaterialTheme.colorScheme.inverseOnSurface.copy(0.5f),
                    MaterialTheme.colorScheme.inverseOnSurface.copy(0.4f),
                    MaterialTheme.colorScheme.inverseOnSurface.copy(0.25f),
                    MaterialTheme.colorScheme.inverseOnSurface.copy(0.1f),
                    Color.Transparent,
                    Color.Transparent,
                    Color.Transparent,
                    Color.Transparent,
                )
            ))
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            if(state.timerState != TimerState.Not_Started){
                Text(
                    text = targetDurationValue.toWord(),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 8.dp),
                    textAlign = TextAlign.Start
                )
            }
            Spacer(Modifier.height((height * 0.3).dp))
            TimerElement(
                duration = targetDurationValue,
                onPause = {
                    if(state.timerState != TimerState.Resumed){
                        viewmodel.resumeTimer()
                    }else{
                        viewmodel.pauseTimer()
                    }
                }
            )
            Spacer(Modifier.weight(0.3f))
        }
    }
    
}