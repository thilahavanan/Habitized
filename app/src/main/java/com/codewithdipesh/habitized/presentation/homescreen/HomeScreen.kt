package com.codewithdipesh.habitized.presentation.homescreen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.domain.model.Status
import com.codewithdipesh.habitized.presentation.homescreen.component.AddSubTask
import com.codewithdipesh.habitized.presentation.homescreen.component.AddingOption
import com.codewithdipesh.habitized.presentation.homescreen.component.BottomNavBar
import com.codewithdipesh.habitized.presentation.homescreen.component.CountUpdater
import com.codewithdipesh.habitized.presentation.homescreen.component.DatePicker
import com.codewithdipesh.habitized.presentation.homescreen.component.HabitCard
import com.codewithdipesh.habitized.presentation.homescreen.component.OptionSelector
import com.codewithdipesh.habitized.presentation.homescreen.component.RunningTimer
import com.codewithdipesh.habitized.presentation.homescreen.component.SkipAlertDialog
import com.codewithdipesh.habitized.presentation.navigation.Screen
import com.codewithdipesh.habitized.ui.theme.ndot
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewmodel: HomeViewModel
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var previousScrollOffset by remember { mutableStateOf(0) }

    val state by viewmodel.uiState.collectAsState()
    var showAddingOptions by remember { mutableStateOf(false) }

    var showingOptionSelector by remember { mutableStateOf(true) }
    var showingDateTitle by remember { mutableStateOf(true) }

    var showingSubtaskAdding by remember { mutableStateOf(false) }
    var habitForSubTaskAdding by remember { mutableStateOf<HabitWithProgress?>(null) }

    var showingCounter by remember { mutableStateOf(false) }
    var habitForCounter by remember { mutableStateOf<HabitWithProgress?>(null) }

    var showingSkipAlert by remember { mutableStateOf(false) }
    var habitForShowingAlert by remember { mutableStateOf<HabitWithProgress?>(null) }


    LaunchedEffect(state.selectedDate) {
        viewmodel.loadHomePage(state.selectedDate)
    }
    LaunchedEffect(scrollState.isScrollInProgress) {
        if(state.isShowingDatePicker && scrollState.isScrollInProgress){
            viewmodel.toggleDatePicker()
        }
    }
    LaunchedEffect(scrollState.value) {
        val currentOffset = scrollState.value
        showingDateTitle = currentOffset <= 10

        if(showingDateTitle){
            showingOptionSelector = true
        }else{
            if( currentOffset > previousScrollOffset || currentOffset == scrollState.maxValue){
                showingOptionSelector = false
            }else{
                showingOptionSelector = true
            }
        }
        previousScrollOffset = currentOffset
    }


    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ){
                IconButton(
                    onClick = {//todo
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                        .padding(top = 30.dp,start = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.three_dots),
                        contentDescription = "menu",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    ){innerPadding->
        //adding option
        if(showAddingOptions){
            AddingOption(
                onDismiss = {
                    showAddingOptions = false
                },
                onAddHabitClicked = {
                    navController.navigate(Screen.AddHabit.createRoute(state.selectedDate))
                },
                onAddGoalClicked = {
                    navController.navigate(Screen.AddGoal.route)
                }
            )
        }
        //session habit
        if(showingSubtaskAdding && habitForSubTaskAdding != null){
            AddSubTask(
                habitWithProgress = habitForSubTaskAdding!!,
                onUpdateSubTask = {
                    scope.launch{
                        viewmodel.addUpdateSubTasks(it,habitForSubTaskAdding!!.progress.progressId)
                        showingSubtaskAdding = false
                    }

                }
            )
        }

        //counter habit
        if(showingCounter && habitForCounter != null){
            CountUpdater(
                habitWithProgress = habitForCounter!!,
                onUpdateCounter = {
                    scope.launch{
                        viewmodel.onUpdateCounter(it,habitForCounter!!.progress)
                        showingCounter = false
                    }

                }
            )
        }
        //for skipping alert of one time ha it
        if(showingSkipAlert && habitForShowingAlert != null){
            SkipAlertDialog(
                habitWithProgress = habitForShowingAlert!!,
                onDismiss = {
                    showingSkipAlert = false
                },
                onConfirm = {
                    viewmodel.onSkipHabit(it.progress)
                    viewmodel.loadHomePage(state.selectedDate)
                }
            )
        }

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp)
            .padding(bottom = 80.dp)
        ){
            val date = state.selectedDate.format(
                DateTimeFormatter.ofPattern("dd MMM")
            )

            AnimatedVisibility(showingDateTitle) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ){
                    Text(
                        text = if(state.selectedDate == LocalDate.now()) "Today,$date" else "$date",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = ndot,
                            fontWeight = FontWeight.Normal,
                            fontSize = 24.sp
                        ),
                        modifier = Modifier
                            .padding(start = 8.dp)
                    )
                    IconButton(
                        onClick = {
                            //date picker
                            viewmodel.toggleDatePicker()
                        }
                    ) {
                        val rotation by animateFloatAsState(
                            targetValue = if(state.isShowingDatePicker) 180f else 0f,
                            animationSpec = tween(300),
                            label = "datePicker"
                        )
                        Icon(
                            painter = painterResource(R.drawable.toggle),
                            contentDescription = "select date",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier =Modifier.graphicsLayer(
                                rotationZ = rotation
                            )
                        )
                    }
                }
            }
            AnimatedVisibility(showingOptionSelector) {
                OptionSelector(
                    selectedOption = state.selectedOption,
                    onOptionSelected = {viewmodel.onOptionSelected(it)}
                )
            }
            Spacer(Modifier.height(16.dp))
            //habits

            Column(modifier = Modifier.verticalScroll(
                scrollState, 
                flingBehavior = ScrollableDefaults.flingBehavior()
            )
            ) {
                //no started / ongoing
                state.habitWithProgressList
                    .filter { it.progress.status == Status.NotStarted }
                    .forEach{ habit->
                         key(habit.habit.habit_id){
                             HabitCard(
                                 habitWithProgress = habit,
                                 onSubTaskAdding = {
                                     showingSubtaskAdding = true
                                     habitForSubTaskAdding = it
                                 },
                                 onToggle = {
                                     viewmodel.toggleSubtask(it)
                                 },
                                 onSkip = {
                                     showingSkipAlert = true
                                     habitForShowingAlert = it
                                 },
                                 onDone = {
                                     viewmodel.onDoneHabit(it.progress)
                                     viewmodel.loadHomePage(state.selectedDate)
                                 },
                                 onAddCounter = {
                                     showingCounter = true
                                     habitForCounter = it
                                 },
                                 onStartDuration = {
                                     if(state.ongoingHabit != null && it != state.ongoingHabit ){
                                         scope.launch {
                                             Toast.makeText(context,"Already Habit Running", Toast.LENGTH_SHORT).show()
                                         }
                                     }else{
                                         navController.navigate(Screen.DurationScreen.createRoute(it))
                                     }
                                 },
                                 onFutureTaskStateChange = {
                                     scope.launch {
                                         Toast.makeText(context,"Can't start Future Tasks", Toast.LENGTH_SHORT).show()
                                     }
                                 }
                             )
                             Spacer(Modifier.height(16.dp))
                         }
                }
                //finished or skipped //todo
                state.habitWithProgressList
                    .filter { it.progress.status != Status.NotStarted }
                    .forEach{ habit->
                        key(habit.habit.habit_id){
                            HabitCard(
                                habitWithProgress = habit,
                                onSubTaskAdding = {
                                    showingSubtaskAdding = true
                                    habitForSubTaskAdding = it
                                },
                                onToggle = {
                                    viewmodel.toggleSubtask(it)
                                },
                                onUnSkip = {
                                    viewmodel.onUnSkipDoneHabit(it.progress)
                                    viewmodel.loadHomePage(state.selectedDate)
                                },
                                onAddCounter = {
                                    showingCounter = true
                                    habitForCounter = it
                                },
                                onFutureTaskStateChange = {
                                    scope.launch {
                                        Toast.makeText(context,"Can't start Future Tasks", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                            Spacer(Modifier.height(16.dp))
                        }
                    }

                //if showing ongoing timer then padding
                if(state.ongoingHabit != null){
                    Spacer(Modifier.height(150.dp))
                }
            }

        }

        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ){  //date picker
            Box(
               modifier =  Modifier.padding(top = 120.dp)
            ){
                AnimatedVisibility(
                    visible = state.isShowingDatePicker,
                    enter = expandVertically(),
                    exit = shrinkVertically(animationSpec = tween(100))
                ) {
                    DatePicker(
                        currentDate = state.selectedDate,
                        onChange = {
                            viewmodel.onDateSelected(it)
                            viewmodel.toggleDatePicker()
                        }
                    )

                }
            }
            //ongoing timer
            Box(
                modifier = Modifier.align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ){
                AnimatedVisibility(
                    visible = state.ongoingHabit != null,
                    enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(),
                    exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut()
                ) {
                    state.ongoingHabit?.let {
                        RunningTimer(
                            habitWithProgress = state.ongoingHabit!!,
                            onClick = {
                                navController.navigate(Screen.DurationScreen.createRoute(it))
                            },
                            onTimerFinished = {
                                scope.launch{
                                    viewmodel.finishTimer()
                                }
                            },
                            modifier = Modifier
                                .padding(bottom= 90.dp)
                        )
                    }
                }
            }
            //BottomNavBar
            Box(
                modifier = Modifier.align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ){
                BottomNavBar(
                    selectedScreen = Screen.Home,
                    onAddClick = {showAddingOptions= !showAddingOptions}
                )
            }
        }

    }
}