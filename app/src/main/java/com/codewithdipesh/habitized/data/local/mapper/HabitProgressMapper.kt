package com.codewithdipesh.habitized.data.local.mapper

import com.codewithdipesh.habitized.data.local.entity.HabitEntity
import com.codewithdipesh.habitized.data.local.entity.HabitProgressEntity
import com.codewithdipesh.habitized.domain.model.CountParam
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.Status
import com.codewithdipesh.habitized.presentation.util.SingleString
import com.codewithdipesh.habitized.presentation.util.toLocalTime

fun HabitProgress.toEntity(): HabitProgressEntity {
    return HabitProgressEntity(
        progressId = progressId,
        habitId = habitId,
        date = date,
        type = type.toString(),
        countParam = countParam.toString(),
        currentCount = currentCount,
        targetCount = targetCount,
        targetDurationValue = targetDurationValue?.SingleString(),
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
        date = date,
        type = HabitType.fromString(type),
        countParam = CountParam.fromString(countParam),
        currentCount = currentCount,
        targetCount = targetCount,
        targetDurationValue = targetDurationValue?.toLocalTime(),
        currentSessionNumber = currentSessionNumber,
        targetSessionNumber = targetSessionNumber,
        status = Status.fromString(status),
        notes = notes
    )
}
