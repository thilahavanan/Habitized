package com.codewithdipesh.habitized.presentation.progress.components

import android.graphics.fonts.FontStyle
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.Status
import com.codewithdipesh.habitized.presentation.util.IntToWeekDayMap
import com.codewithdipesh.habitized.presentation.util.getOriginalColorFromKey
import com.codewithdipesh.habitized.presentation.util.getThemedColorFromKey
import com.codewithdipesh.habitized.ui.theme.playfair
import com.codewithdipesh.habitized.ui.theme.regular
import java.time.LocalDate
import java.time.LocalTime
import java.util.Locale
import java.util.UUID
import kotlin.String

@Composable
fun Progress(
    habit: Habit,
    color : Color,
    progress : @Composable ()->Unit
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clip(RoundedCornerShape(15.dp))
        .background(
            color = color,
            shape = RoundedCornerShape(15.dp)
        ),
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
                    Row {
                        Text(
                            text = habit.title,
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = playfair,
                                fontWeight =  FontWeight.Bold,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                fontSize = 18.sp
                            )
                        )
                        Icon(
                            painter = painterResource(R.drawable.fire_icon),
                            contentDescription = "streak",
                            tint = getOriginalColorFromKey(habit.colorKey)
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

