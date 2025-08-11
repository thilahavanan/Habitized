package com.codewithdipesh.habitized.presentation.progress.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.presentation.util.IntToWeekDayMap
import com.codewithdipesh.habitized.ui.theme.instrumentSerif
import com.codewithdipesh.habitized.ui.theme.regular

@Composable
fun Progress(
    habit: Habit,
    color : Color,
    onClick : () ->Unit = {},
    progress : @Composable ()->Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clip(RoundedCornerShape(15.dp))
        .background(
            color = color,
            shape = RoundedCornerShape(15.dp)
        )
        .clickable{
            onClick()
        },
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
           //upper content
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ){
                //title and frequency
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ){
                    Text(
                        text = habit.title,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight =  FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = when(habit.frequency){
                            Frequency.Daily -> "Everyday"
                            Frequency.Weekly -> {
                                IntToWeekDayMap(habit.days_of_week)
                                    .filter { it.value == true }
                                    .keys
                                    .joinToString(", ") { it.name.lowercase().take(3) }
                            }
                            Frequency.Monthly -> {
                                habit.daysOfMonth!!.joinToString(", ")
                            }
                            else -> "Everyday"
                        },
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.surfaceDim,
                            fontFamily = regular,
                            fontWeight =  FontWeight.Normal,
                            fontSize = 12.sp
                        )
                    )
                }

                //streak
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.End
                ){
                    //current streak
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = habit.currentStreak.toString(),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight =  FontWeight.Bold,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontSize = 20.sp
                            )
                        )
                        //fire animation
                        FireAnimation(
                            modifier = Modifier.padding(start = 4.dp),
                            colorKey = habit.colorKey
                        )
                    }

                    Text(
                        text = "Maximum streak: ${habit.maxStreak}",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontFamily = regular,
                            fontWeight =  FontWeight.Light,
                            fontSize = 10.sp
                        )
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.surfaceContainerLow
            )
            Spacer(modifier = Modifier.height(8.dp))

            //progress
            progress()
        }
    }
}

