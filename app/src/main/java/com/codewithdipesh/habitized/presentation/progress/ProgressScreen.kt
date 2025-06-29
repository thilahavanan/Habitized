package com.codewithdipesh.habitized.presentation.progress

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.presentation.addscreen.component.Selector
import com.codewithdipesh.habitized.presentation.homescreen.component.BottomNavBar
import com.codewithdipesh.habitized.presentation.homescreen.component.DatePicker
import com.codewithdipesh.habitized.presentation.homescreen.component.RunningTimer
import com.codewithdipesh.habitized.presentation.navigation.Screen
import com.codewithdipesh.habitized.presentation.progress.components.OverallProgress
import com.codewithdipesh.habitized.presentation.progress.components.WeeklyProgress
import com.codewithdipesh.habitized.ui.theme.playfair
import com.codewithdipesh.habitized.ui.theme.regular
import kotlinx.coroutines.launch

@Composable
fun ProgressScreen(
    modifier: Modifier = Modifier,
    viewmodel: ProgressViewModel,
    navController: NavController
) {
    val state by viewmodel.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            viewmodel.getAllGoals()
            viewmodel.getHabitProgresses()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.CenterStart
            ){
                Text(
                    text = "Progress",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = playfair,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 24.sp
                    ),
                    modifier = Modifier
                        .padding(top = 30.dp, start = 16.dp)
                )
            }
        }
    ){innerPadding->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            //goals
            item{
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)){
                    item {
                        Box(
                            modifier = Modifier
                                .size(300.dp,56.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(
                                    color = if(state.selectedGoal == null) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .clickable{
                                    viewmodel.selectGoal(null)
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "All Habits",
                                style = TextStyle(
                                    color = if(state.selectedGoal == null) MaterialTheme.colorScheme.inverseOnSurface
                                    else MaterialTheme.colorScheme.onPrimary,
                                    fontFamily = playfair,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                    items(state.goals){goal->
                        Box(
                            modifier = Modifier
                                .size(300.dp,56.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .background(
                                    color = if(goal.id == state.selectedGoal?.id) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(15.dp)
                                )
                                .clickable{
                                    viewmodel.selectGoal(goal)
                                },
                            contentAlignment = Alignment.Center
                        ){ 
                            Text(
                                text = goal.title,
                                style = TextStyle(
                                    color = if(state.selectedGoal == goal) MaterialTheme.colorScheme.inverseOnSurface
                                    else MaterialTheme.colorScheme.onPrimary,
                                    fontFamily = playfair,
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 13.sp
                                )
                            )
                        }
                    }
                }
            }

            //subheading and analytics
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    //heading
                    Text(
                        text = "Habits related to the Goal",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = playfair,
                            fontWeight = FontWeight.Bold,
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier
                            .padding(top = 30.dp, start = 16.dp)
                    )
                    //Analytics
                    Text(
                        text = "Analytics >",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier
                            .padding(top = 30.dp, start = 16.dp)
                    )

                }
                Spacer(Modifier.height(8.dp))
            }
            //optionChooser
            item{
                Selector(
                    options = Options.entries,
                    selectedOption = state.selectedOption,
                    onOptionSelected = {
                        viewmodel.setOption(it)
                    },
                    backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    nonSelectedTextColor = MaterialTheme.colorScheme.tertiary,
                    selectedOptionColor = MaterialTheme.colorScheme.tertiary,
                    nonSelectedOptionColor = MaterialTheme.colorScheme.surfaceVariant,
                    height = 50,
                    shape = RoundedCornerShape(25.dp)

                )
                Spacer(Modifier.height(8.dp))
            }
            item {
                AnimatedContent(
                    targetState = state.selectedOption,
                    label = "ProgressSwitcher"
                ) { option ->
                    Column(modifier = Modifier.fillMaxWidth()) {
                        when (option) {
                            Options.Weekly -> {
                                state.showedHabits.forEach {
                                    WeeklyProgress(
                                        date = state.date,
                                        weekDayRange = state.WeeklyDateRange,
                                        habit = it.habit,
                                        progresses = it.WeeklyProgresses,
                                        onClick = {
                                            navController.navigate(Screen.HabitScreen.createRoute(it.habit))
                                        }
                                    )
                                    Spacer(Modifier.height(16.dp))
                                }
                            }

                            Options.Overall -> {
                                state.showedHabits.forEach {
                                    OverallProgress(
                                        overallRange = state.OverAllDateRange,
                                        habit = it.habit,
                                        progresses = it.OverallProgresses,
                                        onClick = {
                                            navController.navigate(Screen.HabitScreen.createRoute(it.habit))
                                        }
                                    )
                                    Spacer(Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }

        }

        Box(
            modifier = Modifier.fillMaxSize()
        ){
            //BottomNavBar
            Box(
                modifier = Modifier.align(Alignment.BottomCenter),
                contentAlignment = Alignment.Center
            ){
                BottomNavBar(
                    selectedScreen = Screen.Progress,
                    onNavigate = { navController.navigate(it.route) }
                )
            }
        }

    }


}