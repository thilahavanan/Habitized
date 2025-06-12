package com.codewithdipesh.habitized.presentation.timerscreen.durationScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.addscreen.component.AddScreenTopBar
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.Theme.*
import com.codewithdipesh.habitized.presentation.timerscreen.elements.Starter
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
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val state by viewmodel.state.collectAsState()
    var showStarter by remember {
        mutableStateOf(false)
    }
    var totalSeconds = 0

    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val inverseColor = MaterialTheme.colorScheme.inverseOnSurface
    var onPrimary by remember { mutableStateOf(onPrimaryColor) }
    var inverse by remember { mutableStateOf(inverseColor) }

    LaunchedEffect(state.theme) {
        when(state.theme){
            Normal -> {}
            else ->{
               onPrimary = Color.White
               inverse = Color.Black
            }
        }
    }

    BackHandler {
        when(state.timerState){
            TimerState.Finished -> {
               viewmodel.clearUi()
            }
            TimerState.Not_Started -> {
                viewmodel.clearUi()
            }
            else -> {}
        }
        navController.navigateUp()
    }

    LaunchedEffect(Unit) {
        viewmodel.init(habitProgressId)
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            AddScreenTopBar(
                isShowingLeftIcon = true,
                isShowingRightIcon = true,
                leftIcon = {
                    IconButton(
                        onClick = {
                            if(state.timerState == TimerState.Finished || state.timerState == TimerState.Not_Started){
                                viewmodel.clearUi()
                            }
                            navController.navigateUp()
                        },
                        modifier = Modifier
                            .padding(top = 30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                title = title,
                rightIcon = {
                    IconButton(
                        onClick = {
                           viewmodel.openSettings()
                        },
                        modifier = Modifier
                            .padding(top = 30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = "settings",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        //start counter
        if(showStarter){
            Starter {
                showStarter = false
                scope.launch {
                    viewmodel.startTimer(
                        context = context,
                        totalSeconds = totalSeconds
                    )
                }
            }
        }

        //todo settings
        //todo change it to good ui
        var tempTheme by remember {
            mutableStateOf(state.theme)
        }
        if(state.isSettingsOpen){
            AlertDialog(
                onDismissRequest = {
                    viewmodel.closeSettings()
                },
                text = {
                    Column {
                        TextButton(
                            onClick = {
                                tempTheme = Theme.Normal
                            }
                        ) {
                            Text("Normal")
                        }
                        TextButton(
                            onClick = {
                                tempTheme = Theme.Matcha
                            }
                        ) {
                            Text("Matcha")
                        }
                        TextButton(
                            onClick = {
                                tempTheme = Theme.Coffee
                            }
                        ) {
                            Text("Coffee")
                        }
                    }
                },
                confirmButton = {
                    Text(
                        "confirm",
                        modifier = Modifier.clickable{
                            viewmodel.chooseTheme(tempTheme)
                            viewmodel.closeSettings()
                        }
                    )
                }
            )
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .then( //different bg for diff theme
                when(state.theme){
                    Normal -> Modifier
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
                        .paint(
                            painter = painterResource(R.drawable.bg_noise),
                            contentScale = ContentScale.FillBounds
                        )
                    Coffee -> Modifier.paint(
                        painter = painterResource(R.drawable.coffee_theme),
                        contentScale = ContentScale.FillBounds
                    )
                    Matcha -> Modifier.paint(
                        painter = painterResource(R.drawable.matcha_theme),
                        contentScale = ContentScale.FillBounds
                    )
                }
            )
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){

            Box(Modifier.fillMaxSize()){
                //the target
                Box(
                    Modifier.align(Alignment.TopStart)
                ){
                    if(state.timerState != TimerState.Not_Started){
                    Text(
                        text = targetDurationValue.toWord(),
                        style = TextStyle(
                            color = onPrimary,
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
                }
                //timer alignment
                Box(
                    Modifier.align(Alignment.Center)
                ){
                    TimerElement(
                        duration = targetDurationValue,
                        start = state.timerState == TimerState.Resumed,
                        finished = state.timerState == TimerState.Finished,
                        onPrimary = onPrimary,
                        inverse = inverse,
                        onStart = {
                            totalSeconds = it
                            showStarter = true
                        },
                        onTimerFinished = {
                            viewmodel.finishTimer()
                            scope.launch {
                                viewmodel.finishHabit()
                            }
                        },
                        onFinish = {
                           scope.launch{
                               navController.navigateUp()
                               viewmodel.clearUi()
                           }
                        }
                    )
                }
            }


        }
    }
    
}