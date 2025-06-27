package com.codewithdipesh.habitized.presentation.addscreen.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.presentation.util.getThemedColorFromKey
import com.codewithdipesh.habitized.ui.theme.regular

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsForGoal(
    modifier: Modifier = Modifier,
    habits : List<Habit>,
    selected : List<Habit> = emptyList(),
    clickable : Boolean,
    onSelect : (List<Habit>) -> Unit = {},
    onDismiss : ()->Unit,
    sheetState : SheetState
) {

    var selectedHabits by remember {
        mutableStateOf(selected)
    }
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 70.dp,top = 30.dp)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(habits.isEmpty()){
                    Text(
                        text = "No Habit",
                        style = TextStyle(
                            fontFamily = regular,
                            color = MaterialTheme.colorScheme.scrim,
                            fontSize = 24.sp
                        ),
                        modifier = Modifier.padding(12.dp)
                            .padding(bottom = 16.dp)
                    )
                }
                habits.forEach{habit->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 4.dp)
                            .background(
                                color = if(selectedHabits.find { it.habit_id == habit.habit_id } != null) getThemedColorFromKey(habit.colorKey) else Color.Transparent,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .clip(RoundedCornerShape(10.dp))
                            .then(
                                if(clickable){
                                    Modifier.clickable{
                                        if(selectedHabits.find { it.habit_id == habit.habit_id } != null){
                                            selectedHabits = selectedHabits.filter { it.habit_id != habit.habit_id }
                                        }else{
                                            selectedHabits = selectedHabits + habit
                                        }

                                    }
                                }
                                else Modifier
                            ),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = habit.title,
                            style = TextStyle(
                                fontFamily = regular,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            //button
            if(clickable){
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ){
                    Button(
                        onClick = {
                            onDismiss()
                            onSelect(selectedHabits)
                        },
                        bgColor = colorResource(R.color.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                    ){
                        Text(
                            text = "Add",
                            style = TextStyle(
                                fontFamily = regular,
                                color = MaterialTheme.colorScheme.inverseOnSurface,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsForHabit(
    modifier: Modifier = Modifier,
    goals : List<Goal>,
    onSelect : (Goal?) -> Unit = {},
    onDismiss : ()->Unit,
    sheetState : SheetState
) {

    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 50.dp,top = 30.dp)
                    .wrapContentHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(goals.isEmpty()){
                    Text(
                        text = "No Goal",
                        style = TextStyle(
                            fontFamily = regular,
                            color = MaterialTheme.colorScheme.scrim,
                            fontSize = 24.sp
                        ),
                        modifier = Modifier.padding(12.dp)
                            .padding(bottom = 16.dp)
                    )
                }
                goals.forEach{goal->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .clickable{
                                onDismiss()
                                onSelect(goal)
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = goal.title,
                            style = TextStyle(
                                fontFamily = regular,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 16.sp
                            ),
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
                //no goal
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable{
                            onDismiss()
                            onSelect(null)
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "No Goal",
                        style = TextStyle(
                            fontFamily = regular,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}