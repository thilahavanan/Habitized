package com.codewithdipesh.habitized.presentation.homescreen.component

import com.codewithdipesh.habitized.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.ui.theme.regular
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

//count based
@Composable
fun HabitCard(
    habitWithProgress : HabitWithProgress,
    onAddButton : () -> Unit = {},
    modifier: Modifier = Modifier
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentSize()
        .background(
            color = colorResource(R.color.secondary_gray),
            shape = RoundedCornerShape(15.dp)
        ),
        contentAlignment = Alignment.Center
    ){
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){  //title
            Text(
                text = habitWithProgress.habit.title,
                style = TextStyle(
                    color = colorResource(R.color.white),
                    fontFamily = regular,
                    fontStyle = FontStyle.Normal,
                    fontSize = 16.sp
                )
            )

            Row {
                //count ex:- 9/10
                Text(
                    text =
                        if(habitWithProgress.progress != null) "${habitWithProgress.progress.currentCount}/${habitWithProgress.habit.countTarget}"
                        else "0/${habitWithProgress.habit.countTarget}"
                    ,
                    style = TextStyle(
                        color = colorResource(R.color.white),
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    )
                )
                //param ex:- glasses
                Text(
                    text = habitWithProgress.habit.countParam?.displayName ?: "",
                    style = TextStyle(
                        color = colorResource(R.color.white),
                        fontFamily = regular,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                )
                //button
                Box(modifier = Modifier
                    .size(30.dp)
                    .background(
                        color = colorResource(R.color.white),
                        shape = CircleShape
                    )
                    .clickable{
                        onAddButton()
                    },
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(R.drawable.add),
                        contentDescription = "add ${habitWithProgress.habit.title}"
                    )
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HabitCardPreview() {
    HabitCard(
        habitWithProgress = HabitWithProgress(
            habit = Habit(
                habit_id = UUID.randomUUID(),
                title = "Morning Run",
                description = "Run every morning for better health",
                type = HabitType.Duration,
                goal_id = UUID.randomUUID(),
                start_date = LocalDate.now(),
                frequency = Frequency.Daily,
                days_of_week = listOf(1, 1, 1, 1, 1, 0, 0),
                daysOfMonth = null,
                reminder_time = LocalTime.of(6, 30),
                is_active = true,
                color = android.R.color.holo_blue_light,
                countParam = CountParam.Times,
                countTarget = null,
                durationParam = "Minutes",
                duration = 30f
            ),
            date = LocalDate.now(),
            progress = null,
            subtasks = emptyList()
        )
    )
}
