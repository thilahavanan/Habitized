package com.codewithdipesh.habitized.data.local.mapper

import android.util.Log
import com.codewithdipesh.habitized.data.local.entity.HabitEntity
import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.ReminderType
import com.codewithdipesh.habitized.presentation.util.SingleString
import com.codewithdipesh.habitized.presentation.util.toColor
import com.codewithdipesh.habitized.presentation.util.toInt
import com.codewithdipesh.habitized.presentation.util.toLocalTime
import com.codewithdipesh.habitized.presentation.util.toWord
import java.util.UUID
import kotlin.math.max

fun Habit.toEntity(): HabitEntity {
    Log.d("HabitMapper", "Converting Habit to Entity with reminderType: $reminderType")
    val days_of_Month = daysOfMonth?.joinToString(",")
    val entity = HabitEntity(
        habit_id = habit_id ?: UUID.randomUUID(),
        title = title,
        description = description,
        type = type.displayName,
        goal_id = goal_id,
        start_date = start_date,
        frequency = frequency.displayName,
        days_of_week = days_of_week.joinToString(","),
        daysOfMonth = days_of_Month,
        reminderType = if(reminderType != null) reminderType.displayName else null,
        reminderFrom = if(reminderType is ReminderType.Interval) reminderType.fromTime else null,
        reminderTo = if(reminderType is ReminderType.Interval) reminderType.toTime else null,
        reminderInterval = if(reminderType is ReminderType.Interval) reminderType.interval else null,
        reminder_time = if(reminderType is ReminderType.Once) reminderType.reminderTime else null,
        is_active = is_active,
        colorKey = colorKey,
        countParam = countParam.toString(),
        countTarget = countTarget,
        duration = duration?.SingleString(),
        currentStreak = currentStreak,
        maxStreak = maxStreak
    )
    Log.d("HabitMapper", "Created entity with reminderType: ${entity.reminderType}, reminderFrom: ${entity.reminderFrom}, reminderTo: ${entity.reminderTo}, reminderInterval: ${entity.reminderInterval}, reminder_time: ${entity.reminder_time}")
    return entity
}

fun HabitEntity.toHabit(): Habit {
    Log.d("HabitEntity", "toHabit: $this")

    return Habit(
        habit_id = habit_id,
        title = title,
        description = description,
        type = HabitType.fromString(type),
        goal_id = goal_id,
        start_date = start_date,
        frequency = Frequency.fromString(frequency),
        days_of_week = days_of_week.split(",").map { it.toInt() },
        daysOfMonth = if(daysOfMonth != "") daysOfMonth?.split(",")?.map{it.toInt()} else null,
        reminderType = when {
            reminderType != null -> {
                when (reminderType.lowercase()) {
                    "once" -> {
                        if (reminder_time != null) {
                            ReminderType.Once(reminder_time)
                        } else {
                            Log.w("HabitMapper", "reminder_time is null for Once type in habit $habit_id")
                            null
                        }
                    }
                    "interval" -> {
                        if (reminderInterval != null && reminderFrom != null && reminderTo != null) {
                            ReminderType.Interval(
                                interval = reminderInterval,
                                fromTime = reminderFrom,
                                toTime = reminderTo
                            )
                        } else {
                            Log.w("HabitMapper", "Interval fields are null for Interval type in habit $habit_id: interval=$reminderInterval, from=$reminderFrom, to=$reminderTo")
                            null
                        }
                    }
                    else -> {
                        Log.w("HabitMapper", "Unknown reminder type: $reminderType for habit $habit_id")
                        null
                    }
                }
            }
            else -> null
        },
        is_active = is_active,
        colorKey = colorKey,
        countParam = CountParam.fromString(countParam),
        countTarget = countTarget,
        duration = duration?.toLocalTime(),
        currentStreak = currentStreak,
        maxStreak = maxStreak
    )
}
