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
        type = type.toString(),
        reminder_time = reminder_time,
        countParam = countParam.toString(),
        currentCount = currentCount,
        targetCount = targetCount,
        currentDurationValue = currentDurationValue,
        targetDurationValue = targetDurationValue,
        currentSessionNumber = currentSessionNumber,
        targetSessionNumber = targetSessionNumber,
        status = status.toString(),
        notes = notes,
        excuse = excuse
    )
}


fun HabitProgressEntity.toHabitProgress(): HabitProgress {
    return HabitProgress(
        progressId = progressId,
        habitId = habitId,
        title = title,
        date = date,
        type = HabitType.fromString(type),
        reminder_time = reminder_time,
        countParam = CountParam.fromString(countParam),
        currentCount = currentCount,
        targetCount = targetCount,
        currentDurationValue = currentDurationValue,
        targetDurationValue = targetDurationValue,
        currentSessionNumber = currentSessionNumber,
        targetSessionNumber = targetSessionNumber,
        status = Status.fromString(status),
        notes = notes,
        excuse = excuse
    )
}
