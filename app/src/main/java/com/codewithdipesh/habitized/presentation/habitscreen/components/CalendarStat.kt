package com.codewithdipesh.habitized.presentation.habitscreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.ui.theme.regular
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.HorizontalYearCalendar
import com.kizitonwose.calendar.compose.WeekCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.compose.yearcalendar.rememberYearCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.ExperimentalCalendarApi
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import java.time.DayOfWeek
import java.time.Year
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalCalendarApi::class)
@Composable
fun CalendarStat(
    progressList: List<HabitProgress> = emptyList(),
    color: Color,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    height: Int = 300,
    width: Dp,
    dayTextSize : Int = 16,
    onclick: (HabitProgress) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state = rememberCalendarState(
        startMonth= YearMonth.now().minusYears(4),
        endMonth = YearMonth.now().plusYears(4),
        firstVisibleMonth = YearMonth.now()
    )
    HorizontalCalendar(
        state = state,
        dayContent = { day ->
            if (day.position == DayPosition.MonthDate) {
                val progress = progressList.find { it.date == day.date }
                Day(
                    day = day,
                    isSelected = progress != null,
                    color = color,
                    size = dayTextSize,
                    onclick = {
                        onclick(progress ?: return@Day)
                    }
                )
            } else {
                // Empty Box for non-month days (padding cells)
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(Color.Transparent)
                )
            }
        }
        ,
        monthContainer = { _, container ->
            Element(
                backgroundColor = backgroundColor,
                modifier = Modifier
                    .width(width)
                    .height(height.dp)
                    .padding(top = 8.dp, bottom = 8.dp, end = 16.dp)
            ) {
                container() // Render the provided container!
            }
        },
        monthHeader = {month->
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Top
            ){
                Text(
                    text = "${month.yearMonth.month.name.lowercase().capitalize(Locale.ROOT)} ${month.yearMonth.year}",
                    style = androidx.compose.ui.text.TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 18.sp,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.padding(end = 16.dp,bottom= 8.dp)
                )

            }
        }
    )

}

@Composable
fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    color: Color,
    size : Int = 16,
    onclick : (CalendarDay) -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(25.dp))
            .background(
                color = if (isSelected) color else Color.Transparent
            )
            .then(
                if(isSelected){
                    Modifier.clickable{
                       onclick(day)
                    }
                }else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            style = androidx.compose.ui.text.TextStyle(
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = size.sp,
                fontFamily = regular,
                fontWeight = FontWeight.Normal
            )
        )
    }
}
