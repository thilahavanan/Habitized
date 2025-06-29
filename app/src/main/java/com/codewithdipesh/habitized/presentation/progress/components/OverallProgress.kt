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
import androidx.compose.foundation.layout.wrapContentSize
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
fun OverallProgress(
    overallRange : List<LocalDate>,
    habit: Habit,
    progresses : List<HabitProgress>,
    onClick : () ->Unit,
    modifier: Modifier = Modifier
) {
    Progress(
        habit = habit,
        color = getThemedColorFromKey(habit.colorKey),
        onClick = {onClick()},
        progress = {
            Row(
                modifier = Modifier.wrapContentSize()
            ){
                var habitDayList : List<LocalDate> = emptyList()
                //list will contain all possible dates for habit progress
                when(habit.frequency){
                    Frequency.Daily -> {
                        habitDayList = overallRange
                    }
                    Frequency.Monthly -> {
                        habitDayList = overallRange.filter { habit.daysOfMonth!!.contains(it.dayOfMonth) }
                    }
                    Frequency.Weekly -> {
                        habitDayList = overallRange.filter { habit.days_of_week[it.dayOfWeek.value - 1] == 1 }
                    }
                    else ->{}
                }
                //filtering days for only after creating the habit
                habitDayList = habitDayList.filter { it >= habit.start_date }

                overallRange.chunked(7).forEach { week->
                    Column {
                        week.forEach { day ->
                            val progress = progresses.find { it.date == day }
                            OverAllCell(
                               color = getOriginalColorFromKey(habit.colorKey),
                               isActive = habitDayList.contains(day),
                               isSelect = (progress != null && progress.status == Status.Done)
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun OverAllCell(
    modifier: Modifier = Modifier,
    color: Color,
    isSelect: Boolean,
    isActive :Boolean
){
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(
                if (!isActive) MaterialTheme.colorScheme.surfaceContainerLow.copy(0.3f)
                else if (isSelect) color
                else MaterialTheme.colorScheme.surfaceContainerLow.copy(0.5f)
            )
    )
}
