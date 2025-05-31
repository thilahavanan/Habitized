package com.codewithdipesh.habitized.presentation.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.homescreen.component.BottomNavBar
import com.codewithdipesh.habitized.presentation.homescreen.component.DatePicker
import com.codewithdipesh.habitized.presentation.homescreen.component.FloatingActionOptions
import com.codewithdipesh.habitized.presentation.homescreen.component.HabitCard
import com.codewithdipesh.habitized.presentation.homescreen.component.OptionSelector
import com.codewithdipesh.habitized.presentation.navigation.Screen
import com.codewithdipesh.habitized.ui.theme.ndot
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewmodel: HomeViewModel
) {
    val scrollState = rememberScrollState()
    var previousScrollOffset by remember { mutableStateOf(0) }

    val state by viewmodel.uiState.collectAsState()
    var showAddingOptions by remember { mutableStateOf(false) }

    var showingOptionSelector by remember {
        mutableStateOf(true)
    }
    var showingDateTitle by remember {
        mutableStateOf(true)
    }

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
        },
        floatingActionButton = {
            FloatingActionOptions(
                showOptions = showAddingOptions,
                onAddHabitClicked = {
                    navController.navigate(Screen.AddHabit.createRoute(state.selectedDate))
                },
                onAddGoalClicked = {
                    navController.navigate(Screen.AddGoal.route)
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ){innerPadding->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .then(
                if(showAddingOptions) Modifier.blur(16.dp)
                 else Modifier
            )
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
            Column(modifier = Modifier.verticalScroll(scrollState)){
                state.habitWithProgressList.forEach { habit->
                    HabitCard(
                        habitWithProgress = habit
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }

        }
        //date picker
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopStart
        ){
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
            //BottomNavBar
            Box(
                modifier = Modifier.align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ){
                BottomNavBar(
                    selectedScreen = Screen.Home,
                    isShowingAddOption = showAddingOptions,
                    onAddClick = {showAddingOptions= !showAddingOptions}
                )
            }
        }

    }
}