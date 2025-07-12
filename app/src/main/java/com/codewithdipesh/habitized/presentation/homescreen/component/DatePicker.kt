package com.codewithdipesh.habitized.presentation.homescreen.component

import android.R.attr.firstDayOfWeek
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.weekcalendar.rememberWeekCalendarState
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.LocalDate

@Composable
fun DatePicker(
    currentDate: LocalDate,
    onChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    onScrollChanged: ((Boolean) -> Unit)? = null
) {
    val startDate = remember { currentDate.minusDays(100) }
    val endDate = remember { currentDate.plusDays(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }
    val weekState = rememberWeekCalendarState(
        startDate = startDate,
        endDate = endDate,
        firstDayOfWeek = firstDayOfWeek
    )

    var selectedDay by remember { mutableStateOf<LocalDate>(currentDate) }

    //notify when scroll is in progress
    LaunchedEffect(weekState.isScrollInProgress) {
        onScrollChanged?.invoke(weekState.isScrollInProgress)

    }

    WeekCalendar(
        state = weekState,
        dayContent = { day ->
            WeekDayComponent(
                day,
                selected = selectedDay == day.date,
            ) {
                selectedDay = it
                onChange(it)
            }
        },
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    )

}

