package com.codewithdipesh.habitized.domain.util

import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.ReminderType
import com.codewithdipesh.habitized.presentation.util.Days
import com.codewithdipesh.habitized.presentation.util.toDaysOfWeek
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters

data class NextAlarmResult(
    val nextDateTime: LocalDateTime,
    val error: Boolean
)

/**
 * Calculates the next alarm time for a habit.
 * @param date The reference date (usually today or the habit's start date)
 * @param now The current date and time
 * @param frequency The habit's frequency
 * @param reminderTime The time of day for the reminder
 * @param daysOfWeek Map of Days to Boolean for weekly habits
 * @param daysOfMonth List of Int for monthly habits
 */
fun getNextAlarmDateTime(
    date: LocalDate,
    now: LocalDateTime,
    frequency: Frequency,
    reminderType: ReminderType,
    daysOfWeek: Map<Days, Boolean> = emptyMap(),
    daysOfMonth: List<Int> = emptyList()
): NextAlarmResult {
    var nextDateTime = LocalDateTime.now()
    var error = false
    val maxLoopIterations = 365

    when(reminderType){
        is ReminderType.Interval -> {
            val interval = reminderType.interval
            val fromTime = reminderType.fromTime
            val toTime = reminderType.toTime

            if(interval < 1){
                error = true
                return NextAlarmResult(nextDateTime,error)
            }

            var currentDayFrom = LocalDateTime.of(now.toLocalDate(), fromTime)
            val currentDayTo = LocalDateTime.of(now.toLocalDate(), toTime)

            if(now.isBefore(currentDayFrom)){
                nextDateTime = currentDayFrom
            }else{
                val minutesSinceFrom = ChronoUnit.MINUTES.between(currentDayFrom,now)
                val addMinutes = interval - (minutesSinceFrom % interval)
                nextDateTime = now.plusMinutes(addMinutes)

                if (nextDateTime.isAfter(currentDayTo) || nextDateTime.isEqual(currentDayTo)) {
                    nextDateTime = LocalDateTime.of(now.toLocalDate().plusDays(1), fromTime)
                }
            }
            var iterations = 0
            while (
                !isValidDay(nextDateTime.toLocalDate(), frequency, daysOfWeek, daysOfMonth) ||
                nextDateTime.isBefore(now) || !isWithinWindow(nextDateTime.toLocalTime(), fromTime, toTime)
            ){
                nextDateTime = nextDateTime.plusDays(1).with(fromTime)  // Advance day and reset to fromTime
                iterations++
                if (iterations > maxLoopIterations) {
                    error = true
                    break
                }
            }
        }
        is ReminderType.Once -> {
            val reminderTime = reminderType.reminderTime
            when (frequency) {
                Frequency.Daily -> {
                    nextDateTime = LocalDateTime.of(date,reminderTime ) //exact time of today
                    if (nextDateTime.isBefore(now)) {
                        nextDateTime = nextDateTime.plusDays(1) //if it already passed then next day
                    }
                }
                Frequency.Weekly -> {
                    val selectedDays = daysOfWeek // Map<Days, Boolean>
                    nextDateTime = LocalDateTime.MAX //initializing with max for calculating the min among days
                    Days.entries.forEach { day ->
                        if (selectedDays[day] == true) {
                            //get the next mon...sat day
                            //(date.with) it will get the next __ day from $date
                            val nextDate = date.with(TemporalAdjusters.nextOrSame(day.toDaysOfWeek()))
                            var potentialDateTime = LocalDateTime.of(nextDate, reminderTime)
                            //if the day is today and passed then next week dame day else today only
                            if(potentialDateTime.isBefore(now)){
                                potentialDateTime = potentialDateTime.plusWeeks(1)
                            }
                            nextDateTime = minOf(potentialDateTime,nextDateTime) //minimum of all potential days
                        }
                    }
                }
                Frequency.Monthly -> {
                    val selectedDays = daysOfMonth // List<Int>
                    nextDateTime = LocalDateTime.MAX
                    selectedDays.forEach { dayOfMonth ->
                        try {
                            //same as month
                            val nextDate = now.toLocalDate().withDayOfMonth(dayOfMonth)
                            var potentialDateTime = LocalDateTime.of(nextDate, reminderTime)
                            //the date has passed already s0 -> next month same date
                            if(potentialDateTime.isBefore(now) || potentialDateTime.isEqual(now)){
                                try {
                                    val nextMonthDate = now.toLocalDate().plusMonths(1).withDayOfMonth(dayOfMonth)
                                    potentialDateTime = LocalDateTime.of(nextMonthDate, reminderTime)
                                } catch (e: DateTimeException) {
                                    //for  exceptional like Feb 31
                                    // If next month doesn't have this day, skip to the month after
                                    val monthAfterNext = now.toLocalDate().plusMonths(2).withDayOfMonth(dayOfMonth)
                                    potentialDateTime = LocalDateTime.of(monthAfterNext, reminderTime)
                                }
                            }
                            nextDateTime = minOf(potentialDateTime,nextDateTime)
                        } catch (e: DateTimeException) {
                            // Handle invalid dates (e.g., Feb 30th)
                            // Skip this day of month
                            error = true
                        }
                    }
                }
            }
        }
    }
    return NextAlarmResult(nextDateTime, error)
}

//Check if time is within from/to
private fun isWithinWindow(time: LocalTime, from: LocalTime, to: LocalTime): Boolean {
    return !time.isBefore(from) && !time.isAfter(to)
}
//check day edge case
private fun isValidDay(date: LocalDate, freq: Frequency, daysOfWeek: Map<Days, Boolean>, daysOfMonth: List<Int>): Boolean {
    return when (freq) {
        Frequency.Daily -> true
        Frequency.Weekly -> daysOfWeek[Days.valueOf(date.dayOfWeek.name)] == true
        Frequency.Monthly -> try {
            daysOfMonth.contains(date.dayOfMonth)
        } catch (e: DateTimeException) {
            false  // Invalid date, e.g., Feb 30
        }
    }
}
