package com.codewithdipesh.habitized.presentation.homescreen.component

import com.codewithdipesh.habitized.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.presentation.util.toWord
import com.codewithdipesh.habitized.ui.theme.ndot
import com.codewithdipesh.habitized.ui.theme.regular
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID

//count based
@Composable
fun HabitCard(
    habitWithProgress: HabitWithProgress ,
    onAddButton : () -> Unit = {},
    modifier: Modifier = Modifier
){
    when(habitWithProgress.habit.type){
        HabitType.Count -> {
            CountHabit(habitWithProgress = habitWithProgress)
        }
        HabitType.Duration -> {
            DurationHabit(habitWithProgress = habitWithProgress)
        }
        HabitType.OneTime ->{
            OneTimeHabit(habitWithProgress = habitWithProgress)
        }
        HabitType.Session -> TODO()
    }
}

@Composable
fun OneTimeHabit(
    modifier: Modifier = Modifier,
    habitWithProgress: HabitWithProgress
) {
    HabitElement(
        color = colorResource(habitWithProgress.habit.color),
        reminder = habitWithProgress.habit.reminder_time
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ){
            //title
            Text(
                text = habitWithProgress.habit.title,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun CountHabit(
    modifier: Modifier = Modifier,
    habitWithProgress: HabitWithProgress
) {
    HabitElement(
        color = colorResource(habitWithProgress.habit.color),
        reminder = habitWithProgress.habit.reminder_time
    ){
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //title
            Text(
                text = habitWithProgress.habit.title,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .fillMaxWidth(.45f)
                    .wrapContentHeight()
            )

            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                //progress
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = "${habitWithProgress.progress.currentCount}/"+
                                "${habitWithProgress.progress.targetCount}",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    )
                    //param ex:- glasses
                    Text(
                        text = habitWithProgress.progress.countParam.displayName,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    )
                }

                //button
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(
                            color = MaterialTheme.colorScheme.inverseOnSurface,
                            shape = CircleShape
                        )
                        .clickable {
                            //todo
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.add),
                        contentDescription = "add ${habitWithProgress.habit.title}",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }


        }
    }
}

@Composable
fun DurationHabit(
    modifier: Modifier = Modifier,
    habitWithProgress: HabitWithProgress
) {
    HabitElement(
        color = colorResource(habitWithProgress.habit.color),
        reminder = habitWithProgress.habit.reminder_time
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ){
            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                //start button
                Box(
                   modifier = Modifier.size(80.dp,20.dp)
                       .clip(RoundedCornerShape(10.dp))
                       .border(1.dp,MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(10.dp))
                       .clickable{
                          //todo
                       },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Start",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = ndot,
                            fontSize = 12.sp
                        )
                    )
                }

                VerticalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.height(18.dp)
                )

                //time
                habitWithProgress.habit.duration?.let {
                    Text(
                        text = habitWithProgress.habit.duration.toWord(),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            //title
            Text(
                text = habitWithProgress.habit.title,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = regular,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }
    }
}
