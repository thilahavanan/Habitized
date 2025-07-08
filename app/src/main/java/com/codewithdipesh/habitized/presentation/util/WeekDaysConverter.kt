package com.codewithdipesh.habitized.presentation.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.text.util.LocalePreferences
import com.codewithdipesh.habitized.domain.model.Frequency
import java.time.DayOfWeek
import java.time.LocalDate


fun IntToWeekDayMap(days: List<Int>): Map<Days, Boolean> {
    val ans = mutableMapOf<Days, Boolean>()
    days.forEachIndexed { index, value ->
        daystoIndex[index]?.let { day ->
            ans[day] = value == 1
        }
    }
    return ans
}

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
fun WeekDayMapToInt(days: Map<Days, Boolean>): List<Int> {
    val ans = mutableListOf<Int>()
    daystoIndex.values.forEach { day ->
        ans.add(if (days[day] == true) 1 else 0)
    }
    return ans
}

fun getWeekDayIndex(dayOfWeek: DayOfWeek) : Int{
    return when(dayOfWeek){
        DayOfWeek.MONDAY -> 0
        DayOfWeek.TUESDAY -> 1
        DayOfWeek.WEDNESDAY -> 2
        DayOfWeek.THURSDAY -> 3
        DayOfWeek.FRIDAY -> 4
        DayOfWeek.SATURDAY -> 5
        DayOfWeek.SUNDAY -> 6
    }
}

enum class Days {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

fun DayOfWeek.toDays(): Days {
    return when (this) {
        DayOfWeek.MONDAY -> Days.MONDAY
        DayOfWeek.TUESDAY -> Days.TUESDAY
        DayOfWeek.WEDNESDAY -> Days.WEDNESDAY
        DayOfWeek.THURSDAY -> Days.THURSDAY
        DayOfWeek.FRIDAY -> Days.FRIDAY
        DayOfWeek.SATURDAY -> Days.SATURDAY
        DayOfWeek.SUNDAY -> Days.SUNDAY
    }

}

fun Days.toDaysOfWeek(): DayOfWeek {
    return when(this){
        Days.MONDAY -> DayOfWeek.MONDAY
        Days.TUESDAY -> DayOfWeek.TUESDAY
        Days.WEDNESDAY -> DayOfWeek.WEDNESDAY
        Days.THURSDAY -> DayOfWeek.THURSDAY
        Days.FRIDAY -> DayOfWeek.FRIDAY
        Days.SATURDAY -> DayOfWeek.SATURDAY
        Days.SUNDAY -> DayOfWeek.SUNDAY
    }
}

val daystoIndex: Map<Int, Days> = mapOf(
     0 to Days.MONDAY,
     1 to Days.TUESDAY,
     2 to Days.WEDNESDAY,
     3 to Days.THURSDAY,
     4 to Days.FRIDAY,
     5 to Days.SATURDAY,
     6 to Days.SUNDAY
)

val DailySelected = mapOf(
    Days.MONDAY to true,
    Days.TUESDAY to true,
    Days.WEDNESDAY to true,
    Days.THURSDAY to true,
    Days.FRIDAY to true,
    Days.SATURDAY to true,
    Days.SUNDAY to true
)
