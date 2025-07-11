package com.codewithdipesh.habitized.presentation.timerscreen.sessionScreen

import android.util.Log
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.data.services.timerService.TimerServiceManager
import com.codewithdipesh.habitized.presentation.addscreen.component.AddScreenTopBar
import com.codewithdipesh.habitized.presentation.homescreen.component.SubTaskEditor
import com.codewithdipesh.habitized.presentation.timerscreen.Theme.Black
import com.codewithdipesh.habitized.presentation.timerscreen.Theme.Coffee
import com.codewithdipesh.habitized.presentation.timerscreen.Theme.Matcha
import com.codewithdipesh.habitized.presentation.timerscreen.Theme.Normal
import com.codewithdipesh.habitized.presentation.timerscreen.TimerState
import com.codewithdipesh.habitized.presentation.timerscreen.elements.MarkAsCompleteAlertDialog
import com.codewithdipesh.habitized.presentation.timerscreen.elements.Starter
import com.codewithdipesh.habitized.presentation.timerscreen.elements.ThemeChooser
import com.codewithdipesh.habitized.presentation.timerscreen.elements.TimerElement
import com.codewithdipesh.habitized.presentation.util.getThemedColorFromKey
import com.codewithdipesh.habitized.presentation.util.toWord
import com.codewithdipesh.habitized.ui.theme.regular
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionScreen(
    modifier: Modifier = Modifier,
    habitProgressId : UUID,
    title : String,
    targetDurationValue : LocalTime,
    colorKey : String,
    viewmodel: SessionViewModel,
    navController: NavController
){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    val scrollState = rememberScrollState()
    val screen = LocalConfiguration.current
    val height = screen.screenHeightDp
    val manager = remember { TimerServiceManager(context) }

    val state by viewmodel.state.collectAsState()
    var showStarter by remember {
        mutableStateOf(false)
    }
    var showCompleteAlertBox by remember { mutableStateOf(false) }

    var totalSeconds by remember { mutableStateOf(0) }

    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val onSurfaceColor  = MaterialTheme.colorScheme.onSurface
    var onPrimary by remember { mutableStateOf(onPrimaryColor) }
    var onSurface by remember { mutableStateOf(onSurfaceColor) }

    LaunchedEffect(state.theme) {
        when(state.theme){
            Normal -> {
                onPrimary = onPrimaryColor
                onSurface = onSurface
            }
            else ->{
                onPrimary = Color.White
                onSurface = Color(0xFF5F5F5F)
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
                            tint = if(state.theme == Normal) MaterialTheme.colorScheme.onPrimary
                            else Color.White
                        )
                    }
                },
                title = {
                    Text(
                        text = title,
                        style = TextStyle(
                            color = if(state.theme == Normal) MaterialTheme.colorScheme.onPrimary
                            else Color.White,
                            fontFamily = regular,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        ),
                        modifier = Modifier
                            .padding(top = 40.dp)
                    )
                },
                rightIcon = {
                    Row{
                        IconButton(
                            onClick = {
                                viewmodel.openThemes()
                            },
                            modifier = Modifier
                                .padding(top = 30.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.theme_icon),
                                contentDescription = "settings",
                                tint = if(state.theme == Normal) MaterialTheme.colorScheme.onPrimary
                                else Color.White
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        IconButton(
                            onClick = {
                                showCompleteAlertBox = true
                            },
                            modifier = Modifier
                                .padding(top = 30.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.tick_icon),
                                contentDescription = "mark as complete",
                                tint = if(state.theme == Normal) MaterialTheme.colorScheme.onPrimary
                                else Color.White
                            )
                        }
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


        if(state.isThemesOpen){
            ThemeChooser(
                sheetState = sheetState,
                selectedTheme = state.theme,
                onSelect = {
                    viewmodel.chooseTheme(it)
                },
                onDismiss = {
                    viewmodel.closeThemes()
                }
            )
        }

        //mark as complete
        if(showCompleteAlertBox){
            MarkAsCompleteAlertDialog(
                onDismiss = {
                    showCompleteAlertBox = false
                },
                onConfirm = {
                    //mark as done
                    scope.launch {
                        viewmodel.finishHabit()
                        manager.complete()
                        navController.navigateUp()
                        viewmodel.clearUi()
                    }
                }
            )
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .then( //different bg for diff theme
                when(state.theme){
                    Normal -> Modifier
                        .background(getThemedColorFromKey(colorKey))
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
                    Coffee -> Modifier.paint(
                        painter = painterResource(R.drawable.coffee_theme),
                        contentScale = ContentScale.FillBounds
                    )
                    Matcha -> Modifier.paint(
                        painter = painterResource(R.drawable.matcha_theme),
                        contentScale = ContentScale.FillBounds
                    )
                    Black -> Modifier.background(Color.Black)
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
                        manager = manager,
                        start = state.isStarted,
                        finished = state.timerState == TimerState.Finished,
                        onPrimary = onPrimary,
                        onStart = {
                            totalSeconds = it
                            showStarter = true
                        },
                        onPause = {
                            viewmodel.pauseTimer()
                        },
                        onResume = {
                            viewmodel.resumeTimer()
                        },
                        onCancel = {
                            scope.launch {
                                viewmodel.cancelTimer()
                            }
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
                //subtasks
                Box(
                    Modifier.align(Alignment.TopCenter)
                ){
                    Column(
                        modifier = Modifier
                            .padding(top =  (height/2).dp + 16.dp )
                            .fillMaxWidth(0.8f)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        state.habitWithProgress?.subtasks?.forEachIndexed { index, subTask ->
                            SubTaskEditor(
                                subTask = subTask,
                                enabled = true,
                                onToggleSubtask = {
                                    scope.launch {
                                        viewmodel.toggleSubTask(index)
                                    }
                                },
                                onChange = {
                                    scope.launch {
                                        viewmodel.updateSubTask(index,it)
                                    }
                                },
                                onDelete = {
                                    scope.launch {
                                        viewmodel.deleteSubTask(it)
                                    }
                                },
                                textSize = 14,
                                textColor = onPrimary,
                                onSurface = onSurface
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "+ List Subtask",
                            style = TextStyle(
                                color = onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 14.sp
                            ),
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable{
                                    if(state.tempSubTask == null){
                                         viewmodel.addSubTask()
                                    }
                                }
                        )
                    }
                }
            }


        }
    }
}