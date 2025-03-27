package com.codewithdipesh.habitized.data.local.mapper

import com.codewithdipesh.habitized.data.local.entity.HabitEntity
import com.codewithdipesh.habitized.data.local.entity.HabitProgressEntity
import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.Status

fun HabitProgress.toEntity(): HabitProgressEntity {
    return HabitProgressEntity(
        progressId = progressId,
        habitId = habitId,
        title = title,
        date = date,
        currentCount = currentCount,
        targetCount = targetCount,
        durationValue = durationValue,
        percentageValue = percentageValue,
        numberSessionDone = numberSessionDone,
        status = status.toString(),
        notes = notes,
        excuse = excuse,
        fallAsleepTime = fallAsleepTime,
        wakeUpTime = wakeUpTime
    )
}


fun HabitProgressEntity.toHabitProgress(): HabitProgress {
    return HabitProgress(
        progressId = progressId,
        habitId = habitId,
        title = title,
        date = date,
        currentCount = currentCount,
        targetCount = targetCount,
        durationValue = durationValue,
        percentageValue = percentageValue,
        numberSessionDone = numberSessionDone,
        status = Status.fromString(status),
        notes = notes,
        excuse = excuse,
        fallAsleepTime = fallAsleepTime,
        wakeUpTime = wakeUpTime
    )
}
